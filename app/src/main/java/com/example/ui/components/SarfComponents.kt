package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PatternCard
import com.example.model.RootCard
import com.example.model.RootType
import com.example.ui.theme.*
import com.example.viewmodel.SarfGameViewModel
import kotlin.math.cos
import kotlin.math.sin

// --- PREMIUM SHAPE: THE HEXAGON ---
val HexagonShape = GenericShape { size, _ ->
    val width = size.width
    val height = size.height
    val radius = minOf(width, height) / 2
    val centerX = width / 2
    val centerY = height / 2
    
    moveTo(centerX + radius * cos(0f), centerY + radius * sin(0f))
    for (i in 1..5) {
        val angle = i * (2 * Math.PI / 6).toFloat()
        lineTo(centerX + radius * cos(angle), centerY + radius * sin(angle))
    }
    close()
}

@Composable
fun HexagonNode(
    title: String,
    arabicTitle: String,
    id: Int,
    isSelected: Boolean,
    isControlled: Boolean,
    challengeLevel: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(targetValue = if (isSelected) 1.05f else 1f, label = "hexScale")
    
    Box(
        modifier = modifier
            .size(110.dp)
            .shadow(elevation = if (isSelected) 8.dp else 4.dp, shape = HexagonShape)
            .clip(HexagonShape)
            .background(
                brush = Brush.verticalGradient(
                    colors = if (isSelected) {
                        listOf(GoldMetallic, GoldSand)
                    } else if (isControlled) {
                        listOf(EmeraldDeep, EmeraldMedium)
                    } else {
                        listOf(CardBackgroundDark, CharcoalDark)
                    }
                )
            )
            .clickable(onClick = onClick)
            .testTag("hex_node_$id"),
        contentAlignment = Alignment.Center
    ) {
        // Double gold border canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val path = Path().apply {
                val radius = size.minDimension / 2 - 4.dp.toPx()
                val centerX = size.width / 2
                val centerY = size.height / 2
                moveTo(centerX + radius * cos(0f), centerY + radius * sin(0f))
                for (i in 1..5) {
                    val angle = i * (2 * Math.PI / 6).toFloat()
                    lineTo(centerX + radius * cos(angle), centerY + radius * sin(angle))
                }
                close()
            }
            drawPath(
                path = path,
                color = if (isSelected) EmeraldDeep else GoldMetallic,
                style = Stroke(width = 2.dp.toPx())
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = arabicTitle,
                color = if (isSelected) EmeraldDeep else GoldMetallic,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Form $id",
                color = if (isSelected) EmeraldDeep else TextLight.copy(alpha = 0.8f),
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(1.dp))
            Text(
                text = title.substringAfter(": "),
                color = if (isSelected) EmeraldDeep else TextLight.copy(alpha = 0.6f),
                fontSize = 10.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
            
            if (isControlled) {
                Spacer(modifier = Modifier.height(4.dp))
                Surface(
                    color = if (isSelected) EmeraldDeep else GoldMetallic,
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.height(14.dp)
                ) {
                    Text(
                        text = "SCRIBE",
                        color = if (isSelected) GoldMetallic else EmeraldDeep,
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                    )
                }
            } else {
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    repeat(challengeLevel) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = "Difficulty",
                            tint = if (isSelected) EmeraldDeep else GoldSand,
                            modifier = Modifier.size(8.dp)
                        )
                    }
                }
            }
        }
    }
}

// --- CLASSIC PARCHMENT CARD ---
@Composable
fun SarfParchmentCard(
    modifier: Modifier = Modifier,
    backgroundColor: Color = DarkIvory,
    borderColor: Color = GoldSand,
    headerColor: Color = EmeraldDeep,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = modifier
            .border(
                width = 2.dp,
                color = borderColor,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(4.dp)
            .border(
                width = 1.dp,
                color = borderColor.copy(alpha = 0.5f),
                shape = RoundedCornerShape(8.dp)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        listOf(backgroundColor, backgroundColor.copy(alpha = 0.9f))
                    )
                )
                .padding(12.dp)
        ) {
            content()
        }
    }
}

