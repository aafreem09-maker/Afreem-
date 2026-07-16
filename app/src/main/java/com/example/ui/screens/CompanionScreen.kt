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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GameDatabase
import com.example.model.PatternCard
import com.example.model.RootCard
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.SarfGameViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CompanionScreen(
    viewModel: SarfGameViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    // Arbitrator selection state
    var arbiterRoot by remember { mutableStateOf<RootCard?>(null) }
    var arbiterPattern by remember { mutableStateOf<PatternCard?>(null) }
    var userArbitrationSpelling by remember { mutableStateOf("") }
    var arbitrationFeedback by remember { mutableStateOf("") }
    var arbitrationVerdict by remember { mutableStateOf<Boolean?>(null) }

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
            titleText = "TABLETOP COMPANION"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- SUBTITLE INTRO ---
        Card(
            colors = CardDefaults.cardColors(containerColor = EmeraldMedium),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.Casino, contentDescription = null, tint = GoldMetallic)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Physical Board Game Assistant",
                        color = GoldMetallic,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Playing the physical tabletop version of Sarf Quest? Use this tool as your digital arbiter, dice roller, and randomized deck dealer.",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 11.sp,
                    lineHeight = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- 1. DICES PANEL ---
        DiceWidget(
            value1 = viewModel.companionDiceResult1,
            value2 = viewModel.companionDiceResult2,
            sarfResult = viewModel.companionSarfDieResult,
            onRollClick = { viewModel.rollCompanionDice() }
        )

        Spacer(modifier = Modifier.height(20.dp))

        // --- 2. CARD PILES SIMULATOR ---
        Card(
            colors = CardDefaults.cardColors(containerColor = CardBackgroundLight),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Draw Deck Simulators:",
                    color = EmeraldDeep,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Lost cards? Run out of decks? Draw cards electronically from our master list.",
                    color = AccentBronze,
                    fontSize = 11.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Button(
                        onClick = { viewModel.drawRootCompanion() },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldDeep, contentColor = GoldMetallic),
                        modifier = Modifier.weight(1f).testTag("comp_draw_root")
                    ) {
                        Text(text = "Draw Root Card", fontSize = 10.sp)
                    }
                    Button(
                        onClick = { viewModel.drawPatternCompanion() },
                        colors = ButtonDefaults.buttonColors(containerColor = AccentBronze, contentColor = Color.White),
                        modifier = Modifier.weight(1f).testTag("comp_draw_wazan")
                    ) {
                        Text(text = "Draw Wazan", fontSize = 10.sp)
                    }
                    Button(
                        onClick = { viewModel.drawEventCompanion() },
                        colors = ButtonDefaults.buttonColors(containerColor = CharcoalDark, contentColor = GoldMetallic),
                        modifier = Modifier.weight(1f).testTag("comp_draw_event")
                    ) {
                        Text(text = "Draw Event", fontSize = 10.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Card outputs
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    if (viewModel.companionLastRootDrawn != null) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(6.dp)
                        ) {
                            Text(text = "Root Drawn:", color = EmeraldDeep, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            RootCardWidget(rootCard = viewModel.companionLastRootDrawn!!, onClick = {})
                        }
                    }
                    if (viewModel.companionLastPatternDrawn != null) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(6.dp)
                        ) {
                            Text(text = "Wazan Drawn:", color = EmeraldDeep, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            PatternCardWidget(patternCard = viewModel.companionLastPatternDrawn!!, onClick = {})
                        }
                    }
                }

                AnimatedVisibility(visible = viewModel.companionLastEventDrawn != null) {
                    val ev = viewModel.companionLastEventDrawn
                    if (ev != null) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = AccentBronze),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth().padding(top = 10.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "EVENT SCROLL: ${ev.title}",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = ev.description,
                                    color = WarmParchment,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- 3. SARF ARBITRATOR (DISPUTE SETTLER) ---
        Card(
            colors = CardDefaults.cardColors(containerColor = DarkIvory),
            border = BorderStroke(1.5.dp, GoldSand),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "The Sarf Arbiter (Spelling Disputes)",
                    color = EmeraldDeep,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Did another player forge a word incorrectly? Settle standard classical spelling disputes instantly.",
                    color = AccentBronze,
                    fontSize = 11.sp
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Small quick selectors
                Text(text = "Select Root:", color = TextDark, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                LazyRow(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(GameDatabase.ROOT_CARDS) { r ->
                        val isSel = arbiterRoot?.id == r.id
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSel) EmeraldDeep else WarmParchment
                            ),
                            modifier = Modifier.clickable { arbiterRoot = r }
                        ) {
                            Text(
                                text = r.arabic,
                                color = if (isSel) Color.White else EmeraldDeep,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(text = "Select Wazan Pattern:", color = TextDark, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                LazyRow(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(GameDatabase.PATTERN_CARDS) { p ->
                        val isSel = arbiterPattern?.id == p.id
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = if (isSel) EmeraldDeep else WarmParchment
                            ),
                            modifier = Modifier.clickable { arbiterPattern = p }
                        ) {
                            Text(
                                text = p.arabicWazan,
                                color = if (isSel) Color.White else EmeraldDeep,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif
                            )
                        }
                    }
                }

                if (arbiterRoot != null && arbiterPattern != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = userArbitrationSpelling,
                        onValueChange = { userArbitrationSpelling = it },
                        label = { Text("Enter Player's Spelled Answer") },
                        modifier = Modifier.fillMaxWidth().testTag("arbiter_input"),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EmeraldDeep,
                            unfocusedBorderColor = GoldSand
                        )
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    if (arbitrationFeedback.isNotEmpty()) {
                        Text(
                            text = arbitrationFeedback,
                            color = if (arbitrationVerdict == true) SuccessMint else ErrorRuby,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                    }

                    Button(
                        onClick = {
                            val expectedWord = viewModel.calculateMorphedWord(arbiterRoot!!, arbiterPattern!!)
                            val isCorrect = userArbitrationSpelling.trim() == expectedWord
                            arbitrationVerdict = isCorrect
                            if (isCorrect) {
                                arbitrationFeedback = "🏆 LEGITIMATE! '${userArbitrationSpelling.trim()}' is a correct classical morph of ${arbiterRoot!!.arabic} into ${arbiterPattern!!.arabicWazan}!"
                            } else {
                                arbitrationFeedback = "⚠️ INVALID GRAMMAR! '${userArbitrationSpelling.trim()}' is incorrect. The perfect Classical conjugation is '$expectedWord'. Settle the dispute: Player receives penalty."
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldDeep, contentColor = GoldMetallic),
                        modifier = Modifier.fillMaxWidth().testTag("arbiter_solve_btn")
                    ) {
                        Text(text = "Execute Arbitrator Verification")
                    }
                }
            }
        }
    }
}
