package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.Character
import com.example.model.GameLiterature
import com.example.ui.theme.*
import com.example.viewmodel.SarfGameViewModel
import com.example.viewmodel.SarfScreen
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun DashboardScreen(
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
        // --- HERO HEADER ---
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(6.dp, RoundedCornerShape(16.dp))
                .clip(RoundedCornerShape(16.dp))
                .background(
                    brush = Brush.verticalGradient(
                        listOf(EmeraldDeep, EmeraldMedium)
                    )
                )
                .border(2.dp, GoldMetallic, RoundedCornerShape(16.dp))
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                // Programmatic Islamic star icon
                IslamicStarIcon(modifier = Modifier.size(50.dp))
                
                Spacer(modifier = Modifier.height(10.dp))
                
                Text(
                    text = "سَعْيُ الصَّرْفِ",
                    color = GoldMetallic,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily.Serif,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "SARF QUEST",
                    color = Color.White,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 4.sp,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Kingdom of Arabic Morphology",
                    color = GoldSand,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- CHARACTER SELECTION SECTION ---
        Text(
            text = "Select Your Master Scholar",
            color = EmeraldDeep,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )
        Text(
            text = "Each character wields a specialized passive grammar ability to alter gameplay rules.",
            color = AccentBronze,
            fontSize = 11.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )
        
        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(viewModel.characters) { char ->
                val isSelected = viewModel.selectedCharacter.id == char.id
                CharacterCard(
                    character = char,
                    isSelected = isSelected,
                    onClick = {
                        viewModel.selectedCharacter = char
                        viewModel.resetCampaign() // auto adapt campaign setup
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- SECTIONS / ACTIONS MENU ---
        Text(
            text = "Embark into the Kingdom",
            color = EmeraldDeep,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )
        Spacer(modifier = Modifier.height(12.dp))

        // Grid Menu
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                MenuButton(
                    title = "Solo Quest",
                    subtitle = "Campaign Board",
                    icon = Icons.Default.Map,
                    bgColor = EmeraldMedium,
                    textColor = Color.White,
                    onClick = { viewModel.currentScreen = SarfScreen.Campaign },
                    modifier = Modifier.weight(1f).testTag("menu_campaign")
                )
                MenuButton(
                    title = "Pass & Play",
                    subtitle = "Local Multi",
                    icon = Icons.Default.Groups,
                    bgColor = AccentBronze,
                    textColor = Color.White,
                    onClick = { viewModel.currentScreen = SarfScreen.PassPlay },
                    modifier = Modifier.weight(1f).testTag("menu_passplay")
                )
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                MenuButton(
                    title = "Sarf Workshop",
                    subtitle = "Forge & Solve",
                    icon = Icons.Default.MenuBook,
                    bgColor = WaterBlue,
                    textColor = Color.White,
                    onClick = { viewModel.currentScreen = SarfScreen.Workshop },
                    modifier = Modifier.weight(1f).testTag("menu_workshop")
                )
                MenuButton(
                    title = "Tabletop Companion",
                    subtitle = "Dice & Decks Assistant",
                    icon = Icons.Default.Casino,
                    bgColor = DarkIvory,
                    textColor = EmeraldDeep,
                    onClick = { viewModel.currentScreen = SarfScreen.Companion },
                    modifier = Modifier.weight(1f).testTag("menu_companion")
                )
            }
            MenuButton(
                title = "The Grand Library of Kufa",
                subtitle = "Rules, Teacher Guides, PnP Materials, Manufacturing & Roadmap",
                icon = Icons.Default.AccountBalance,
                bgColor = CharcoalDark,
                textColor = GoldMetallic,
                onClick = { viewModel.currentScreen = SarfScreen.Library },
                modifier = Modifier.fillMaxWidth().testTag("menu_library")
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- SHORT DESCRIPTION/VIBE CARD ---
        Card(
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = DarkIvory),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.WorkspacePremium,
                        contentDescription = "Premium Badge",
                        tint = GoldMetallic
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "The Designer's Vision",
                        color = EmeraldDeep,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = GameLiterature.INTRODUCTION,
                    color = TextDark,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }
        }
    }
}

@Composable
fun CharacterCard(
    character: Character,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) character.bannerColor else CardBackgroundLight
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 8.dp else 2.dp),
        modifier = Modifier
            .width(185.dp)
            .height(220.dp)
            .border(
                width = if (isSelected) 3.dp else 1.dp,
                color = if (isSelected) GoldMetallic else GoldSand.copy(alpha = 0.5f),
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .testTag("char_card_${character.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = character.name,
                    color = if (isSelected) Color.White else EmeraldDeep,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = character.title,
                    color = if (isSelected) GoldMetallic else AccentBronze,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = character.abilityDescription,
                    color = if (isSelected) TextLight.copy(alpha = 0.9f) else TextDark,
                    fontSize = 10.sp,
                    lineHeight = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            
            Column {
                Text(
                    text = character.quote,
                    color = if (isSelected) GoldSand else AccentBronze.copy(alpha = 0.8f),
                    fontSize = 8.sp,
                    fontStyle = FontStyle.Italic,
                    lineHeight = 10.sp,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Gold: ${character.initialGold}d",
                        color = if (isSelected) GoldMetallic else TextDark,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "XP: ${character.initialXP}",
                        color = if (isSelected) Color.White else TextDark,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun MenuButton(
    title: String,
    subtitle: String,
    icon: ImageVector,
    bgColor: Color,
    textColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = modifier
            .height(80.dp)
            .border(1.dp, GoldSand.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.White.copy(alpha = 0.2f), RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = if (textColor == Color.White) GoldMetallic else EmeraldDeep
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    text = title,
                    color = textColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = subtitle,
                    color = if (textColor == Color.White) TextLight.copy(alpha = 0.8f) else TextDark.copy(alpha = 0.8f),
                    fontSize = 10.sp,
                    lineHeight = 12.sp,
                    maxLines = 2
                )
            }
        }
    }
}

@Composable
fun IslamicStarIcon(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "starRotation")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(12000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Canvas(
        modifier = modifier
            .rotate(rotation)
    ) {
        val size = size.minDimension
        val radius = size / 2
        val cx = size / 2
        val cy = size / 2

        val path = Path()
        // Draw an 8-pointed Islamic Star (composed of two overlapping squares)
        // Square 1
        path.moveTo(cx - radius, cy)
        path.lineTo(cx, cy - radius)
        path.lineTo(cx + radius, cy)
        path.lineTo(cx, cy + radius)
        path.close()

        // Square 2 (rotated by 45 degrees)
        val offset = (radius / Math.sqrt(2.0)).toFloat()
        path.moveTo(cx - offset, cy - offset)
        path.lineTo(cx + offset, cy - offset)
        path.lineTo(cx + offset, cy + offset)
        path.lineTo(cx - offset, cy + offset)
        path.close()

        drawPath(
            path = path,
            color = GoldMetallic,
            style = Stroke(width = 3.dp.toPx())
        )
    }
}
