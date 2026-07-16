package com.example.model

import androidx.compose.ui.graphics.Color

// --- ROOT CARDS ---
enum class RootType {
    REGULAR,      // صحيح سالم
    ASSIMILATED,  // معتل مثال (e.g. وعد)
    HOLLOW,       // معتل أجوف (e.g. قال / قول)
    DEFECTIVE,    // معتل ناقص (e.g. رمى / رمي)
    HAMZATED      // مهموز (e.g. أكل, سأل, قرأ)
}

data class RootCard(
    val id: String,
    val arabic: String, // e.g. "ك ت ب"
    val transliteration: String,
    val englishMeaning: String,
    val type: RootType,
    val baseLetters: String = arabic.replace(" ", "")
)

// --- PATTERN CARDS (WAZAN) ---
enum class PatternCategory {
    ISM_FAIL,      // اسم الفاعل
    ISM_MAFOOL,    // اسم المفعول
    MASDAR,        // المصدر
    ISM_ZAMAN_MAKAN,// اسم الزمان والمكان
    ISM_ALAH,      // اسم الآلة
    MUBALAGHA,     // صيغة المبالغة
    PAST_VERB,     // الفعل الماضي
    PRESENT_VERB,  // الفعل المضارع
    COMMAND_VERB   // فعل الأمر
}

data class PatternCard(
    val id: String,
    val form: Int, // I to X
    val arabicWazan: String, // e.g. "فَاعِل"
    val transliteration: String,
    val category: PatternCategory,
    val englishRule: String,
    val costGold: Int = 0,
    val xpBonus: Int = 15
)

// --- CHARACTERS ---
data class Character(
    val id: String,
    val name: String,
    val title: String,
    val specialty: String,
    val abilityDescription: String,
    val quote: String,
    val initialGold: Int,
    val initialXP: Int,
    val bannerColor: Color
)

// --- MAP PROVINCES (HEX SYSTEM) ---
data class ProvinceHex(
    val id: Int, // Derived Form I to X
    val title: String, // e.g. "Form I: Al-Asas (The Base)"
    val arabicTitle: String, // e.g. "الباب الأول: الأساس"
    val coordinates: Pair<Int, Int>, // x, y for hex placement
    val description: String,
    val challengeLevel: Int,
    val bossName: String,
    val bossTitle: String,
    val bossArabic: String,
    val bossQuote: String,
    val rewardGold: Int,
    val rewardXP: Int
)

// --- EVENT CARDS ---
data class EventCard(
    val id: String,
    val title: String,
    val description: String,
    val type: EventEffectType,
    val effectValue: Int = 0
)

enum class EventEffectType {
    GAIN_GOLD,
    LOSE_GOLD,
    GAIN_XP,
    EXTRA_ROOT_DRAW,
    MOVEMENT_RESTRICTION,
    ANOMALY_BOOST
}

// --- LANGUAGE CHALLENGE CARDS ---
data class ChallengeCard(
    val id: String,
    val title: String,
    val question: String,
    val correctMorphed: String,
    val choices: List<String>,
    val explanation: String,
    val xpReward: Int,
    val goldReward: Int
)

// --- BOSS CHALLENGES ---
data class BossChallenge(
    val id: String,
    val bossName: String,
    val description: String,
    val riddles: List<ChallengeCard>
)

// --- PRE-LOADED DATA CONSTANTS ---
object GameDatabase {

    val CHARACTER_LIST = listOf(
        Character(
            id = "al_farahidi",
            name = "Al-Farahidi",
            title = "The Lexicographer of Basra",
            specialty = "Root Master",
            abilityDescription = "Draw an extra Root Card at the beginning of each turn. High starting Intellect.",
            quote = "\"The boundaries of a language are the boundaries of its speakers' world.\"",
            initialGold = 15,
            initialXP = 10,
            bannerColor = Color(0xFF1E5F48)
        ),
        Character(
            id = "sibawayh",
            name = "Sibawayh",
            title = "The Grammarian of Shiraz",
            specialty = "Sarf Protection",
            abilityDescription = "Once per round, when making an error in the Transformation Workshop, you can retry for free.",
            quote = "\"Speak, so that I may see you through your words.\"",
            initialGold = 10,
            initialXP = 25,
            bannerColor = Color(0xFFBB9353)
        ),
        Character(
            id = "ibn_jinni",
            name = "Ibn Jinni",
            title = "The Morphologist of Mosul",
            specialty = "Rich Secrets",
            abilityDescription = "Earn double Gold rewards when successfully defeating Language Challenges or Bosses.",
            quote = "\"Every letter is a soul, and every wazan is its garment.\"",
            initialGold = 25,
            initialXP = 5,
            bannerColor = Color(0xFF915F24)
        ),
        Character(
            id = "al_kisai",
            name = "Al-Kisa'i",
            title = "The Tutor of Baghdad",
            specialty = "Desert Teleport",
            abilityDescription = "Spend 1 action point to teleport to any adjacent province hex without rolling dice.",
            quote = "\"Beauty of the tongue lies in the clarity of its structures.\"",
            initialGold = 12,
            initialXP = 15,
            bannerColor = Color(0xFF3D5A80)
        )
    )

