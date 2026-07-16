package com.example.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.model.*

enum class SarfScreen {
    Home,
    Campaign,
    PassPlay,
    Workshop,
    Companion,
    Library
}

// Pass & Play Player State
data class PlayerState(
    val id: Int,
    val name: String,
    val character: Character,
    var gold: Int,
    var xp: Int,
    var level: Int = 1,
    var handRoots: List<RootCard> = emptyList(),
    var handPatterns: List<PatternCard> = emptyList(),
    var score: Int = 0
)

class SarfGameViewModel : ViewModel() {

    // --- NAVIGATION ---
    var currentScreen by mutableStateOf(SarfScreen.Home)

    // --- ACTIVE PLAYER SELECTOR ---
    val characters = GameDatabase.CHARACTER_LIST
    var selectedCharacter by mutableStateOf(characters[0])

    // --- CAMPAIGN STATE (SOLO MODE) ---
    var playerLevel by mutableStateOf(1)
    var playerXP by mutableStateOf(10)
    var playerGold by mutableStateOf(15)
    var currentProvinceId by mutableStateOf(1) // Form I to X
    
    // Captured provinces ( Form Id -> Has Controlled Scribe )
    val controlledProvinces = mutableStateListOf<Int>()
    
    // Hand of cards in Campaign
    val campaignHandRoots = mutableStateListOf<RootCard>()
    val campaignHandPatterns = mutableStateListOf<PatternCard>()
    
    // Event State
    var activeCampaignEvent by mutableStateOf<EventCard?>(null)
    var campaignLog = mutableStateListOf<String>()

    // --- WORKSHOP STATE ---
    var workshopSelectedRoot by mutableStateOf<RootCard?>(GameDatabase.ROOT_CARDS[0])
    var workshopSelectedPattern by mutableStateOf<PatternCard?>(GameDatabase.PATTERN_CARDS[0])
    var workshopStreak by mutableStateOf(0)
    var workshopQuizActive by mutableStateOf(false)
    var workshopQuizCorrectCount by mutableStateOf(0)
    var workshopUserAnswer by mutableStateOf("")
    var workshopFeedbackMessage by mutableStateOf("")
    var workshopFeedbackSuccess by mutableStateOf<Boolean?>(null)

    // --- COMPANION TOOLS STATE ---
    var companionDiceResult1 by mutableStateOf(1)
    var companionDiceResult2 by mutableStateOf(3)
    var companionSarfDieResult by mutableStateOf("Root Draw (+1)")
    var companionLastRootDrawn by mutableStateOf<RootCard?>(null)
    var companionLastPatternDrawn by mutableStateOf<PatternCard?>(null)
    var companionLastEventDrawn by mutableStateOf<EventCard?>(null)

    // --- BOSS FIGHT STATE ---
    var activeBossChallenge by mutableStateOf<BossChallenge?>(null)
    var activeBossRiddleIndex by mutableStateOf(0)
    var bossSelectedOption by mutableStateOf("")
    var bossBattleFeedback by mutableStateOf("")
    var bossBattleSuccess by mutableStateOf<Boolean?>(null)

    // --- PASS & PLAY STATE ---
    var passPlayPlayers = mutableStateListOf<PlayerState>()
    var passPlayActiveIndex by mutableStateOf(0)
    var passPlayTurnPhase by mutableStateOf("Resource Draw") // "Resource Draw", "Action", "End"
    var passPlayStatusLog = mutableStateListOf<String>()

    init {
        resetCampaign()
        resetPassPlay()
    }

    // --- CAMPAIGN CONTROLLERS ---
    fun resetCampaign() {
        playerLevel = 1
        playerXP = selectedCharacter.initialXP
        playerGold = selectedCharacter.initialGold
        currentProvinceId = 1
        controlledProvinces.clear()
        controlledProvinces.add(1) // start controlling province 1
        campaignHandRoots.clear()
        // Draw 3 random roots and 2 random patterns to start
        campaignHandRoots.addAll(GameDatabase.ROOT_CARDS.shuffled().take(3))
        campaignHandPatterns.clear()
        campaignHandPatterns.addAll(GameDatabase.PATTERN_CARDS.shuffled().take(2))
        activeCampaignEvent = null
        campaignLog.clear()
        campaignLog.add("Welcome to the Kingdom of Arabic! Your quest begins in Form I: Al-Asas.")
    }