// --- ROOT CARD RENDERER ---
@Composable
fun RootCardWidget(
    rootCard: RootCard,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false
) {
    val borderCol = if (isSelected) EmeraldDeep else GoldSand
    val borderThick = if (isSelected) 3.dp else 1.5.dp
    
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = WarmParchment),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 8.dp else 3.dp),
        modifier = modifier
            .width(115.dp)
            .height(165.dp)
            .border(width = borderThick, color = borderCol, shape = RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header Type
            Surface(
                color = when (rootCard.type) {
                    RootType.REGULAR -> EmeraldMedium
                    RootType.ASSIMILATED -> WaterBlue
                    RootType.HOLLOW -> WeakVerbOrange
                    RootType.DEFECTIVE -> AccentBronze
                    RootType.HAMZATED -> ErrorRuby
                },
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = when (rootCard.type) {
                        RootType.REGULAR -> "REGULAR"
                        RootType.ASSIMILATED -> "ASSIMILATED"
                        RootType.HOLLOW -> "HOLLOW"
                        RootType.DEFECTIVE -> "DEFECTIVE"
                        RootType.HAMZATED -> "HAMZATED"
                    },
                    color = Color.White,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Large Arabic Calligraphy Root
            Text(
                text = rootCard.arabic,
                color = EmeraldDeep,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                textAlign = TextAlign.Center
            )

            Text(
                text = "[ ${rootCard.transliteration} ]",
                color = AccentBronze,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(1f))

            // Meaning Footer
            Divider(color = GoldSand.copy(alpha = 0.5f), thickness = 0.5.dp)
            Text(
                text = rootCard.englishMeaning,
                color = TextDark,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                maxLines = 2,
                lineHeight = 12.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            )
        }
    }
}

// --- WAZAN PATTERN CARD RENDERER ---
@Composable
fun PatternCardWidget(
    patternCard: PatternCard,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isSelected: Boolean = false
) {
    val borderCol = if (isSelected) EmeraldDeep else GoldMetallic
    val borderThick = if (isSelected) 3.dp else 1.5.dp

    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = DarkIvory),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 8.dp else 3.dp),
        modifier = modifier
            .width(115.dp)
            .height(165.dp)
            .border(width = borderThick, color = borderCol, shape = RoundedCornerShape(10.dp))
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Category Badge
            Surface(
                color = EmeraldDeep,
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = patternCard.category.name.replace("_", " "),
                    color = GoldMetallic,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    modifier = Modifier.padding(vertical = 2.dp, horizontal = 2.dp)
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            // Large Wazan in Arabic
            Text(
                text = patternCard.arabicWazan,
                color = EmeraldDeep,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Form ${patternCard.form} - ${patternCard.transliteration}",
                color = AccentBronze,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(1f))

            Divider(color = GoldSand.copy(alpha = 0.5f), thickness = 0.5.dp)
            Text(
                text = patternCard.englishRule,
                color = TextDark,
                fontSize = 9.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                lineHeight = 11.sp,
                maxLines = 3,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
            )
        }
    }
}