    val ROOT_CARDS = listOf(
        // Regular
        RootCard("R1", "ك ت ب", "k-t-b", "Writing / Books", RootType.REGULAR),
        RootCard("R2", "ع ل م", "'-l-m", "Knowledge / Science", RootType.REGULAR),
        RootCard("R3", "د خ ل", "d-kh-l", "Entering / Input", RootType.REGULAR),
        RootCard("R4", "ن ص ر", "n-s-r", "Helping / Victory", RootType.REGULAR),
        RootCard("R5", "خ ر ج", "kh-r-j", "Exiting / Output", RootType.REGULAR),
        RootCard("R6", "د ر س", "d-r-s", "Studying / Lessons", RootType.REGULAR),
        RootCard("R7", "ف ت ح", "f-t-h", "Opening / Victory", RootType.REGULAR),
        RootCard("R8", "س م ع", "s-m-'-", "Hearing / Sound", RootType.REGULAR),
        RootCard("R9", "ح م د", "h-m-d", "Praise / Gratitude", RootType.REGULAR),
        
        // Assimilated
        RootCard("R10", "و ع د", "w-'-d", "Promise / Appointment", RootType.ASSIMILATED),
        RootCard("R11", "و ج د", "w-j-d", "Finding / Existence", RootType.ASSIMILATED),
        
        // Hollow
        RootCard("R12", "ق و ل", "q-w-l", "Speaking / Saying", RootType.HOLLOW),
        RootCard("R13", "ب ي ع", "b-y-'-", "Selling / Trade", RootType.HOLLOW),
        
        // Defective
        RootCard("R14", "ر م ي", "r-m-y", "Throwing / Projectile", RootType.DEFECTIVE),
        RootCard("R15", "د ع و", "d-'-w", "Calling / Inviting", RootType.DEFECTIVE),
        
        // Hamzated
        RootCard("R16", "أ ك ل", "'-k-l", "Eating / Food", RootType.HAMZATED),
        RootCard("R17", "س أ ل", "s-'-l", "Asking / Question", RootType.HAMZATED),
        RootCard("R18", "ق ر أ", "q-r-'", "Reading / Reciting", RootType.HAMZATED)
    )

