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
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.WorkspacePremium
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
import com.example.model.GameDatabase
import com.example.model.PatternCard
import com.example.model.RootCard
import com.example.model.RootType
import com.example.ui.components.*
import com.example.ui.theme.*
import com.example.viewmodel.SarfGameViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun WorkshopScreen(
    viewModel: SarfGameViewModel,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    // Rapid Quiz State
    var activeQuizQuestion by remember { mutableStateOf<com.example.model.ChallengeCard?>(null) }
    var selectedQuizAnswer by remember { mutableStateOf("") }
    var quizFeedback by remember { mutableStateOf("") }
    var quizStreak by remember { mutableStateOf(0) }
    var isQuizAnswerCorrect by remember { mutableStateOf<Boolean?>(null) }

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
            titleText = "SARF WORKSHOP"
        )

        Spacer(modifier = Modifier.height(16.dp))

        // --- SUBTITLE INTRO ---
        Card(
            colors = CardDefaults.cardColors(containerColor = EmeraldDeep),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Default.MenuBook, contentDescription = null, tint = GoldMetallic)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Linguistic Synthesis & Quiz Engine",
                        color = GoldMetallic,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "A full morphological sandbox. Select root letters and patterns to observe vowel shifts, or launch the Rapid Blitz Quiz below to test your mastery.",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 11.sp,
                    lineHeight = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- SECTION 1: SARF BLITZ QUIZ MODE ---
        Card(
            colors = CardDefaults.cardColors(containerColor = CardBackgroundDark),
            border = BorderStroke(1.5.dp, GoldMetallic),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(imageVector = Icons.Default.Quiz, contentDescription = null, tint = GoldMetallic)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Rapid Sarf Blitz Quiz",
                            color = GoldMetallic,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    
                    if (quizStreak > 0) {
                        Surface(
                            color = SuccessMint,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.height(24.dp)
                        ) {
                            Text(
                                text = "STREAK: $quizStreak 🔥",
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                if (activeQuizQuestion == null) {
                    Text(
                        text = "Ready to prove your grammar expertise? Take the high-stakes Sarf Quiz. Strengthens identification of hollow, defective, and derived forms.",
                        color = TextLight.copy(alpha = 0.8f),
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            activeQuizQuestion = GameDatabase.CHALLENGE_CARDS.random()
                            selectedQuizAnswer = ""
                            quizFeedback = ""
                            isQuizAnswerCorrect = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldMetallic, contentColor = CharcoalDark),
                        modifier = Modifier.fillMaxWidth().testTag("start_quiz_btn")
                    ) {
                        Text(text = "Launch Sarf Blitz Quiz")
                    }
                } else {
                    val q = activeQuizQuestion!!
                    Text(
                        text = q.title,
                        color = GoldMetallic,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = q.question,
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Multi choice rows
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        q.choices.forEach { choice ->
                            val isSel = selectedQuizAnswer == choice
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSel) EmeraldMedium else CharcoalDark
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .minimumInteractiveComponentSize()
                                    .border(0.5.dp, GoldSand.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                    .clickable { selectedQuizAnswer = choice }
                            ) {
                                Text(
                                    text = choice,
                                    color = if (isSel) Color.White else GoldMetallic,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Serif,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (quizFeedback.isNotEmpty()) {
                        Text(
                            text = quizFeedback,
                            color = if (isQuizAnswerCorrect == true) SuccessMint else ErrorRuby,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { activeQuizQuestion = null },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = ErrorRuby)
                        ) {
                            Text(text = "End Quiz")
                        }

                        if (isQuizAnswerCorrect == null) {
                            Button(
                                onClick = {
                                    val correct = selectedQuizAnswer == q.correctMorphed
                                    isQuizAnswerCorrect = correct
                                    if (correct) {
                                        quizStreak++
                                        quizFeedback = "🏆 CORRECT! ${q.explanation}"
                                    } else {
                                        quizStreak = 0
                                        quizFeedback = "❌ WRONG! The correct Classical spelling is '${q.correctMorphed}'. Explanation: ${q.explanation}"
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = GoldMetallic, contentColor = CharcoalDark),
                                enabled = selectedQuizAnswer.isNotEmpty(),
                                modifier = Modifier.testTag("submit_quiz_ans")
                            ) {
                                Text(text = "Confirm Answer")
                            }
                        } else {
                            Button(
                                onClick = {
                                    // pick a new random question
                                    var nextQ = GameDatabase.CHALLENGE_CARDS.random()
                                    while (nextQ.id == q.id) {
                                        nextQ = GameDatabase.CHALLENGE_CARDS.random()
                                    }
                                    activeQuizQuestion = nextQ
                                    selectedQuizAnswer = ""
                                    quizFeedback = ""
                                    isQuizAnswerCorrect = null
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = GoldMetallic, contentColor = CharcoalDark),
                                modifier = Modifier.testTag("next_quiz_btn")
                            ) {
                                Text(text = "Next Question")
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // --- SECTION 2: MORPHOLOGICAL SANDBOX SOLVER ---
        Text(
            text = "Morphological Sandbox Synthesis",
            color = EmeraldDeep,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )
        Text(
            text = "Select any base Root card and a Wazan to observe real-time visual spelling and phonetic contractions.",
            color = AccentBronze,
            fontSize = 11.sp,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Roots List
        Text(
            text = "1. Choose Root Letters:",
            color = TextDark,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(GameDatabase.ROOT_CARDS) { r ->
                val isSel = viewModel.workshopSelectedRoot?.id == r.id
                RootCardWidget(
                    rootCard = r,
                    onClick = { viewModel.workshopSelectedRoot = r },
                    isSelected = isSel
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Patterns List
        Text(
            text = "2. Choose Wazan Pattern (Form I - X):",
            color = TextDark,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start
        )
        LazyRow(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(GameDatabase.PATTERN_CARDS) { p ->
                val isSel = viewModel.workshopSelectedPattern?.id == p.id
                PatternCardWidget(
                    patternCard = p,
                    onClick = { viewModel.workshopSelectedPattern = p },
                    isSelected = isSel
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // --- SYNTHESIS WORKSHOP RESULT MAT ---
        val root = viewModel.workshopSelectedRoot
        val pattern = viewModel.workshopSelectedPattern

        if (root != null && pattern != null) {
            val correctSpelling = viewModel.calculateMorphedWord(root, pattern)
            
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkIvory),
                border = BorderStroke(2.dp, GoldMetallic),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "THE GRAND SYNTHESIS OUTPUT",
                        color = AccentBronze,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))

                    // Big beautiful Arabic output block
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(WarmParchment, RoundedCornerShape(10.dp))
                            .border(1.dp, GoldSand.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                            .padding(20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = correctSpelling,
                                color = EmeraldDeep,
                                fontSize = 42.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = FontFamily.Serif,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Phonetic Output: $correctSpelling",
                                color = AccentBronze,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Morphed Meaning & Paradigm:",
                                color = EmeraldDeep,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Form ${pattern.form} ${pattern.category.name.replace("_", " ")}",
                                color = TextDark,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Formulaic Base: ${pattern.arabicWazan}",
                                color = TextDark.copy(alpha = 0.8f),
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Divider(color = GoldSand.copy(alpha = 0.4f))
                    Spacer(modifier = Modifier.height(12.dp))

                    // Irregular weak verb explanations
                    Text(
                        text = "Phonetic / Morphological Rules Applied:",
                        color = AccentBronze,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Start
                    )
                    
                    val explanation = when {
                        root.type == RootType.HOLLOW && pattern.category == com.example.model.PatternCategory.ISM_FAIL -> {
                            "Hollow Verb Active Participle Rule: The middle weak letter '${root.baseLetters[1]}' collapses into a hamzah on a ya bearer (ئ) to prevent consecutive vowel sliding. (e.g. قول -> قائل)."
                        }
                        root.type == RootType.HOLLOW && pattern.category == com.example.model.PatternCategory.ISM_MAFOOL -> {
                            "Hollow Verb Passive Participle Rule: One of the 'waws' in the standard مَفْعُول template is deleted for vocalic contraction. (e.g. قول -> مقول, بيع -> مبيع)."
                        }
                        root.type == RootType.ASSIMILATED && pattern.arabicWazan == "مِفْعَال" -> {
                            "Assimilated Instrument Rule: The initial weak letter 'waw' (و) in '${root.baseLetters}' stretches into a high-vowel 'ya' (ي) when preceding a long 'alif', ensuring phonetic fluidity. (e.g. وعد -> ميعاد)."
                        }
                        root.type == RootType.DEFECTIVE && pattern.category == com.example.model.PatternCategory.ISM_FAIL -> {
                            "Defective Active Participle (اسم منقوص): In Form I, the final weak letter drops in the nominative indefinite case, ending with a double kasrah (tanween al-kadr). (e.g. رمي -> رامٍ)."
                        }
                        root.type == RootType.DEFECTIVE && pattern.category == com.example.model.PatternCategory.ISM_MAFOOL -> {
                            "Defective Passive Participle Rule: The final weak letter merges with the template waw, causing a shaddah accentuation (e.g. رمي -> مرميّ, دعو -> مدعوّ)."
                        }
                        else -> {
                            "Regular Solid Sarf Rule: The root consonants [${root.arabic}] are seamlessly injected into the vocalic slots of the template '${pattern.arabicWazan}', preserving vowel weights exactly without contraction."
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.Top,
                        modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.WorkspacePremium,
                            contentDescription = null,
                            tint = GoldMetallic,
                            modifier = Modifier.size(16.dp).padding(top = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = explanation,
                            color = TextDark,
                            fontSize = 11.sp,
                            lineHeight = 15.sp,
                            textAlign = TextAlign.Start
                        )
                    }
                }
            }
        }
    }
}