// --- GOLD COIN WIDGET ---
@Composable
fun GoldDinarBadge(amount: Int) {
    Surface(
        color = CharcoalDark,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, GoldMetallic),
        modifier = Modifier.height(30.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .background(GoldMetallic, RoundedCornerShape(8.dp))
                    .border(1.dp, AccentBronze, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "د",
                    color = EmeraldDeep,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "$amount Dinars",
                color = GoldMetallic,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// --- XP ACCUMULATOR BADGE ---
@Composable
fun XPBadge(xp: Int, level: Int) {
    Surface(
        color = CharcoalDark,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, EmeraldLight),
        modifier = Modifier.height(30.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "XP Icon",
                tint = EmeraldLight,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = "Lvl $level ($xp XP)",
                color = EmeraldLight,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// --- VIRTUAL DICE COMPONENT ---
@Composable
fun DiceWidget(
    value1: Int,
    value2: Int,
    sarfResult: String,
    onRollClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(CardBackgroundLight, RoundedCornerShape(12.dp))
            .border(1.dp, GoldSand, RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Text(
            text = "Caravan & Sarf Dice",
            color = EmeraldDeep,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Dice 1
            Box(
                modifier = Modifier
                    .size(45.dp)
                    .background(DarkIvory, RoundedCornerShape(8.dp))
                    .border(1.5.dp, GoldSand, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$value1",
                    color = EmeraldDeep,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            // Dice 2
            Box(
                modifier = Modifier
                    .size(45.dp)
                    .background(DarkIvory, RoundedCornerShape(8.dp))
                    .border(1.5.dp, GoldSand, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$value2",
                    color = EmeraldDeep,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            
            // Sarf Polyhedral Die
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .shadow(elevation = 3.dp, shape = HexagonShape)
                    .clip(HexagonShape)
                    .background(EmeraldMedium)
                    .border(1.dp, GoldMetallic, HexagonShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "SARF",
                    color = GoldMetallic,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Sarf Outcome: $sarfResult",
            color = AccentBronze,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = onRollClick,
            colors = ButtonDefaults.buttonColors(containerColor = EmeraldDeep, contentColor = GoldMetallic),
            modifier = Modifier.testTag("roll_dice_btn")
        ) {
            Text(text = "Roll Dice (إلقاء النرد)")
        }
    }
}

@Composable
fun HighDensityPlayerHeader(
    viewModel: SarfGameViewModel,
    onBackClick: (() -> Unit)? = null,
    onResetClick: (() -> Unit)? = null,
    titleText: String? = null
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(8.dp, RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
            .background(
                brush = Brush.verticalGradient(
                    listOf(EmeraldDeep, EmeraldDeep.copy(alpha = 0.95f))
                ),
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
            )
            .border(
                width = 1.dp,
                color = GoldMetallic.copy(alpha = 0.4f),
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp)
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
                    if (onBackClick != null) {
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Back",
                                tint = Color.White
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .background(Color(0xFFD1FAE5), RoundedCornerShape(21.dp))
                            .border(2.dp, GoldMetallic, RoundedCornerShape(21.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = viewModel.selectedCharacter.name.take(1),
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
                                text = viewModel.selectedCharacter.name,
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Surface(
                                color = GoldMetallic.copy(alpha = 0.2f),
                                border = BorderStroke(1.dp, GoldMetallic.copy(alpha = 0.4f)),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = "Lvl ${viewModel.playerLevel}",
                                    color = Color(0xFFFCD34D),
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = viewModel.getRankName(viewModel.playerLevel),
                            color = Color(0xFFD1FAE5),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = FontFamily.Serif
                        )
                    }
                }

                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        if (onResetClick != null) {
                            IconButton(
                                onClick = onResetClick,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Reset",
                                    tint = Color.White.copy(alpha = 0.7f)
                                )
                            }
                        }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .background(EmeraldMedium, RoundedCornerShape(6.dp))
                                .padding(horizontal = 6.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "🪙 ${viewModel.playerGold}d",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    val xpProgress = (viewModel.playerXP % 100).toFloat() / 100f
                    Box(
                        modifier = Modifier
                            .width(80.dp)
                            .height(6.dp)
                            .background(Color(0xFF042F1A), RoundedCornerShape(3.dp))
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth(fraction = xpProgress.coerceIn(0.1f, 1f))
                                .background(GoldMetallic, RoundedCornerShape(3.dp))
                        )
                    }
                }
            }

            if (titleText != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = titleText,
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.Serif,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
