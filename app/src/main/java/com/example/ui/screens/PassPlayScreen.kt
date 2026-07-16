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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PatternCard
import com.example.model.RootCard
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.SarfGameViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PassPlayScreen(
    viewModel: SarfGameViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    // Internal selection state for the active player's turn
    var activePlayerSelectedRoot by remember { mutableStateOf<RootCard?>(null) }
    var activePlayerSelectedPattern by remember { mutableStateOf<PatternCard?>(null) }
    var localForgeInput by remember { mutableStateOf("") }
    var localForgeFeedback by remember { mutableStateOf("") }
    var localForgeSuccess by remember { mutableStateOf<Boolean?>(null) }

    val activePlayer = viewModel.passPlayPlayers.getOrNull(viewModel.passPlayActiveIndex)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WarmParchment)
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // --- TOP NAVIGATION BACK BAR & STATUS ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(8.dp, RoundedCornerShape(24.dp))
                .background(
                    brush = Brush.verticalGradient(
                        listOf(EmeraldDeep, EmeraldDeep.copy(alpha = 0.95f))
                    ),
                    shape = RoundedCornerShape(24.dp)
                )
                .border(
                    width = 1.dp,
                    color = GoldMetallic.copy(alpha = 0.4f),
                    shape = RoundedCornerShape(24.dp)
                )
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        IconButton(
                            onClick = { viewModel.currentScreen = com.example.viewmodel.SarfScreen.Home },
                            modifier = Modifier.size(36.dp).testTag("passplay_back_btn")
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }

                        if (activePlayer != null) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .background(Color(0xFFD1FAE5), RoundedCornerShape(21.dp))
                                    .border(2.dp, GoldMetallic, RoundedCornerShape(21.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = activePlayer.character.name.take(1),
                                    color = EmeraldDeep,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = "${activePlayer.name} (${activePlayer.character.name})",
                                        color = Color.White,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Surface(
                                        color = GoldMetallic.copy(alpha = 0.2f),
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = "Lvl ${activePlayer.level}",
                                            color = Color(0xFFFCD34D),
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = "Current Scholar on Turn",
                                    color = Color(0xFFD1FAE5),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Normal
                                )
                            }
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        IconButton(
                            onClick = { viewModel.resetPassPlay() },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Reset Game",
                                tint = Color.White.copy(alpha = 0.7f)
                            )
                        }
                        if (activePlayer != null) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .background(EmeraldMedium, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 6.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "🪙 ${activePlayer.gold}d",
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "PASS & PLAY MULTIPLAYER",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Serif,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- SCOREBOARD ---
        Card(
            colors = CardDefaults.cardColors(containerColor = CharcoalDark),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(bottom = 6.dp)
                ) {
                    Icon(imageVector = Icons.Default.Groups, contentDescription = null, tint = GoldMetallic)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Scholar Standings / Scoreboard",
                        color = GoldMetallic,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                viewModel.passPlayPlayers.forEach { pl ->
                    val isCurrent = activePlayer?.id == pl.id
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isCurrent) EmeraldDeep else Color.Transparent)
                            .padding(6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${pl.name} (Lvl ${pl.level})",
                            color = if (isCurrent) GoldMetallic else Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Text(
                                text = "Din: ${pl.gold}d",
                                color = if (isCurrent) Color.White else GoldSand,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Score: ${pl.score}",
                                color = if (isCurrent) Color.White else EmeraldLight,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // --- TURN CARD PANEL ---
        if (activePlayer != null) {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkIvory),
                border = BorderStroke(1.5.dp, GoldMetallic),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "ACTIVE SCHOLAR TURN:",
                        color = AccentBronze,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = activePlayer.name,
                        color = EmeraldDeep,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = "Character Specialization: ${activePlayer.character.specialty}",
                        color = TextDark.copy(alpha = 0.8f),
                        fontSize = 11.sp,
                        fontStyle = FontStyle.Italic
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Divider(color = GoldSand.copy(alpha = 0.4f))
                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "TURN PHASE: ${viewModel.passPlayTurnPhase}",
                        color = EmeraldDeep,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // --- PHASE 1: DRAW DECK PANEL ---
                    if (viewModel.passPlayTurnPhase == "Resource Draw") {
                        Text(
                            text = "Draw cards from the library to expand your hand before taking action.",
                            color = TextDark,
                            fontSize = 11.sp
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Button(
                                onClick = { viewModel.passPlayDrawRoot() },
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldDeep, contentColor = GoldMetallic),
                                modifier = Modifier.weight(1f).testTag("pp_draw_root")
                            ) {
                                Text(text = "Draw Root Card")
                            }
                            Button(
                                onClick = { viewModel.passPlayDrawPattern() },
                                colors = ButtonDefaults.buttonColors(containerColor = AccentBronze, contentColor = Color.White),
                                modifier = Modifier.weight(1f).testTag("pp_draw_pattern")
                            ) {
                                Text(text = "Draw Wazan Card")
                            }
                        }
                    }

                    // --- PHASE 2: FORGE ACTION PANEL ---
                    if (viewModel.passPlayTurnPhase == "Action") {
                        Text(
                            text = "Conjugate/forge roots and patterns from your hand for massive scores.",
                            color = TextDark,
                            fontSize = 11.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        // Active player roots
                        Text(
                            text = "Choose a Root from your hand:",
                            color = TextDark,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        LazyRow(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(activePlayer.handRoots) { root ->
                                val isSelected = activePlayerSelectedRoot?.id == root.id
                                RootCardWidget(
                                    rootCard = root,
                                    onClick = { activePlayerSelectedRoot = root },
                                    isSelected = isSelected
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Active player patterns
                        Text(
                            text = "Choose a Wazan from your hand:",
                            color = TextDark,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                        LazyRow(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(activePlayer.handPatterns) { pattern ->
                                val isSelected = activePlayerSelectedPattern?.id == pattern.id
                                PatternCardWidget(
                                    patternCard = pattern,
                                    onClick = { activePlayerSelectedPattern = pattern },
                                    isSelected = isSelected
                                )
                            }
                        }

                        // Input Transformation spelling
                        if (activePlayerSelectedRoot != null && activePlayerSelectedPattern != null) {
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(WarmParchment, RoundedCornerShape(8.dp))
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Root Selected: ${activePlayerSelectedRoot!!.arabic}",
                                        color = EmeraldDeep,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "Wazan Selected: ${activePlayerSelectedPattern!!.arabicWazan}",
                                        color = AccentBronze,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            OutlinedTextField(
                                value = localForgeInput,
                                onValueChange = { localForgeInput = it },
                                label = { Text("Morphed Classical Form (Arabic)") },
                                placeholder = { Text("e.g. مَكْتُوب") },
                                modifier = Modifier.fillMaxWidth().testTag("pp_forge_input"),
                                singleLine = true,
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = EmeraldDeep,
                                    unfocusedBorderColor = GoldSand
                                )
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            if (localForgeFeedback.isNotEmpty()) {
                                Text(
                                    text = localForgeFeedback,
                                    color = if (localForgeSuccess == true) SuccessMint else ErrorRuby,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 6.dp)
                                )
                            }

                            Button(
                                onClick = {
                                    val success = viewModel.passPlayExecuteTransformation(
                                        activePlayerSelectedRoot!!,
                                        activePlayerSelectedPattern!!,
                                        localForgeInput
                                    )
                                    localForgeSuccess = success
                                    if (success) {
                                        localForgeFeedback = "🎉 Outstanding! Correct classical form morphed. Resources consumed."
                                        activePlayerSelectedRoot = null
                                        activePlayerSelectedPattern = null
                                        localForgeInput = ""
                                    } else {
                                        val correctForm = viewModel.calculateMorphedWord(activePlayerSelectedRoot!!, activePlayerSelectedPattern!!)
                                        localForgeFeedback = "❌ Spelled incorrectly! Classical Standard: $correctForm. Penalty incurred."
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldDeep, contentColor = GoldMetallic),
                                modifier = Modifier.fillMaxWidth().testTag("pp_forge_word_btn")
                            ) {
                                Text(text = "Forge & End Action")
                            }
                        }
                    }

                    // --- PHASE 3: END TURN PANEL ---
                    if (viewModel.passPlayTurnPhase == "End") {
                        Text(
                            text = "Turn Actions complete! Click pass turn below and hand the screen to the next Scholar.",
                            color = EmeraldDeep,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(
                            onClick = {
                                viewModel.passPlayEndTurn()
                                localForgeFeedback = ""
                                localForgeSuccess = null
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = GoldMetallic, contentColor = CharcoalDark),
                            modifier = Modifier.fillMaxWidth().testTag("pp_pass_btn")
                        ) {
                            Text(text = "Pass Turn to Next Player")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- LOCAL MULTIPLAYER EVENT BOARD LOG ---
        Card(
            colors = CardDefaults.cardColors(containerColor = CharcoalDark),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = "Ancient Diwan - Game Log",
                    color = GoldMetallic,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    viewModel.passPlayStatusLog.takeLast(4).reversed().forEach { log ->
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