    val PATTERN_CARDS = listOf(
        // Form I Patterns
        PatternCard("P1_1", 1, "فَاعِل", "Fā'il", PatternCategory.ISM_FAIL, "Active Participle (Doer) - e.g. Writer, Knower", xpBonus = 10),
        PatternCard("P1_2", 1, "مَفْعُول", "Maf'ūl", PatternCategory.ISM_MAFOOL, "Passive Participle (Receiver) - e.g. Written, Known", xpBonus = 12),
        PatternCard("P1_3", 1, "مَفْعَل", "Maf'al", PatternCategory.ISM_ZAMAN_MAKAN, "Noun of Place/Time - e.g. Desk/Office (Maktab)", xpBonus = 15),
        PatternCard("P1_4", 1, "مِفْعَال", "Mif'āl", PatternCategory.ISM_ALAH, "Noun of Instrument (Tool) - e.g. Key (Miftāh)", xpBonus = 20),
        PatternCard("P1_5", 1, "فِعَالَة", "Fi'ālah", PatternCategory.MASDAR, "Masdar of Profession/Craft - e.g. Writing (Kitābah)", xpBonus = 15),
        PatternCard("P1_6", 1, "فَعَّال", "Fa''āl", PatternCategory.MUBALAGHA, "Noun of Intensity/Mubalagha - e.g. All-Knowing, Helper", xpBonus = 25),
        
        // Form II
        PatternCard("P2_1", 2, "تَفْعِيل", "Taf'īl", PatternCategory.MASDAR, "Form II Masdar (Systematic/Intense action) - e.g. Teaching (Ta'līm)", xpBonus = 20),
        PatternCard("P2_2", 2, "مُفَعِّل", "Mufa''il", PatternCategory.ISM_FAIL, "Form II Doer (Teacher/Instructor) - e.g. Mu'allim", xpBonus = 22),
        
        // Form III
        PatternCard("P3_1", 3, "مُفَاعَلَة", "Mufā'alah", PatternCategory.MASDAR, "Form III Masdar (Interactive action) - e.g. Correspondent / Discussion", xpBonus = 25),
        
        // Form IV
        PatternCard("P4_1", 4, "إِفْعَال", "If'āl", PatternCategory.MASDAR, "Form IV Masdar (Causative) - e.g. Entry / Injection", xpBonus = 20),
        PatternCard("P4_2", 4, "مُفْعِل", "Muf'il", PatternCategory.ISM_FAIL, "Form IV Active Participle - e.g. Helper, Guide", xpBonus = 22),
        
        // Form X
        PatternCard("P10_1", 10, "اسْتِفْعَال", "Istif'āl", PatternCategory.MASDAR, "Form X Masdar (Seeking/Requesting) - e.g. Inquiring (Istifsār)", xpBonus = 35),
        PatternCard("P10_2", 10, "مُسْتَفْعِل", "Mustaf'il", PatternCategory.ISM_FAIL, "Form X Doer (Inquirer/User) - e.g. Mustaf'il", xpBonus = 35)
    )

