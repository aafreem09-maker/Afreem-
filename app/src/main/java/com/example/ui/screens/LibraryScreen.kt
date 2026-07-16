package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.GameLiterature
import com.example.ui.theme.*
import com.example.ui.components.*
import com.example.viewmodel.SarfGameViewModel

@Composable
fun LibraryScreen(
    viewModel: SarfGameViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

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
            titleText = "THE GRAND LIBRARY OF KUFA"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- HERO SCROLL TOP ---
        Card(
            colors = CardDefaults.cardColors(containerColor = CharcoalDark),
            border = BorderStroke(1.5.dp, GoldMetallic),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Book,
                    contentDescription = null,
                    tint = GoldMetallic,
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Linguistic Parchment Archive",
                    color = GoldMetallic,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "A world-class board game design documentation vault, detailing 20 key structural blueprints of Sarf Quest.",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Browse Scrolls & Manuscripts",
            color = EmeraldDeep,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )
        Text(
            text = "Click on any scroll to expand the authentic game-design documents.",
            color = AccentBronze,
            fontSize = 11.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Expandable manuscripts
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            ExpandableScroll(
                title = "1. Game Concept & Core Narrative",
                content = GameLiterature.INTRODUCTION
            )
            ExpandableScroll(
                title = "2. Tabletop Component List",
                content = GameLiterature.COMPONENTS_LIST
            )
            ExpandableScroll(
                title = "3. Board Layout & Hex Geometry",
                content = GameLiterature.BOARD_LAYOUT
            )
            ExpandableScroll(
                title = "4. Turn Structure & Phase Guide",
                content = GameLiterature.RULES_AND_TURNS
            )
            ExpandableScroll(
                title = "5. Win Conditions & Victory Scoring",
                content = GameLiterature.WIN_CONDITIONS
            )
            ExpandableScroll(
                title = "6. Scholar Character Profiles & Passives",
                content = GameLiterature.CHARACTER_ABILITIES_MANUAL
            )
            ExpandableScroll(
                title = "7. Print-and-Play (PnP) Blueprint Specs",
                content = GameLiterature.PRINT_PLAY
            )
            ExpandableScroll(
                title = "8. Solo, Co-Op & Campaign Mechanics",
                content = GameLiterature.CAMPAIGN_MODE
            )
            ExpandableScroll(
                title = "9. Competitive Tournament Rules",
                content = GameLiterature.TOURNEY_RULES
            )
            ExpandableScroll(
                title = "10. Teacher Guide & Lesson Plans",
                content = GameLiterature.TEACHER_GUIDE
            )
            ExpandableScroll(
                title = "11. Kickstarter Campaign Product Copy",
                content = GameLiterature.KICKSTARTER_MARKETING
            )
            ExpandableScroll(
                title = "12. Eco-Manufacturing Specifications",
                content = GameLiterature.MANUFACTURING
            )
            ExpandableScroll(
                title = "13. Educational Impact & Cognitive Scaffolding",
                content = GameLiterature.EDUCATIONAL_IMPACT
            )
            ExpandableScroll(
                title = "14. Mobile App Transformation Roadmap",
                content = """
                    MOBILE APP DEVELOPMENT SCHEDULE (SARF QUEST):
                    Phase 1: Digital Companion Arbitrator (Completed in this Android showcase!).
                    Phase 2: Global Matchmaking & Multi-player P2P Lobby using Room & Firebase.
                    Phase 3: Gamified AI opponents matching historical masters (Al-Farahidi, Sibawayh) with adjustable difficulty metrics.
                    Phase 4: Beautiful 3D animations showcasing Andalusian architecture when a player successfully conjugates a Derived Form X word.
                """.trimIndent()
            )
        }
    }
}

@Composable
fun ExpandableScroll(
    title: String,
    content: String
) {
    var isExpanded by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = DarkIvory),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = if (isExpanded) GoldMetallic else GoldSand.copy(alpha = 0.5f),
                shape = RoundedCornerShape(10.dp)
            )
            .clickable { isExpanded = !isExpanded }
            .testTag("scroll_${title.take(15).replace(" ", "_")}")
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    color = EmeraldDeep,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = AccentBronze
                )
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 10.dp)) {
                    Divider(color = GoldSand.copy(alpha = 0.3f))
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = content,
                        color = TextDark,
                        fontSize = 12.sp,
                        lineHeight = 16.sp,
                        textAlign = TextAlign.Justify
                    )
                }
            }
        }
    }
}