    fun rollForMovement(): Int {
        val roll = (1..6).random()
        campaignLog.add("Rolled a $roll for movement.")
        return roll
    }

    fun moveToProvince(id: Int) {
        val targetHex = GameDatabase.PROVINCES.find { it.id == id }
        if (targetHex != null) {
            currentProvinceId = id
            campaignLog.add("Traveled to ${targetHex.title} (${targetHex.arabicTitle}).")
            // Apply a random event on travel
            triggerCampaignEvent()
        }
    }

    private fun triggerCampaignEvent() {
        val event = GameDatabase.EVENT_CARDS.random()
        activeCampaignEvent = event
        campaignLog.add("Special Event triggered: ${event.title} - ${event.description}")
        
        when (event.type) {
            EventEffectType.GAIN_GOLD -> {
                playerGold += event.effectValue
                campaignLog.add("Gained ${event.effectValue} Gold Dinars.")
            }
            EventEffectType.LOSE_GOLD -> {
                playerGold = maxOf(0, playerGold - event.effectValue)
                campaignLog.add("Lost ${event.effectValue} Gold Dinars.")
            }
            EventEffectType.GAIN_XP -> {
                gainXP(event.effectValue)
            }
            EventEffectType.EXTRA_ROOT_DRAW -> {
                val newRoot = GameDatabase.ROOT_CARDS.random()
                campaignHandRoots.add(newRoot)
                campaignLog.add("Drawn extra Root card: ${newRoot.arabic} (${newRoot.transliteration}).")
            }
            EventEffectType.MOVEMENT_RESTRICTION -> {
                campaignLog.add("Your actions are constrained by desert storms.")
            }
            EventEffectType.ANOMALY_BOOST -> {
                campaignLog.add("An ink overflow increases active spellcraft output.")
            }
        }
    }

    fun gainXP(amount: Int) {
        playerXP += amount
        campaignLog.add("Earned +$amount XP.")
        // Level up algorithm (100 XP per level)
        val targetLevel = (playerXP / 100) + 1
        if (targetLevel > playerLevel) {
            playerLevel = minOf(5, targetLevel)
            campaignLog.add("🎉 Congratulations! You leveled up to Level $playerLevel (${getRankName(playerLevel)})!")
        }
    }

    fun performCampaignTransformation(root: RootCard, pattern: PatternCard, userInput: String): Boolean {
        val expected = calculateMorphedWord(root, pattern)
        val isCorrect = userInput.trim() == expected
        
        if (isCorrect) {
            campaignHandRoots.remove(root)
            campaignHandPatterns.remove(pattern)
            
            val xpGain = pattern.xpBonus
            val goldGain = 10 + (if (selectedCharacter.id == "ibn_jinni") 10 else 0)
            
            playerGold += goldGain
            gainXP(xpGain)
            
            // capture the province!
            if (!controlledProvinces.contains(currentProvinceId)) {
                controlledProvinces.add(currentProvinceId)
                campaignLog.add("Captured ${GameDatabase.PROVINCES.find { it.id == currentProvinceId }?.title}!")
            }
            
            campaignLog.add("Successfully forged word: $expected ($userInput) from Root '${root.arabic}' and Wazan '${pattern.arabicWazan}'.")
            
            // Draw replacement cards
            campaignHandRoots.add(GameDatabase.ROOT_CARDS.random())
            campaignHandPatterns.add(GameDatabase.PATTERN_CARDS.random())
            return true
        } else {
            // Anomaly penalty - draw a consequence or lose gold
            val loss = 5
            playerGold = maxOf(0, playerGold - loss)
            campaignLog.add("⚠️ Grammatical Anomaly! Failed to forge '${pattern.arabicWazan}' of root '${root.arabic}'. Lost $loss Gold Dinars. Expected: $expected, but got: $userInput.")
            return false
        }
    }