    val PROVINCES = listOf(
        ProvinceHex(
            id = 1,
            title = "Form I: Al-Asas",
            arabicTitle = "الأساس الأول",
            coordinates = Pair(0, 0),
            description = "The absolute foundations. This is the birthplace of all Arabic roots. Master the active/passive participles, instrument nouns, and simple past actions.",
            challengeLevel = 1,
            bossName = "Ibn Sibawayh Jr.",
            bossTitle = "Guardian of the Ground Rules",
            bossArabic = "سِيبَوَيْهِ الصَّغِير",
            bossQuote = "\"You cannot construct majestic towers without a firm cornerstone.\"",
            rewardGold = 10,
            rewardXP = 30
        ),
        ProvinceHex(
            id = 2,
            title = "Form II: At-Taf'eel",
            arabicTitle = "التفعيل المضاعف",
            coordinates = Pair(1, -1),
            description = "The Realm of Intensification. Shaddah (doubling) dominates here. Words here denote causative or intensive actions (e.g., علم 'to know' -> علّم 'to teach').",
            challengeLevel = 2,
            bossName = "Al-Khalil",
            bossTitle = "Lord of the Double-Letters",
            bossArabic = "الخليل بن أحمد",
            bossQuote = "\"Listen to the rhythmic heartbeat of the Shaddah!\"",
            rewardGold = 15,
            rewardXP = 45
        ),
        ProvinceHex(
            id = 3,
            title = "Form III: Al-Mufa'alah",
            arabicTitle = "المفاعلة والمشاركة",
            coordinates = Pair(2, -1),
            description = "The Guild of Cooperation. Verbs here involve mutual, interactive actions between two parties (e.g., كاتب 'to correspond with').",
            challengeLevel = 2,
            bossName = "Ibn Jinni",
            bossTitle = "The Master of Dialogues",
            bossArabic = "ابن جنّي الشاعر",
            bossQuote = "\"In language, as in life, victory is forged in reciprocity.\"",
            rewardGold = 20,
            rewardXP = 50
        ),
        ProvinceHex(
            id = 4,
            title = "Form IV: Al-If'aal",
            arabicTitle = "الإفعال المتعدي",
            coordinates = Pair(1, 0),
            description = "The Peaks of Causation. Hamzah prefixes of Form IV reign high, transforming intransitive verbs into powerful transitive forces (e.g., دخل 'to enter' -> أدخل 'to admit').",
            challengeLevel = 3,
            bossName = "Abul-Aswad",
            bossTitle = "Sovereign of the Hamzah",
            bossArabic = "أبو الأسود الدؤلي",
            bossQuote = "\"The hamzah is the spark that drives the verb forward!\"",
            rewardGold = 25,
            rewardXP = 60
        ),
        ProvinceHex(
            id = 5,
            title = "Form V: At-Tafa''ul",
            arabicTitle = "التفعل والمطاوعة",
            coordinates = Pair(0, 1),
            description = "The Reflective Valley. Where actions turn inward upon the subject, representing the state achieved through effort (e.g., تعلّم 'to learn' as the result of teaching).",
            challengeLevel = 3,
            bossName = "Al-Farra",
            bossTitle = "The Reflective Sage",
            bossArabic = "الفرّاء النحوي",
            bossQuote = "\"He who teaches must observe how the student reflects.\"",
            rewardGold = 30,
            rewardXP = 70
        ),
        ProvinceHex(
            id = 6,
            title = "Form VI: At-Tafa'ul",
            arabicTitle = "التفاعل المتبادل",
            coordinates = Pair(-1, 1),
            description = "The Plains of Multiplicity. Where entire assemblies cooperate or feign actions (e.g., تعاون 'to cooperate', تجاهل 'to pretend to ignore').",
            challengeLevel = 4,
            bossName = "Al-Kisa'i",
            bossTitle = "Consul of Assemblies",
            bossArabic = "الكسائي المقرئ",
            bossQuote = "\"Together we stand, but in Sarf, we shift our vowel coordinates!\"",
            rewardGold = 35,
            rewardXP = 80
        ),
        ProvinceHex(
            id = 7,
            title = "Form VII: Al-Infi'aal",
            arabicTitle = "الانفعال الانكساري",
            coordinates = Pair(-1, 0),
            description = "The Passive Shallows. The prefix 'In-' turns active verbs into passive states automatically (e.g., انكسر 'to be shattered').",
            challengeLevel = 4,
            bossName = "Ibn Malik",
            bossTitle = "Guardian of Passive Streams",
            bossArabic = "ابن مالك الأندلسي",
            bossQuote = "\"Observe the glass: it does not just break, it shatters of itself!\"",
            rewardGold = 40,
            rewardXP = 90
        ),
        ProvinceHex(
            id = 8,
            title = "Form VIII: Al-Ifti'aal",
            arabicTitle = "الافتعال الذاتي",
            coordinates = Pair(0, -1),
            description = "The Citadel of Self-Action. The internal '-ta-' infixes require careful phonetic assimilation (e.g., استمع 'to listen closely').",
            challengeLevel = 5,
            bossName = "Al-Zamakhshari",
            bossTitle = "The Phonetic Alchemist",
            bossArabic = "الزمخشري المفسر",
            bossQuote = "\"Phonetics and morphology are two faces of the same golden coin.\"",
            rewardGold = 50,
            rewardXP = 110
        ),
        ProvinceHex(
            id = 9,
            title = "Form IX: Al-If'ilaal",
            arabicTitle = "الافعلال اللوني",
            coordinates = Pair(-2, 1),
            description = "The Prism of Colors and Defects. A rare, high-difficulty province containing words for colors, transformations, and physical defects (e.g., احمرّ 'to turn red').",
            challengeLevel = 5,
            bossName = "Qutrub",
            bossTitle = "Spectre of the Prism",
            bossArabic = "قطرب النحوي",
            bossQuote = "\"Colors fade, but the structural wazan stands eternal.\"",
            rewardGold = 60,
            rewardXP = 130
        ),
        ProvinceHex(
            id = 10,
            title = "Form X: Al-Istif'aal",
            arabicTitle = "الاستفعال والطلب",
            coordinates = Pair(-1, -1),
            description = "The Sanctuary of Seeking. The crown jewel of derived forms, prefixing 'Ista-' to seek, request, or deem a quality (e.g., استغفر 'to seek forgiveness').",
            challengeLevel = 5,
            bossName = "Sufyan Al-Thawri",
            bossTitle = "The Arch-Grammarian",
            bossArabic = "سفيان الثوري",
            bossQuote = "\"Seek, and through the Form X gateway, you shall unlock the Kingdom's treasures.\"",
            rewardGold = 75,
            rewardXP = 150
        )
    )

