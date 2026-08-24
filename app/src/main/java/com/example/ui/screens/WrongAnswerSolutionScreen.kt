package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.QuestionItem
import com.example.data.repository.FullSolutionModel
import com.example.data.repository.SolutionExplanationEngine
import com.example.ui.components.SolutionVisualCanvas
import com.example.ui.theme.AlertRed
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldGlow
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.InfoCyan
import com.example.ui.theme.NavyBackground
import com.example.ui.theme.NavyBorder
import com.example.ui.theme.NavyCard
import com.example.ui.theme.NavyCardElevated
import com.example.ui.theme.NavyDeepest
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.QuizViewModel

/**
 * Full Educational Solution Mode for Wrong Answers.
 * Reconstructs user's incorrect reasoning path, classifies the fallacy/mistake,
 * presents a 5-step rigorous deductive proof, renders visual models,
 * and plays natural-paced teacher voice narration.
 */
@Composable
fun WrongAnswerSolutionScreen(
    viewModel: QuizViewModel,
    question: QuestionItem,
    selectedOptionIndex: Int,
    onContinue: () -> Unit
) {
    val profile by viewModel.userProfile.collectAsState()
    val language by viewModel.languageMode.collectAsState()
    val mode = language.uppercase()
    val isHi = mode == "HINDI" || mode == "HI"

    val uiState by viewModel.uiState.collectAsState()
    val lifelineState = (uiState as? com.example.ui.viewmodel.QuizUiState.InGame)?.lifelineState
        ?: com.example.data.model.LifelineState()

    val currentUiState = uiState
    val isTimeout = (currentUiState as? com.example.ui.viewmodel.QuizUiState.WrongAnswerSolution)?.isTimeout == true
    val highestQNumber = (currentUiState as? com.example.ui.viewmodel.QuizUiState.WrongAnswerSolution)?.highestQNumber ?: 1
    val guaranteedSecuredPoints = (currentUiState as? com.example.ui.viewmodel.QuizUiState.WrongAnswerSolution)?.guaranteedSecuredPoints ?: 0L

    val solutionModel = remember(question, selectedOptionIndex, profile) {
        SolutionExplanationEngine.generateWrongAnswerAnalysis(
            question = question,
            selectedOptionIndex = selectedOptionIndex,
            userProfile = profile,
            lifelineState = lifelineState
        )
    }

    var isVoicePlaying by remember { mutableStateOf(true) }

    // Start natural educational voice narration upon entering screen
    LaunchedEffect(solutionModel) {
        val script = if (isHi) solutionModel.teacherVoiceScriptHindi else solutionModel.teacherVoiceScriptEnglish
        viewModel.speechNarrator.speakSolutionNatural(script, language)
        isVoicePlaying = true
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.speechNarrator.stop()
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .testTag("wrong_answer_solution_screen"),
        color = NavyBackground
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 14.dp, vertical = 8.dp)
        ) {
            // Screen Header HUD
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = NavyDeepest),
                border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(AlertRed, GoldPrimary.copy(alpha = 0.5f))))
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(AlertRed.copy(alpha = 0.2f), CircleShape)
                                .border(1.dp, AlertRed, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isTimeout) Icons.Default.ErrorOutline else Icons.Default.Close,
                                contentDescription = "Status",
                                tint = AlertRed,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (isTimeout) {
                                    if (isHi) "🕒 समय समाप्त (Time Expired)" else "🕒 TIME EXPIRED"
                                } else {
                                    if (isHi) "❌ गलत उत्तर (Incorrect Answer)" else "❌ INCORRECT ANSWER"
                                },
                                style = MaterialTheme.typography.titleMedium.copy(
                                    color = AlertRed,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp
                                )
                            )
                            Text(
                                text = if (isTimeout) {
                                    if (isHi) "${profile.name}, समय समाप्त! कोई उत्तर लॉक नहीं हुआ।" else "${profile.name}, Time expired. No answer locked."
                                } else {
                                    if (isHi) "${profile.name}, आप Q$highestQNumber तक पहुँचे | Prize: ₹$guaranteedSecuredPoints" else "${profile.name}, Reached Q$highestQNumber | Prize: ₹$guaranteedSecuredPoints"
                                },
                                fontSize = 11.sp,
                                color = GoldGlow
                            )
                        }
                    }

                    // Voice Narration Toggle Button
                    IconButton(
                        onClick = {
                            if (isVoicePlaying) {
                                viewModel.speechNarrator.stop()
                                isVoicePlaying = false
                            } else {
                                val script = if (isHi) solutionModel.teacherVoiceScriptHindi else solutionModel.teacherVoiceScriptEnglish
                                viewModel.speechNarrator.speakSolutionNatural(script, language)
                                isVoicePlaying = true
                            }
                        },
                        modifier = Modifier.size(36.dp).testTag("solution_audio_toggle_button")
                    ) {
                        Icon(
                            imageVector = if (isVoicePlaying) Icons.Default.VolumeUp else Icons.Default.PlayArrow,
                            contentDescription = "Voice Solution",
                            tint = if (isVoicePlaying) GoldGlow else TextSecondary,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Scrollable Content Body
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                // 1. COMPARISON BANNER (Your Answer vs Correct Answer)
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = NavyCard)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = if (isHi) "उत्तर तुलना (Answer Comparison):" else "Answer Comparison:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextSecondary
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // User Choice (Red)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(AlertRed.copy(alpha = 0.12f), RoundedCornerShape(8.dp))
                                .border(1.dp, AlertRed.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "आपका उत्तर (Option ${solutionModel.mistakeAnalysis.chosenOptionLetter}):",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = AlertRed
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isHi) solutionModel.mistakeAnalysis.chosenOptionTextHindi else solutionModel.mistakeAnalysis.chosenOptionTextEnglish,
                                fontSize = 12.sp,
                                color = TextPrimary,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Correct Answer (Green)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(SuccessGreen.copy(alpha = 0.12f), RoundedCornerShape(8.dp))
                                .border(1.dp, SuccessGreen.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "सही उत्तर (Option ${solutionModel.mistakeAnalysis.correctOptionLetter}):",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = SuccessGreen
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isHi) solutionModel.mistakeAnalysis.correctOptionTextHindi else solutionModel.mistakeAnalysis.correctOptionTextEnglish,
                                fontSize = 12.sp,
                                color = TextPrimary,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 2. "AAPKI GALTI KAHAN HUI?" (Root-Cause & Mistake Identification)
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("mistake_identification_card"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = NavyCardElevated),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(AlertRed.copy(alpha = 0.6f), NavyBorder)))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.ErrorOutline,
                                contentDescription = "Mistake",
                                tint = AlertRed,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isHi) "🔍 आपकी गलती कहाँ हुई? (What Went Wrong?)" else "🔍 Where Did Your Reasoning Fail?",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    color = GoldGlow,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Mistake / Fallacy Tag
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .background(AlertRed.copy(alpha = 0.2f), RoundedCornerShape(6.dp))
                                    .border(1.dp, AlertRed, RoundedCornerShape(6.dp))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = if (isHi) solutionModel.mistakeAnalysis.mistakeType.titleHindi else solutionModel.mistakeAnalysis.mistakeType.titleEnglish,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = AlertRed
                                )
                            }

                            if (solutionModel.mistakeAnalysis.fallacyName != null) {
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .background(GoldPrimary.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                                        .border(1.dp, GoldPrimary.copy(alpha = 0.4f), RoundedCornerShape(6.dp))
                                        .padding(horizontal = 8.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = solutionModel.mistakeAnalysis.fallacyName,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = GoldGlow
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = if (isHi) solutionModel.mistakeAnalysis.mistakeSummaryHindi else solutionModel.mistakeAnalysis.mistakeSummaryEnglish,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = TextPrimary,
                                lineHeight = 20.sp
                            )
                        )

                        // Lifeline Context Notice (if applicable)
                        if (solutionModel.mistakeAnalysis.lifelineContextHindi != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(NavyDeepest, RoundedCornerShape(6.dp))
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.HelpOutline, contentDescription = null, tint = InfoCyan, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isHi) solutionModel.mistakeAnalysis.lifelineContextHindi else (solutionModel.mistakeAnalysis.lifelineContextEnglish ?: ""),
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // 3. VISUAL REASONING CANVAS
                SolutionVisualCanvas(
                    diagramType = solutionModel.visualDiagramType,
                    category = question.category,
                    chosenLetter = solutionModel.mistakeAnalysis.chosenOptionLetter,
                    correctLetter = solutionModel.mistakeAnalysis.correctOptionLetter,
                    isHi = isHi
                )

                Spacer(modifier = Modifier.height(10.dp))

                // 4. FULL STEP-BY-STEP DEDUCTIVE SOLUTION
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("full_step_by_step_solution_card"),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = NavyCardElevated),
                    border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(GoldPrimary.copy(alpha = 0.5f), NavyBorder)))
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.School,
                                contentDescription = "Step by Step",
                                tint = GoldPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isHi) "🧠 पूरा तार्किक समाधान (Full Step-by-Step Solution)" else "🧠 Step-by-Step Deductive Solution",
                                style = MaterialTheme.typography.titleSmall.copy(
                                    color = GoldGlow,
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Steps List
                        solutionModel.steps.forEach { step ->
                            val isFailPoint = step.isUserFailurePoint
                            val isValidation = step.isValidationStep

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = when {
                                        isFailPoint -> AlertRed.copy(alpha = 0.10f)
                                        isValidation -> SuccessGreen.copy(alpha = 0.12f)
                                        else -> NavyDeepest
                                    }
                                ),
                                border = CardDefaults.outlinedCardBorder().copy(
                                    brush = Brush.horizontalGradient(
                                        listOf(
                                            when {
                                                isFailPoint -> AlertRed.copy(alpha = 0.7f)
                                                isValidation -> SuccessGreen.copy(alpha = 0.7f)
                                                else -> NavyBorder
                                            },
                                            NavyBorder
                                        )
                                    )
                                )
                            ) {
                                Column(modifier = Modifier.padding(10.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = if (isHi) step.titleHindi else step.titleEnglish,
                                            style = MaterialTheme.typography.titleSmall.copy(
                                                fontSize = 12.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = when {
                                                    isFailPoint -> AlertRed
                                                    isValidation -> SuccessGreen
                                                    else -> GoldPrimary
                                                }
                                            )
                                        )

                                        if (isFailPoint) {
                                            Box(
                                                modifier = Modifier
                                                    .background(AlertRed, RoundedCornerShape(4.dp))
                                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                            ) {
                                                Text(if (isHi) "ग़लती का बिंदु ✗" else "Failure Point ✗", fontSize = 9.sp, color = TextPrimary, fontWeight = FontWeight.Bold)
                                            }
                                        } else if (isValidation) {
                                            Box(
                                                modifier = Modifier
                                                    .background(SuccessGreen, RoundedCornerShape(4.dp))
                                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                                            ) {
                                                Text(if (isHi) "सत्यापित हल ✓" else "Verified ✓", fontSize = 9.sp, color = NavyDeepest, fontWeight = FontWeight.Bold)
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = if (isHi) step.detailHindi else step.detailEnglish,
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = TextPrimary,
                                            lineHeight = 18.sp
                                        )
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Final Conclusion Card
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            colors = CardDefaults.cardColors(containerColor = SuccessGreen.copy(alpha = 0.15f)),
                            border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(SuccessGreen, GoldPrimary.copy(alpha = 0.4f))))
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = if (isHi) "🎯 अंतिम निष्कर्ष (Final Deductive Conclusion):" else "🎯 Final Deductive Conclusion:",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SuccessGreen
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = if (isHi) solutionModel.finalConclusionHindi else solutionModel.finalConclusionEnglish,
                                    fontSize = 12.sp,
                                    color = TextPrimary,
                                    fontWeight = FontWeight.SemiBold,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))
            }

            // Bottom Audio Status & Continue Button
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NavyDeepest)
                    .border(1.dp, NavyBorder, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .padding(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isVoicePlaying) Icons.Default.VolumeUp else Icons.Default.Lightbulb,
                            contentDescription = null,
                            tint = if (isVoicePlaying) GoldGlow else TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isVoicePlaying) (if (isHi) "🎙️ तार्किक समाधान पढ़ रहे हैं..." else "🎙️ Explaining step-by-step solution...")
                            else (if (isHi) "समाधान पूरा समझें" else "Review solution thoroughly"),
                            fontSize = 11.sp,
                            color = if (isVoicePlaying) GoldGlow else TextSecondary
                        )
                    }

                    // Replay Voice Button
                    IconButton(
                        onClick = {
                            val script = if (isHi) solutionModel.teacherVoiceScriptHindi else solutionModel.teacherVoiceScriptEnglish
                            viewModel.speechNarrator.speakSolutionNatural(script, language)
                            isVoicePlaying = true
                        },
                        modifier = Modifier.size(32.dp).testTag("replay_solution_audio_button")
                    ) {
                        Icon(Icons.Default.Replay, contentDescription = "Replay", tint = InfoCyan, modifier = Modifier.size(18.dp))
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Continue Button ("आगे बढ़ें")
                Button(
                    onClick = {
                        viewModel.speechNarrator.stop()
                        onContinue()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("continue_after_solution_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary)
                ) {
                    Text(
                        text = if (isHi) "आगे बढ़ें (Continue)" else "CONTINUE",
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = NavyDeepest,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Continue",
                        tint = NavyDeepest,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
