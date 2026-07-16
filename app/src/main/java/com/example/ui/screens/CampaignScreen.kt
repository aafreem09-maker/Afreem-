package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GameDatabase
import com.example.model.ProvinceHex
import com.example.model.PatternCard
import com.example.model.RootCard
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.SarfGameViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CampaignScreen(
    viewModel: SarfGameViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    
    // Selection state inside Forge
    var selectedRootForForge by remember { mutableStateOf<RootCard?>(null) }
    var selectedPatternForForge by remember { mutableStateOf<PatternCard?>(null) }
    var forgeInputAnswer by remember { mutableStateOf("") }
    var forgeResultFeedback by remember { mutableStateOf("") }
    var forgeSuccessResult by remember { mutableStateOf<Boolean?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmParchment)
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HighDensityPlayerHeader(
            viewModel = viewModel,
            onBackClick = { viewModel.currentScreen = com.example.viewmodel.SarfScreen.Home },
            onResetClick = { viewModel.resetCampaign() },
            titleText = "SOLO QUEST: CAMPAIGN"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- MAP AREA ---
        Text(
            text = "The Modular Hex Board: Kingdom of Arabic",
            color = EmeraldDeep,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Click any hexagon representing Forms I-X to travel or challenge the local Scholar Boss.",
            color = AccentBronze,
            fontSize = 11.sp,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Hexagons Hex Grid Layout (Forms I - X)
        // We will display them as responsive FlowRow layout
        FlowRow(
            horizontalArrangement = Arrangement.Center,
            verticalArrangement = Arrangement.Center,
            maxItemsInEachRow = 3,
            modifier = Modifier
                .fillMaxWidth()
                .background(CardBackgroundDark, RoundedCornerShape(16.dp))
                .border(1.dp, GoldSand, RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            GameDatabase.PROVINCES.forEach { province ->
                val isCurrent = viewModel.currentProvinceId == province.id
                val isControlled = viewModel.controlledProvinces.contains(province.id)
                
                Box(modifier = Modifier.padding(6.dp)) {
                    HexagonNode(
                        title = province.title,
                        arabicTitle = province.arabicTitle,
                        id = province.id,
                        isSelected = isCurrent,
                        isControlled = isControlled,
                        challengeLevel = province.challengeLevel,
                        onClick = { viewModel.moveToProvince(province.id) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- ACTIVE PROVINCE PANEL ---
        val activeProvince = GameDatabase.PROVINCES.find { it.id == viewModel.currentProvinceId }!!
        Card(
            colors = CardDefaults.cardColors(containerColor = DarkIvory),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = activeProvince.title,
                            color = EmeraldDeep,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = activeProvince.arabicTitle,
                            color = AccentBronze,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif
                        )
                    }
                    
                    val isControlled = viewModel.controlledProvinces.contains(activeProvince.id)
                    Surface(
                        color = if (isControlled) EmeraldMedium else ErrorRuby,
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = if (isControlled) "CONTROLLED" else "UNCONQUERED",
                            color = Color.White,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = activeProvince.description,
                    color = TextDark,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )

                Spacer(modifier = Modifier.height(12.dp))
                Divider(color = GoldSand.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(12.dp))

                // Boss details inside province
                Text(
                    text = "Province Guardian Boss:",
                    color = AccentBronze,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${activeProvince.bossName} (${activeProvince.bossArabic})",
                    color = EmeraldDeep,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = activeProvince.bossTitle,
                    color = TextDark.copy(alpha = 0.8f),
                    fontSize = 10.sp,
                    fontStyle = FontStyle.Italic
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = activeProvince.bossQuote,
                    color = EmeraldMedium,
                    fontSize = 11.sp,
                    fontStyle = FontStyle.Italic,
                    lineHeight = 14.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Boss Challenge Button
                if (viewModel.activeBossChallenge == null) {
                    Button(
                        onClick = { viewModel.startBossFight(activeProvince) },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldDeep, contentColor = GoldMetallic),
                        modifier = Modifier.fillMaxWidth().testTag("boss_challenge_btn")
                    ) {
                        Icon(imageVector = Icons.Default.Casino, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Defeat Guardian in Linguistic Debate")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- BOSS FIGHT ARENA (CONDITIONAL) ---
        AnimatedVisibility(visible = viewModel.activeBossChallenge != null) {
            val boss = viewModel.activeBossChallenge
            if (boss != null) {
                val riddle = boss.riddles.getOrNull(viewModel.activeBossRiddleIndex)
                if (riddle != null) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = CardBackgroundDark),
                        border = BorderStroke(2.dp, GoldMetallic),
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "BOSS CHALLENGE ARENA",
                                color = GoldMetallic,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Guardian: ${boss.bossName}",
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = boss.description,
                                color = TextLight.copy(alpha = 0.8f),
                                fontSize = 11.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Divider(color = GoldSand.copy(alpha = 0.3f))
                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = "Debate Question ${viewModel.activeBossRiddleIndex + 1}/3:",
                                color = GoldMetallic,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = riddle.question,
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(vertical = 6.dp)
                            )

                            // Multiple choices
                            riddle.choices.forEach { choice ->
                                val isSelected = viewModel.bossSelectedOption == choice
                                Card(
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (isSelected) EmeraldMedium else CharcoalDark
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                        .clickable { viewModel.bossSelectedOption = choice }
                                ) {
                                    Text(
                                        text = choice,
                                        color = if (isSelected) Color.White else GoldMetallic,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        fontFamily = FontFamily.Serif,
                                        modifier = Modifier.padding(12.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            if (viewModel.bossBattleFeedback.isNotEmpty()) {
                                Text(
                                    text = viewModel.bossBattleFeedback,
                                    color = if (viewModel.bossBattleSuccess == true) SuccessMint else ErrorRuby,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 8.dp)
                                )
                            }

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Button(
                                    onClick = { viewModel.activeBossChallenge = null },
                                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = ErrorRuby)
                                ) {
                                    Text(text = "Retreat")
                                }
                                Button(
                                    onClick = { viewModel.submitBossAnswer(viewModel.bossSelectedOption) },
                                    colors = ButtonDefaults.buttonColors(containerColor = GoldMetallic, contentColor = CharcoalDark),
                                    enabled = viewModel.bossSelectedOption.isNotEmpty(),
                                    modifier = Modifier.testTag("submit_boss_ans")
                                ) {
                                    Text(text = "Submit Argument")
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- TRANSFORMATION FORGE ---
        Card(
            colors = CardDefaults.cardColors(containerColor = CardBackgroundLight),
            border = BorderStroke(1.5.dp, GoldSand),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "The Scribe's Transformation Forge",
                    color = EmeraldDeep,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Synthesize Root + Wazan cards in your hand to forge classical Arabic words and secure influence.",
                    color = AccentBronze,
                    fontSize = 11.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Hand list roots
                Text(
                    text = "Select 1 Root Card from Hand:",
                    color = TextDark,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                LazyRow(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(viewModel.campaignHandRoots) { root ->
                        val isSel = selectedRootForForge?.id == root.id
                        RootCardWidget(
                            rootCard = root,
                            onClick = { selectedRootForForge = root },
                            isSelected = isSel
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Hand list patterns
                Text(
                    text = "Select 1 Wazan Pattern from Hand:",
                    color = TextDark,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                LazyRow(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(viewModel.campaignHandPatterns) { pattern ->
                        val isSel = selectedPatternForForge?.id == pattern.id
                        PatternCardWidget(
                            patternCard = pattern,
                            onClick = { selectedPatternForForge = pattern },
                            isSelected = isSel
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Selected Info
                if (selectedRootForForge != null && selectedPatternForForge != null) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(DarkIvory, RoundedCornerShape(8.dp))
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Root: ${selectedRootForForge!!.arabic} (${selectedRootForForge!!.englishMeaning})",
                                color = EmeraldDeep,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Wazan: ${selectedPatternForForge!!.arabicWazan} (${selectedPatternForForge!!.englishRule})",
                                color = AccentBronze,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = forgeInputAnswer,
                        onValueChange = { forgeInputAnswer = it },
                        label = { Text("Write correct morphed word in Arabic") },
                        placeholder = { Text("e.g. كَاتِب") },
                        modifier = Modifier.fillMaxWidth().testTag("forge_input"),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EmeraldDeep,
                            unfocusedBorderColor = GoldSand
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (forgeResultFeedback.isNotEmpty()) {
                        Text(
                            text = forgeResultFeedback,
                            color = if (forgeSuccessResult == true) SuccessMint else ErrorRuby,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                    }

                    Button(
                        onClick = {
                            val success = viewModel.performCampaignTransformation(
                                selectedRootForForge!!,
                                selectedPatternForForge!!,
                                forgeInputAnswer
                            )
                            forgeSuccessResult = success
                            if (success) {
                                forgeResultFeedback = "🎉 Successfully forged! Scribe token deployed. Card consumed, new cards drawn."
                                selectedRootForForge = null
                                selectedPatternForForge = null
                                forgeInputAnswer = ""
                            } else {
                                forgeResultFeedback = "❌ Morphing failed. A Grammatical Anomaly broke your letters! Expected standard: ${viewModel.calculateMorphedWord(selectedRootForForge!!, selectedPatternForForge!!)}"
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldDeep, contentColor = GoldMetallic),
                        modifier = Modifier.fillMaxWidth().testTag("forge_word_btn")
                    ) {
                        Text(text = "Forge Word & Deploy Scribe")
                    }
                } else {
                    Text(
                        text = "Select one Root and one Wazan Card from above to activate the forge.",
                        color = AccentBronze,
                        fontSize = 11.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- SPECIAL EVENTS CARD (IF ACTIVE) ---
        AnimatedVisibility(visible = viewModel.activeCampaignEvent != null) {
            val ev = viewModel.activeCampaignEvent
            if (ev != null) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = AccentBronze),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.Event, contentDescription = null, tint = GoldMetallic)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Active Kingdom Event: ${ev.title}",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = ev.description,
                            color = WarmParchment,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- QUEST JOURNAL LOGS ---
        Card(
            colors = CardDefaults.cardColors(containerColor = CharcoalDark),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "The Master Scholar's Campaign Scroll",
                    color = GoldMetallic,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    viewModel.campaignLog.takeLast(4).reversed().forEach { log ->
                        Text(
                            text = "📜 $log",
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 11.sp,
                            lineHeight = 14.sp
                        )
                    }
                }
            }
        }
    }
}