    val EVENT_CARDS = listOf(
        EventCard("E1", "Caliph's Royal Decree", "Earn an extra +20 Gold on your next successful Form I or Form X transformation.", EventEffectType.GAIN_GOLD, 20),
        EventCard("E2", "Dust Storm of Basra", "A sandstorm hits! Moving costs +1 extra action point next turn.", EventEffectType.MOVEMENT_RESTRICTION, 1),
        EventCard("E3", "Lost Scroll Discovered", "Draw 1 free Root card from the Grand Library deck.", EventEffectType.EXTRA_ROOT_DRAW, 1),
        EventCard("E4", "Grammatical Revelation", "Gain +30 XP instantly as you realize a beautiful phonetic rule.", EventEffectType.GAIN_XP, 30),
        EventCard("E5", "Library Fire!", "Scribes scramble! Lose 10 Gold to pay for parchment restoration.", EventEffectType.LOSE_GOLD, 10),
        EventCard("E6", "Ink Overflow", "Your active transformations this turn gain +5 bonus XP.", EventEffectType.ANOMALY_BOOST, 5)
    )

    val CHALLENGE_CARDS = listOf(
        ChallengeCard(
            id = "C1",
            title = "The Active Participle",
            question = "Transform the root (ك ت ب) using the wazan 'فَاعِل'. What is the correct word?",
            correctMorphed = "كَاتِب",
            choices = listOf("كَاتِب", "مَكْتُوب", "كِتَابَة", "مَكْتَب"),
            explanation = "The pattern فَاعِل (Fā'il) represents the active participle (doer). For (ك ت ب), it becomes كَاتِب (Kātib - writer).",
            xpReward = 15,
            goldReward = 5
        ),
        ChallengeCard(
            id = "C2",
            title = "The Passive Participle",
            question = "Transform the root (ع ل م) using the wazan 'مَفْعُول'. What is the correct word?",
            correctMorphed = "مَعْلُوم",
            choices = listOf("مُعَلِّم", "عَالِم", "مَعْلُوم", "مَعْلَمَة"),
            explanation = "The pattern مَفْعُول (Maf'ūl) represents the passive participle. For (ع ل م), it is مَعْلُوم (Ma'lūm - known).",
            xpReward = 15,
            goldReward = 5
        ),
        ChallengeCard(
            id = "C3",
            title = "Noun of Place/Time",
            question = "Transform the root (د خ ل) using the wazan 'مَفْعَل'. What is the correct word?",
            correctMorphed = "مَدْخَل",
            choices = listOf("مَدْخَل", "دَاخِل", "مَدْخُول", "دِخَالَة"),
            explanation = "The pattern مَفْعَل (Maf'al) creates the noun of place or time. For (د خ ل), it is مَدْخَل (Madkhal - entrance).",
            xpReward = 20,
            goldReward = 10
        ),
        ChallengeCard(
            id = "C4",
            title = "Assimilated Weak Verb",
            question = "For the assimilated root (و ع د) ('to promise'), what is the Form I Active Participle?",
            correctMorphed = "وَاعِد",
            choices = listOf("مَوْعُود", "وَاعِد", "مِيعَاد", "عِدَة"),
            explanation = "In assimilated verbs (starting with waw), the active participle (فَاعِل) remains regular: وَاعِد (Wā'id - one who promises).",
            xpReward = 25,
            goldReward = 12
        ),
        ChallengeCard(
            id = "C5",
            title = "Hollow Verb Transformation",
            question = "The root (ق و ل) has a weak middle letter. What is its Form I Passive Participle (مَفْعُول)?",
            correctMorphed = "مَقُول",
            choices = listOf("مَقْوُول", "مَقُول", "قَائِل", "مَقَال"),
            explanation = "For hollow verbs, the passive participle drops one 'waw' for ease of pronunciation, turning مَقْوُول into مَقُول (Maqūl - said).",
            xpReward = 30,
            goldReward = 15
        ),
        ChallengeCard(
            id = "C6",
            title = "Form X Seeking Noun",
            question = "Transform the root (ع ل م) into a Form X Verbal Noun (اسْتِفْعَال). What is the word?",
            correctMorphed = "اسْتِعْلَام",
            choices = listOf("مُسْتَعْلِم", "اسْتَعْلَمَ", "اسْتِعْلَام", "مُعْلَم"),
            explanation = "The verbal noun of Form X is اسْتِفْعَال. Combining with (ع ل م) results in اسْتِعْلَام (Isti'lām - inquiry / seeking information).",
            xpReward = 35,
            goldReward = 20
        ),
        ChallengeCard(
            id = "C7",
            title = "Defective Verb Masdar",
            question = "The defective root (د ع و) ('to call') becomes what Form I Masdar?",
            correctMorphed = "دَعْوَة",
            choices = listOf("دَاعِي", "مَدْعُوّ", "دَعْوَة", "تَدَاعِي"),
            explanation = "The base Form I masdar for (د ع و) is دَعْوَة (Da'wah - invitation/call), where the weak letter becomes a regular ta-marbutah.",
            xpReward = 30,
            goldReward = 15
        ),
        ChallengeCard(
            id = "C8",
            title = "Form II Intensive",
            question = "If (ع ل م) is transformed into the Form II Doer (مُفَعِّل), who is it?",
            correctMorphed = "مُعَلِّم",
            choices = listOf("عَالِم", "مَعْلُوم", "مُعَلِّم", "تَعْلِيم"),
            explanation = "The Form II doer مُفَعِّل (Mufa''il) for (ع ل م) is مُعَلِّم (Mu'allim - teacher/educator).",
            xpReward = 20,
            goldReward = 10
        )
    )