    // --- WORKSHOP & SOLVER LOGIC ---
    fun calculateMorphedWord(root: RootCard, pattern: PatternCard): String {
        val r1 = root.baseLetters.getOrNull(0) ?: 'ف'
        val r2 = root.baseLetters.getOrNull(1) ?: 'ع'
        val r3 = root.baseLetters.getOrNull(2) ?: 'ل'

        // Implement morphological spelling rules for regular and weak/irregular verbs
        return when (pattern.arabicWazan) {
            "فَاعِل" -> {
                // Active Participle Form I
                if (root.type == RootType.HOLLOW) {
                    // Hollow (e.g. قول -> قائل) - middle weak letter turns into hamzah on ya bearer
                    "${r1}ائِل"
                } else if (root.type == RootType.DEFECTIVE) {
                    // Defective (e.g. رمي -> رامٍ / رامي)
                    "${r1}امٍ"
                } else {
                    "${r1}اعِ${r3}"
                }
            }
            "مَفْعُول" -> {
                // Passive Participle Form I
                if (root.type == RootType.HOLLOW) {
                    // Hollow (e.g. قول -> مقول, بيع -> مبيع)
                    if (r2 == 'و') "مَقُول" else "مَبِيع"
                } else if (root.type == RootType.DEFECTIVE) {
                    // Defective (e.g. رمي -> مرميّ, دعو -> مدعوّ)
                    if (r3 == 'ي') "مَرْمِيّ" else "مَدْعُوّ"
                } else {
                    "مَفْ${r2}و${r3}"
                }
            }
            "مَفْعَل" -> {
                // Noun of Place/Time
                "مَفْ${r2}َ${r3}"
            }
            "مِفْعَال" -> {
                // Instrument Noun
                if (root.type == RootType.ASSIMILATED) {
                    // Assimilated (e.g. وعد -> ميعاد) - initial waw becomes ya
                    "مِي${r2}ا${r3}"
                } else {
                    "مِفْ${r2}ا${r3}"
                }
            }
            "فِعَالَة" -> {
                "فِ${r2}ا${r3}َة"
            }
            "فَعَّال" -> {
                "${r1}${r2}ّ${r3}"
            }
            "تَفْعِيل" -> {
                "تَفْ${r2}ِي${r3}"
            }
            "مُفَعِّل" -> {
                "مُفَ${r2}ِّ${r3}"
            }
            "مُفَاعَلَة" -> {
                "مُفَا${r2}َ${r3}َة"
            }
            "إِفْعَال" -> {
                if (root.type == RootType.ASSIMILATED) {
                    // Assimilated (e.g. وجد -> إيجاد) - initial waw becomes ya
                    "إِي${r2}ا${r3}"
                } else if (root.type == RootType.HOLLOW) {
                    // Hollow (e.g. قول -> إقالة) - middle weak drops and gets ta marbuta
                    "إِ${r1}ا${r3}َة"
                } else {
                    "إِفْ${r2}ا${r3}"
                }
            }
            "مُفْعِل" -> {
                "مُفْ${r2}ِ${r3}"
            }
            "اسْتِفْعَال" -> {
                if (root.type == RootType.HOLLOW) {
                    // Hollow (e.g. قول -> استقالة)
                    "اسْتِ${r1}ا${r3}َة"
                } else {
                    "اسْتِفْ${r2}ا${r3}"
                }
            }
            "مُسْتَفْعِل" -> {
                "مُسْتَفْ${r2}ِ${r3}"
            }
            else -> "مَفْعُول"
        }.replace('ف', r1).replace('ع', r2).replace('ل', r3)
    }