    // Boss battles - matching provinces
    val BOSS_BATTLES = listOf(
        BossChallenge(
            id = "B1",
            bossName = "Ibn Sibawayh Jr.",
            description = "To unlock the Library of Al-Asas, you must identify three basic Form I morphological structures.",
            riddles = listOf(CHALLENGE_CARDS[0], CHALLENGE_CARDS[1], CHALLENGE_CARDS[2])
        ),
        BossChallenge(
            id = "B2",
            bossName = "Al-Khalil",
            description = "The Lord of Double-Letters challenges you to master the intensive Form II and Form IV rules.",
            riddles = listOf(CHALLENGE_CARDS[7], CHALLENGE_CARDS[5], CHALLENGE_CARDS[3])
        ),
        BossChallenge(
            id = "B3",
            bossName = "Sufyan Al-Thawri",
            description = "The Arch-Grammarian demands perfect knowledge of complex hollow and defective transformations.",
            riddles = listOf(CHALLENGE_CARDS[4], CHALLENGE_CARDS[6], CHALLENGE_CARDS[5])
        )
    )
}

// --- LONG FORM LITERATURE FOR THE GRAND LIBRARY ---
object GameLiterature {

    val INTRODUCTION = """
        Sarf Quest: Kingdom of Arabic is a premium tabletop strategy board game that integrates Arabic Morphological Grammar (Sarf - علم الصرف) into a highly addictive area-influence and resource-management system. 
        
        Designed for madrasa students, college academics, Arabic learners, families, and tabletop gamers, it turns abstract grammatical formulas (Wazan patterns) into strategic pathways. 
        
        Instead of rote memorization, players treat Arabic root letters as physical elements that must be forged using structural patterns into powerful words, which then secure routes, capture ancient archives, and trigger powerful verbal combos.
    """.trimIndent()

    val BOARD_LAYOUT = """
        The board is a modular hexagonal grid representing the legendary Kingdom of Arabic. 
        
        - 10 Large Province Hexes: Represent Derived Forms I through X. Each hex displays its associated base template (e.g., Form I shows فَعَلَ, Form II shows فَعَّلَ).
        - Caravan Routes: Highlighted borders that connect adjacent hexes. Players must occupy or control routes to transport roots.
        - Ancient Libraries: Scattered at hex centers. Defeating the local Scholar Guardian allows the player to station a Scribe Token, granting recurring gold and card draws.
        - Sandstorms and Oases: Hazard and relief hex spaces that alter movement.
    """.trimIndent()

    val COMPONENTS_LIST = """
        • 10 Modular Hexagonal Province Tiles (thick linen-finish, gold-gilded edges)
        • 1 Center Royal Capital Tile (The Golden Diwan)
        • 4 Player Boards (parchment textured with wooden slots for upgrade pegs)
        • 80 Linen Root Cards (containing 3-letter roots with transliteration and semantic category icons)
        • 60 Gold-foil Pattern Cards (Wazans categorized by noun/verb forms, Form classification, and strategic power text)
        • 30 Calligraphy Event Cards (historical and tactical events of the Islamic Golden Age)
        • 40 Scholar Challenge Cards (riddles and transformations)
        • 4 Premium Wooden Scholar Tokens (custom silhouettes representing the legendary master linguists)
        • 40 Wooden Scribe Tokens (for territory influence and library control)
        • 50 Custom Metal Gold Dinars (embossed with Islamic geometric art)
        • 2 Wooden Directional Dice & 1 Polyhedral Sarf Die
        • 1 Elegant Golden Stylus (active player marker)
        • 1 Full-color Rulebook & Sarf Master Reference Sheet
    """.trimIndent()

    val RULES_AND_TURNS = """
        Each player's turn consists of 5 phases, allowing for tactical agency and planning:

        1. MOVEMENT PHASE:
           - Spend 1 action point to move your Scholar Token along caravan routes to an adjacent Province Hex.
           - Or, roll the Caravan Die to gain free movement.

        2. RESOURCE COLLECTION PHASE:
           - Draw 1 Root Card from the general library deck.
           - Draw 1 Pattern Card from the wazan marketplace.
           - Collect gold income based on the libraries your Scribe Tokens control.

        3. FORGING & TRANSFORMATION PHASE:
           - This is the core strategy. Select 1 Root (e.g., كتب) and 1 Pattern Card (e.g., مَفْعَل).
           - Consult the transformation sheet: combine the Root letters into the active Wazan template.
           - If you correctly morph the root into 'مَكْتَب' (Maktab - desk/office), you successfully forge the word!
           - Consequences of correct answer: Place a Scribe Token on the current hex to claim territory, collect 10 Gold, and gain the card's XP.
           - Grammatical Anomaly (Incorrect answer): If you fail, the word becomes a "broken relic." No territory is captured, but you draw a 'Grammatical Anomaly' event, which redirects your focus (e.g., a mini-quest to fix the broken letters).

        4. CHALLENGE PHASE:
           - Declare a challenge on the province's Library, or challenge another player's Scribe to a linguistic debate.
           - Solve a Scholar Challenge card. Win extra gold or steal root cards.

        5. END PHASE:
           - Pass the Golden Stylus to the next player.
           - Check your hidden objectives.
    """.trimIndent()

    val WIN_CONDITIONS = """
        The game ends when the final Scholar Guardian of the 10th Province is defeated, or when a player reaches the rank of 'Grand Master of Sarf' (Level 5). 

        Points are calculated from:
        - XP Level (10 points per level)
        - Controlled Libraries & Provinces (5 points per Scribe Token)
        - Gold Dinars (1 point per 5 dinars)
        - Completed Hidden Objectives (e.g., 'The Hollow Master': successfully forge 3 hollow root words).
        - Forged Word Cards (XP listed on cards)

        The player with the highest total points is crowned the Grand Master of Sarf!
    """.trimIndent()

    val CHARACTER_ABILITIES_MANUAL = """
        • AL-FARAHIDI: "The Root Master." Draw an extra Root Card at the beginning of each turn. His encyclopedic knowledge allows him to choose from a wider selection of linguistic elements, making regular and weak combinations much easier to execute.
        
        • SIBAWAYH: "The Guarded Tongue." Once per round, when making an error in the Transformation Workshop, he can rewind time to correct his answer. An incredibly forgiving character for players still mastering complex irregular verbs.
        
        • IBN JINNI: "The Wealth of Secrets." Earns double Gold rewards when successfully completing Language Challenges or Boss battles. Ibn Jinni specializes in complex phonetic transformations, and Caliphs pay dearly for his scrolls.
        
        • AL-KISA'I: "The Nomad Scholar." Can spend 1 action point to teleport to any adjacent province hex without rolling dice. Excellent for quick tactical maneuvers and rapid territory capture across the Kingdom.
    """.trimIndent()

    val CAMPAIGN_MODE = """
        The Solo/Cooperative Campaign takes the player on a 10-chapter linguistic voyage across the Kingdom of Arabic. 
        
        - Chapter 1: The Gates of Al-Asas (Form I). Learn basic active and passive doer nouns.
        - Chapter 3: The Cooperative Assembly (Forms II & III). Master intensive shaddah verbs and collaborative dialogs.
        - Chapter 7: The Passive Stream (Forms VII & VIII). Master self-actions and phonetic assimilation.
        - Chapter 10: The Sovereign of Seeking (Form X). Defeat Sufyan Al-Thawri, the Arch-Grammarian, in the Royal Palace of Kufa to seal your mastery.
        
        Each chapter introduces unique setup hazards (e.g., the Euphrates flood blocking routes) and distinct victory requirements.
    """.trimIndent()