    fun checkWorkshopAnswer() {
        val root = workshopSelectedRoot
        val pattern = workshopSelectedPattern
        if (root == null || pattern == null) return

        val correctAnswer = calculateMorphedWord(root, pattern)
        val isCorrect = workshopUserAnswer.trim() == correctAnswer

        if (isCorrect) {
            workshopStreak++
            workshopFeedbackSuccess = true
            workshopFeedbackMessage = "🏆 Exact Match! '${workshopUserAnswer.trim()}' is the correct morphological form of root ${root.arabic} with pattern ${pattern.arabicWazan}. (+15 Gold & +30 XP in companion database)"
            if (workshopQuizActive) {
                workshopQuizCorrectCount++
            }
        } else {
            workshopStreak = 0
            workshopFeedbackSuccess = false
            workshopFeedbackMessage = "❌ Incorrect. Got: '${workshopUserAnswer.trim()}'. The correct Classical form is '$correctAnswer'. Remember: ${pattern.englishRule}"
        }
    }

    // --- COMPANION ASSISTANT CONTROLLERS ---
    fun rollCompanionDice() {
        companionDiceResult1 = (1..6).random()
        companionDiceResult2 = (1..6).random()
        
        val sarfDieOutcomes = listOf(
            "Root Card (+1)",
            "Wazan Card (+1)",
            "Gold Dinar (+5)",
            "Caravan Advance (+1)",
            "Scribe Token (+1)",
            "Event Scroll (+1)",
            "Linguistic Anomaly (Skip Turn!)",
            "Golden Diwan Choice!"
        )
        companionSarfDieResult = sarfDieOutcomes.random()
    }

    fun drawRootCompanion() {
        companionLastRootDrawn = GameDatabase.ROOT_CARDS.random()
    }

    fun drawPatternCompanion() {
        companionLastPatternDrawn = GameDatabase.PATTERN_CARDS.random()
    }

    fun drawEventCompanion() {
        companionLastEventDrawn = GameDatabase.EVENT_CARDS.random()
    }

    // --- BOSS CHALLENGE ENGINE ---
    fun startBossFight(province: ProvinceHex) {
        val boss = GameDatabase.BOSS_BATTLES.find { it.id == "B${province.challengeLevel}" } 
            ?: GameDatabase.BOSS_BATTLES[0]
        activeBossChallenge = boss
        activeBossRiddleIndex = 0
        bossSelectedOption = ""
        bossBattleFeedback = ""
        bossBattleSuccess = null
    }

    fun submitBossAnswer(selectedOption: String) {
        val boss = activeBossChallenge ?: return
        val riddle = boss.riddles[activeBossRiddleIndex]
        
        val isCorrect = selectedOption == riddle.correctMorphed
        if (isCorrect) {
            bossBattleSuccess = true
            bossBattleFeedback = "✅ Correct! ${riddle.explanation}"
            
            // Advance to next or complete battle
            if (activeBossRiddleIndex < boss.riddles.size - 1) {
                activeBossRiddleIndex++
                bossSelectedOption = ""
            } else {
                // Battle won!
                val rewardXP = 50
                val rewardGold = 25
                playerGold += rewardGold
                gainXP(rewardXP)
                campaignLog.add("🏆 DEFEATED BOSS ${boss.bossName}! Unlocked ancient manuscript and gained $rewardGold Gold & $rewardXP XP.")
                activeBossChallenge = null
            }
        } else {
            bossBattleSuccess = false
            bossBattleFeedback = "❌ Incorrect. Your logic offended the ancient scholar. You lose 2 actions and 5 gold dinars."
            playerGold = maxOf(0, playerGold - 5)
        }
    }

    // --- PASS & PLAY MULTIPLAYER CONTROLLERS ---
    fun resetPassPlay() {
        passPlayPlayers.clear()
        val chars = GameDatabase.CHARACTER_LIST
        passPlayPlayers.add(
            PlayerState(
                id = 1,
                name = "Scholar Reda",
                character = chars[0],
                gold = chars[0].initialGold,
                xp = chars[0].initialXP,
                level = 1,
                handRoots = GameDatabase.ROOT_CARDS.shuffled().take(3),
                handPatterns = GameDatabase.PATTERN_CARDS.shuffled().take(2)
            )
        )
        passPlayPlayers.add(
            PlayerState(
                id = 2,
                name = "Scholar Amina",
                character = chars[1],
                gold = chars[1].initialGold,
                xp = chars[1].initialXP,
                level = 1,
                handRoots = GameDatabase.ROOT_CARDS.shuffled().take(3),
                handPatterns = GameDatabase.PATTERN_CARDS.shuffled().take(2)
            )
        )
        passPlayActiveIndex = 0
        passPlayTurnPhase = "Resource Draw"
        passPlayStatusLog.clear()
        passPlayStatusLog.add("Let the game begin! Turn: Player 1 (Scholar Reda). Draw cards to start.")
    }

    fun passPlayDrawRoot() {
        val player = passPlayPlayers[passPlayActiveIndex]
        val newRoot = GameDatabase.ROOT_CARDS.random()
        player.handRoots = player.handRoots + newRoot
        passPlayStatusLog.add("${player.name} drew root card: ${newRoot.arabic}.")
        passPlayTurnPhase = "Action"
    }

    fun passPlayDrawPattern() {
        val player = passPlayPlayers[passPlayActiveIndex]
        val newPattern = GameDatabase.PATTERN_CARDS.random()
        player.handPatterns = player.handPatterns + newPattern
        passPlayStatusLog.add("${player.name} drew pattern card: ${newPattern.arabicWazan}.")
        passPlayTurnPhase = "Action"
    }

    fun passPlayExecuteTransformation(root: RootCard, pattern: PatternCard, userInput: String): Boolean {
        val player = passPlayPlayers[passPlayActiveIndex]
        val expected = calculateMorphedWord(root, pattern)
        val isCorrect = userInput.trim() == expected

        if (isCorrect) {
            // successful forge
            player.handRoots = player.handRoots.filter { it.id != root.id }
            player.handPatterns = player.handPatterns.filter { it.id != pattern.id }
            
            player.gold += 12
            player.xp += pattern.xpBonus
            player.score += 10
            
            // recalculate level
            val targetLvl = (player.xp / 100) + 1
            if (targetLvl > player.level) {
                player.level = minOf(5, targetLvl)
                passPlayStatusLog.add("🎉 ${player.name} leveled up to Level ${player.level}!")
            }

            passPlayStatusLog.add("✅ ${player.name} forged '$expected' and gained 12 Gold, ${pattern.xpBonus} XP.")
            
            // auto-draw replacement cards
            player.handRoots = player.handRoots + GameDatabase.ROOT_CARDS.random()
            player.handPatterns = player.handPatterns + GameDatabase.PATTERN_CARDS.random()
            
            passPlayTurnPhase = "End"
            return true
        } else {
            // failed
            player.gold = maxOf(0, player.gold - 5)
            passPlayStatusLog.add("⚠️ ${player.name} failed to forge '${pattern.arabicWazan}' of root '${root.arabic}'. Lost 5 Gold.")
            return false
        }
    }

    fun passPlayEndTurn() {
        val currentP = passPlayPlayers[passPlayActiveIndex]
        passPlayActiveIndex = (passPlayActiveIndex + 1) % passPlayPlayers.size
        val nextP = passPlayPlayers[passPlayActiveIndex]
        passPlayTurnPhase = "Resource Draw"
        passPlayStatusLog.add("Turn passed from ${currentP.name} to ${nextP.name}.")
    }

    // --- ACCESSOR UTILITIES ---
    fun getRankName(level: Int): String {
        return when (level) {
            1 -> "Novice Scholar (طالب مبتدئ)"
            2 -> "Scribe of the Diwan (كاتب الديوان)"
            3 -> "Master Grammarian (أستاذ النحو)"
            4 -> "Grand Linguist (علامة اللغة)"
            5 -> "Sarf Grand Master (شيخ الصرف الأكبر)"
            else -> "Scholar"
        }
    }
}