    val PRINT_PLAY = """
        Sarf Quest is designed to be highly accessible. The Print-and-Play (PnP) PDF files can be printed on standard A4 cards:
        - Hex Tiles: Can be printed on cardstock and cut into hexagons.
        - Cards: Standard poker card size (2.5" x 3.5"). Print front and back (parchment backings provided).
        - Player Boards: Printable grids with tracking paths for Gold, XP, and active hand limits.
        - Reference Mat: A single page detailing the 10 verb forms, the vowel transformations, and the weak verb modifications.
    """.trimIndent()

    val TOURNEY_RULES = """
        Competitive Tournament Rules enforce a strict timer and eliminate hand hidden secrets:
        - Blitz Sarf: Players have exactly 30 seconds to perform their morphological transformations in Phase 3. Failing the timer results in automatic Grammatical Anomaly.
        - The Draft: At the start of the tournament, players draft 5 Root Cards and 5 Pattern Cards.
        - Restricted Libraries: Players can challenge any library, but defending players can play 'Grammar Objection' cards to force double-blind verbal showdowns.
    """.trimIndent()

    val TEACHER_GUIDE = """
        TEACHER'S LESSON PLAN & GUIDE:
        Sarf Quest is an ideal educational tool that fits perfectly into Arabic language curriculums:
        - Group Dynamics: Divide a class of 24 students into 6 guilds (teams of 4). Each guild shares a player board and cooperates on transformations.
        - Targeted Focus: Teachers can limit the hex board to only Form I and Form II for beginners, slowly adding Forms III-X as lessons progress.
        - Gamified Quizzing: Use the Companion App's "Linguistic Showdown" screen as a classroom buzzer game. Guilds compete to morph roots on the chalkboard first.
        - Scaffolding: The game rewards attempts. When a student makes a mistake, they do not get 'eliminated'. Instead, they draw an 'Anomaly Card' which contains a helpful clue and sets a micro-learning goal.
    """.trimIndent()

    val KICKSTARTER_MARKETING = """
        KICKSTARTER PRODUCT BRIEF:
        Title: Sarf Quest - The Premium Arabic Morphology Board Game
        Slogan: "Forge Root Letters. Unleash Powerful Words. Master Arabic Sarf Through Strategy."
        
        Estimated Retail Price: ${'$'}59 USD
        Kickstarter Goal: ${'$'}15,000 USD
        
        Pledge Tiers:
        1. Scribe Tier (${'$'}15): Digital Print-and-Play files, high-res manuals, and the Premium Android Companion App unlock.
        2. Master Linguist Tier (${'$'}49): Physical standard edition with linen-finish cards, premium paper board, and wooden scholar tokens.
        3. Grand Caliph Tier (${'$'}89): Deluxe edition with custom metal gold dinars, emerald-foil boxes, wooden card trays, and leather-bound master reference sheets.
    """.trimIndent()

    val MANUFACTURING = """
        MANUFACTURING & PRODUCTION RECOMMENDATIONS:
        - Eco-Friendly Wooden Tokens: Sourced from sustainable FSC-certified birch wood. Silk-screened with calligraphy markings.
        - Linen Cardstock: Black-core German cardstock (310gsm) with a premium linen air-cushion finish, ensuring high tactile premium feel and easy shuffling.
        - Gold Foil Embossing: Hot stamp gold-foil borders on the Pattern cards and box cover to make the premium Andalusian Islamic art shine on game night.
        - Cardboard Tiles: 2mm grayboard with a linen texture wrap for the hexagonal modular map pieces, ensuring they lay perfectly flat and resist wear.
    """.trimIndent()

    val EDUCATIONAL_IMPACT = """
        EDUCATIONAL IMPACT & COGNITIVE ANALYSIS:
        - Active Learning: Rather than passively staring at conjugations, the player acts as a 'linguistic engineer', manually inserting 3 consonants (root) into a structured mold (wazan) and observing the phoneme shift.
        - Visual Scaffold: Colors (Emerald for regular, Orange for weak verbs, Indigo for derived forms) provide strong cognitive anchors.
        - Immediate Feedback Loop: Successful morphing yields gold and territory immediately, reinforcing correct schema, while error cards offer scaffolding.
        - Memory Consolidation: Repeated tactical play ensures the 10 Derived Verb Forms become second nature, allowing students to instantly recognize unfamiliar words in Islamic texts.
    """.trimIndent()
}
