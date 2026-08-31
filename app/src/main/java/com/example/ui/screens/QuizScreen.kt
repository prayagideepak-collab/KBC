package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Translate
import com.example.ui.components.ScratchpadDialog
import com.example.ui.viewmodel.QuestionPhase
import com.example.ui.viewmodel.TimerMode
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.BilingualCluesList
import com.example.ui.components.BilingualHintCard
import com.example.ui.components.BilingualOptionsGrid2x2
import com.example.ui.components.BilingualQuestionCard
import com.example.ui.components.DiagramCanvas
import com.example.ui.components.ExpertAdviceDialog
import com.example.ui.components.FiftyFiftyProofDialog
import com.example.ui.components.LadderDrawer
import com.example.ui.components.LifelineControls
import com.example.ui.components.ScratchpadDialog
import com.example.ui.components.toArabicNumerals
import com.example.ui.components.toDevanagariNumerals
import com.example.ui.theme.AlertRed
import com.example.ui.theme.CheckpointBronze
import com.example.ui.theme.CheckpointGold
import com.example.ui.theme.CheckpointSilver
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldGlow
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.InfoCyan
import com.example.ui.theme.NavyBackground
import com.example.ui.theme.NavyBorder
import com.example.ui.theme.NavyCard
import com.example.ui.theme.NavyCardElevated
import com.example.ui.theme.NavyDeepest
import com.example.ui.theme.PurpleAccent
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import com.example.ui.viewmodel.QuizUiState
import com.example.ui.viewmodel.QuizViewModel

@Composable
fun QuizScreen(
    state: QuizUiState.InGame,
    viewModel: QuizViewModel,
    modifier: Modifier = Modifier
) {
    val language by viewModel.languageMode.collectAsState()
    val mode = language.uppercase()
    val isHi = mode == "HINDI" || mode == "HI"
    val userProfile by viewModel.userProfile.collectAsState()
    val isVoiceEnabled by viewModel.isVoiceNarrationEnabled.collectAsState()

    val question = state.question
    var showQuitConfirmation by remember { mutableStateOf(false) }
    var showScratchpad by remember { mutableStateOf(false) }
    var verifiedClues by remember(question.id) { mutableStateOf(setOf<Int>()) }

    val questionText = if (isHi) question.questionHindi else question.questionEnglish
    val clues = if (isHi) question.cluesHindi else question.cluesEnglish
    val options = if (isHi) question.optionsHindi else question.optionsEnglish
    val deductionProof = if (isHi) question.deductionPathHindi else question.deductionPathEnglish
    val eliminationReasons = if (isHi) question.eliminationReasonsHindi else question.eliminationReasonsEnglish

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(NavyBackground)
            .testTag("quiz_screen_container")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 8.dp)
        ) {
            // ==========================================
            // TOP HUD (Header)
            // ==========================================
            QuizHeaderHud(
                currentQNumber = state.currentQNumber,
                prizeFormatted = question.prizeFormatted,
                isCheckpoint = question.isCheckpoint,
                checkpointTitle = question.checkpointTitle,
                timeRemaining = state.timeRemainingSeconds,
                timerMode = state.timerMode,
                elapsedThinkingSeconds = state.elapsedThinkingSeconds,
                hasBonusTime = state.hasBonusTime,
                accumulatedBonusSeconds = state.accumulatedBonusSeconds,
                bonusLostNotice = state.bonusLostNotice,
                isReadOnlySession = state.isReadOnlySession,
                readOnlyRemaining = state.readOnlySecondsRemaining,
                isVoiceEnabled = isVoiceEnabled,
                userName = userProfile.name,
                onToggleVoice = { viewModel.toggleVoiceNarration() },
                onOpenScratchpad = { showScratchpad = true },
                onOpenLadder = { viewModel.toggleLadderDrawer(true) },
                onQuitClick = { showQuitConfirmation = true },
                onToggleLanguage = { viewModel.toggleLanguage() },
                isHi = isHi
            )

            // ==========================================
            // 17 LOGIC LEVELS VISUAL PROGRESS TRACKER
            // ==========================================
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .testTag("visual_progress_tracker"),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                border = BorderStroke(1.dp, NavyBorder)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isHi) "तर्क स्तर (Logic Level) ${state.currentQNumber} / 17" else "Logic Level ${state.currentQNumber} of 17",
                            color = GoldPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                        Text(
                            text = if (isHi) "${maxOf(0, 17 - state.currentQNumber)} प्रश्न शेष" else "${maxOf(0, 17 - state.currentQNumber)} questions left",
                            color = TextSecondary,
                            fontSize = 11.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        for (i in 1..17) {
                            val isCompleted = i < state.currentQNumber
                            val isCurrent = i == state.currentQNumber
                            val dotColor = when {
                                isCompleted -> SuccessGreen
                                isCurrent -> GoldPrimary
                                else -> NavyBorder
                            }
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(6.dp)
                                    .padding(horizontal = 1.dp)
                                    .clip(RoundedCornerShape(3.dp))
                                    .background(dotColor)
                            )
                        }
                    }
                }
            }

            // ==========================================
            // SCROLLABLE QUESTION & CLUES BODY
            // ==========================================
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 6.dp)
            ) {
                // Category, Difficulty & Junior Mode Tag
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .background(NavyCardElevated, RoundedCornerShape(6.dp))
                                .border(1.dp, GoldPrimary.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = question.category,
                                color = GoldGlow,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        if (userProfile.isStudentMode || userProfile.preparationDomain.contains("Student", true)) {
                            Box(
                                modifier = Modifier
                                    .background(InfoCyan.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                                    .border(1.dp, InfoCyan.copy(alpha = 0.5f), RoundedCornerShape(6.dp))
                                    .padding(horizontal = 6.dp, vertical = 3.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.School,
                                        contentDescription = null,
                                        tint = InfoCyan,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = userProfile.studentClass.ifBlank { "Junior" },
                                        color = InfoCyan,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (state.isFreeHintAvailable) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(GoldPrimary.copy(alpha = 0.2f))
                                    .border(1.dp, GoldPrimary, RoundedCornerShape(8.dp))
                                    .clickable { viewModel.showFreeHint() }
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                                    .testTag("free_hint_trigger_button")
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Lightbulb,
                                        contentDescription = "Hint",
                                        tint = GoldPrimary,
                                        modifier = Modifier.size(13.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = if (isHi) "💡 संकेत (Hint)" else "💡 Hint",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = GoldGlow
                                    )
                                }
                            }
                        }

                        Text(
                            text = "Tier: ${question.difficultyTitle}",
                            color = InfoCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Camera Identity Monitored Badge & Warning Banner
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFF1E3A1E), RoundedCornerShape(6.dp))
                            .border(1.dp, SuccessGreen.copy(alpha = 0.6f), RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                            .testTag("camera_active_badge")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(7.dp)
                                    .background(SuccessGreen, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "📷 Camera Identity Active",
                                color = SuccessGreen,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    if (state.identityWarningCount > 0) {
                        Text(
                            text = "Warnings: ${state.identityWarningCount}/3",
                            color = Color(0xFFFF5252),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                if (state.disqualificationNotice != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF5A1E1E)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = state.disqualificationNotice ?: "",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Primary Bilingual Question Card (Line-by-Line Paired Translation)
                BilingualQuestionCard(
                    questionHindi = question.questionHindi,
                    questionEnglish = question.questionEnglish,
                    preferredLanguage = language
                )

                // Bilingual Deductive Clues (Interactive checkmarks)
                if (question.cluesHindi.isNotEmpty() || question.cluesEnglish.isNotEmpty()) {
                    BilingualCluesList(
                        cluesHindi = question.cluesHindi,
                        cluesEnglish = question.cluesEnglish,
                        verifiedClues = verifiedClues,
                        preferredLanguage = language,
                        onToggleVerify = { index ->
                            verifiedClues = if (verifiedClues.contains(index)) verifiedClues - index else verifiedClues + index
                        }
                    )
                }

                // Visual / Diagram / Audio Canvas
                DiagramCanvas(
                    diagramType = question.diagramType,
                    diagramData = question.diagramData,
                    audioPatternType = question.audioPatternType,
                    onPlayAudio = { viewModel.playQuestionAudio() }
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Read Only Session Card (Active during first 5 seconds)
                if (state.isReadOnlySession) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .testTag("read_only_session_card"),
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = NavyCardElevated),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = Brush.horizontalGradient(
                                listOf(GoldPrimary.copy(alpha = 0.8f), InfoCyan.copy(alpha = 0.6f))
                            )
                        )
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.HourglassBottom,
                                    contentDescription = null,
                                    tint = GoldPrimary,
                                    modifier = Modifier.size(22.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Reading Window — 00:0${state.readOnlySecondsRemaining}",
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        color = GoldGlow,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 17.sp
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            Text(
                                text = if (isHi)
                                    "प्रश्न एवं तार्किक सुरागों को ध्यानपूर्वक पढ़ें। 5 सेकंड बाद विकल्प अनलॉक होंगे और मुख्य टाइमर शुरू होगा।"
                                else
                                    "Study the question and deductive clues carefully. Options and the main timer will unlock in 5 seconds.",
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    color = TextSecondary,
                                    lineHeight = 18.sp
                                )
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            LinearProgressIndicator(
                                progress = { (5 - state.readOnlySecondsRemaining) / 5f },
                                modifier = Modifier
                                    .fillMaxWidth(0.8f)
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp)),
                                color = GoldPrimary,
                                trackColor = NavyBorder,
                            )
                        }
                    }
                }

                // Authoritative 2×2 Paired Bilingual Options Grid (A/B Top Row, C/D Bottom Row)
                AnimatedVisibility(
                    visible = state.isOptionsVisible,
                    enter = fadeIn(tween(400)) + expandVertically(tween(400))
                ) {
                    BilingualOptionsGrid2x2(
                        optionsHindi = question.optionsHindi,
                        optionsEnglish = question.optionsEnglish,
                        selectedOptionIndex = state.selectedOptionIndex,
                        lockedOptionIndex = state.lockedOptionIndex,
                        discardedIndices = state.discardedOptionIndices,
                        isAnswerRevealed = state.isAnswerRevealed,
                        correctAnswerIndex = question.correctAnswerIndex,
                        isLockedIn = state.isLockedIn,
                        preferredLanguage = language,
                        onOptionSelected = { index ->
                            if (state.phase == QuestionPhase.ACTIVE_CHOICE && !state.isLockedIn && !state.discardedOptionIndices.contains(index)) {
                                viewModel.selectOption(index)
                            }
                        }
                    )
                }

                // Step-by-Step Bilingual Deduction Proof (Revealed after Answer Lock)
                AnimatedVisibility(visible = state.isAnswerRevealed) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .testTag("deduction_proof_card"),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (state.isCorrect) SuccessGreen.copy(alpha = 0.15f) else AlertRed.copy(alpha = 0.15f)
                        ),
                        border = CardDefaults.outlinedCardBorder().copy(
                            brush = Brush.horizontalGradient(
                                listOf(
                                    if (state.isCorrect) SuccessGreen else AlertRed,
                                    GoldPrimary.copy(alpha = 0.4f)
                                )
                            )
                        )
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (state.isCorrect) Icons.Default.CheckCircle else Icons.Default.Info,
                                    contentDescription = "Proof",
                                    tint = if (state.isCorrect) SuccessGreen else AlertRed,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (state.isCorrect) "तार्किक प्रमाण (Logical Proof)" else "तार्किक विश्लेषण (Why This Answer Fails)",
                                    style = MaterialTheme.typography.titleSmall.copy(
                                        color = if (state.isCorrect) SuccessGreen else AlertRed,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            // Paired Deduction Path
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(NavyDeepest.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                    .padding(8.dp)
                            ) {
                                if (isHi) {
                                    Text(
                                        text = question.deductionPathHindi.toDevanagariNumerals(),
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = TextPrimary,
                                            fontWeight = FontWeight.SemiBold,
                                            lineHeight = 20.sp
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = question.deductionPathEnglish.toArabicNumerals(),
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = InfoCyan.copy(alpha = 0.85f),
                                            lineHeight = 17.sp
                                        )
                                    )
                                } else {
                                    Text(
                                        text = question.deductionPathEnglish.toArabicNumerals(),
                                        style = MaterialTheme.typography.bodyMedium.copy(
                                            color = TextPrimary,
                                            fontWeight = FontWeight.SemiBold,
                                            lineHeight = 20.sp
                                        )
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = question.deductionPathHindi.toDevanagariNumerals(),
                                        style = MaterialTheme.typography.bodySmall.copy(
                                            color = GoldGlow.copy(alpha = 0.85f),
                                            lineHeight = 17.sp
                                        )
                                    )
                                }
                            }

                            if (question.eliminationReasonsHindi.isNotEmpty() || question.eliminationReasonsEnglish.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = if (isHi) "अन्य विकल्पों का तार्किक विलोपन:" else "Elimination of other options:",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldPrimary
                                )
                                val maxElim = maxOf(question.eliminationReasonsHindi.size, question.eliminationReasonsEnglish.size)
                                for (idx in 0 until maxElim) {
                                    if (idx != question.correctAnswerIndex) {
                                        val optLetter = when (idx) { 0 -> "A"; 1 -> "B"; 2 -> "C"; else -> "D" }
                                        val hReason = question.eliminationReasonsHindi.getOrNull(idx)?.toDevanagariNumerals() ?: ""
                                        val eReason = question.eliminationReasonsEnglish.getOrNull(idx)?.toArabicNumerals() ?: ""
                                        Column(modifier = Modifier.padding(top = 4.dp)) {
                                            if (isHi) {
                                                Text(
                                                    text = "• $optLetter: $hReason",
                                                    fontSize = 11.5.sp,
                                                    color = TextSecondary,
                                                    fontWeight = FontWeight.Medium
                                                )
                                                if (eReason.isNotBlank()) {
                                                    Text(
                                                        text = "  ($eReason)",
                                                        fontSize = 10.5.sp,
                                                        color = InfoCyan.copy(alpha = 0.7f)
                                                    )
                                                }
                                            } else {
                                                Text(
                                                    text = "• $optLetter: $eReason",
                                                    fontSize = 11.5.sp,
                                                    color = TextSecondary,
                                                    fontWeight = FontWeight.Medium
                                                )
                                                if (hReason.isNotBlank()) {
                                                    Text(
                                                        text = "  ($hReason)",
                                                        fontSize = 10.5.sp,
                                                        color = GoldGlow.copy(alpha = 0.7f)
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // ==========================================
            // BOTTOM CONTROLS & LIFELINES
            // ==========================================
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(NavyDeepest)
                    .border(1.dp, NavyBorder, RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .padding(8.dp)
            ) {
                // Lifelines Bar
                LifelineControls(
                    lifelineState = state.lifelineState,
                    onUse5050 = { viewModel.use5050() },
                    onUseAskExpert = { viewModel.useAskExpert() },
                    onUseFlipQuestion = { viewModel.useFlipQuestion() },
                    onUsePowerPaplu = { target -> viewModel.usePowerPaplu(target) },
                    isEnabled = !state.isReadOnlySession
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Lock-In Button ("ताला लगाएँ")
                val canLockIn = state.selectedOptionIndex != null && !state.isLockedIn && !state.isReadOnlySession
                val isLocked = state.isLockedIn

                Button(
                    onClick = { viewModel.lockInAnswer() },
                    enabled = canLockIn,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(horizontal = 12.dp)
                        .testTag("lock_in_answer_button"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isLocked) GoldDark else GoldPrimary,
                        disabledContainerColor = if (isLocked) GoldDark.copy(alpha = 0.3f) else NavyCard
                    )
                ) {
                    Icon(
                        imageVector = if (isLocked) Icons.Default.Lock else Icons.Outlined.Lock,
                        contentDescription = "Lock In",
                        tint = if (canLockIn) NavyDeepest else if (isLocked) GoldGlow else TextMuted,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = when {
                            isLocked -> if (isHi) "🔒 उत्तर लॉक हो चुका है (LOCKED)" else "🔒 ANSWER LOCKED"
                            canLockIn -> if (isHi) "ताला लगाएँ (Lock Answer)" else "LOCK ANSWER"
                            state.isReadOnlySession -> if (isHi) "प्रश्न ध्यान से पढ़ें (Read Carefully)" else "READ QUESTION FIRST"
                            else -> if (isHi) "कृपया विकल्प चुनें (Select Option)" else "SELECT AN OPTION"
                        },
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = if (canLockIn) NavyDeepest else if (isLocked) GoldGlow else TextMuted,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }

        // ==========================================
        // OVERLAY DIALOGS
        // ==========================================

        // 1. Ladder Drawer Sheet
        if (state.isLadderDrawerOpen) {
            LadderDrawer(
                currentQuestionNumber = state.currentQNumber,
                onClose = { viewModel.toggleLadderDrawer(false) }
            )
        }

        // 2. Expert Advice Dialog
        if (state.expertDialogContent != null || state.isExpertLoading) {
            ExpertAdviceDialog(
                advice = state.expertDialogContent ?: "",
                isLoading = state.isExpertLoading,
                language = language,
                onDismiss = { viewModel.dismissExpertDialog() }
            )
        }

        // 3. 50-50 Proof Dialog
        if (state.fiftyFiftyProofDialog != null) {
            FiftyFiftyProofDialog(
                proof = state.fiftyFiftyProofDialog,
                onDismiss = { viewModel.dismiss5050Proof() }
            )
        }

        // 4. Checkpoint Milestone Fanfare Dialog
        if (state.showCheckpointFanfare != null) {
            AlertDialog(
                onDismissRequest = { viewModel.dismissCheckpointFanfare() },
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = "Milestone",
                            tint = GoldPrimary,
                            modifier = Modifier.size(30.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = state.showCheckpointFanfare,
                            color = GoldGlow,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                text = {
                    Text(
                        text = if (isHi) "बधाई! आप एक सुरक्षित पड़ाव (Checkpoint) पर हैं। यहाँ से सही उत्तर देने पर आपकी यह धनराशि 100% सुरक्षित (Guaranteed) हो जाएगी।"
                        else "Milestone Checkpoint Reached! Answering this correctly guarantees your prize money cannot drop below this threshold.",
                        color = TextPrimary,
                        lineHeight = 20.sp
                    )
                },
                confirmButton = {
                    Button(
                        onClick = { viewModel.dismissCheckpointFanfare() },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary)
                    ) {
                        Text("आगे बढ़ें (Proceed)", color = NavyDeepest, fontWeight = FontWeight.Bold)
                    }
                },
                containerColor = NavyCardElevated,
                shape = RoundedCornerShape(16.dp)
            )
        }

        // 5. Free Logical Hint Dialog (Bilingual Paired Display)
        if (state.isFreeHintVisible) {
            AlertDialog(
                onDismissRequest = { viewModel.dismissFreeHint() },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Lightbulb,
                        contentDescription = "Logical Hint",
                        tint = GoldPrimary,
                        modifier = Modifier.size(32.dp)
                    )
                },
                title = {
                    Text(
                        text = if (isHi) "💡 तार्किक संकेत (Built-in Hint)" else "💡 Logical Deductive Hint",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = GoldGlow
                        )
                    )
                },
                text = {
                    Column {
                        val hHint = question.hintHindi.ifBlank { question.cluesHindi.firstOrNull() ?: "" }
                        val eHint = question.hintEnglish.ifBlank { question.cluesEnglish.firstOrNull() ?: "" }
                        BilingualHintCard(
                            hintHindi = hHint,
                            hintEnglish = eHint,
                            preferredLanguage = language
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = if (isHi)
                                "नोट: यह इन-बिल्ट रीज़निंग संकेत है, इसने कोई लाइफलाइन खर्च नहीं की है।"
                            else
                                "Note: This is a built-in reasoning hint and does not consume any lifeline.",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { viewModel.dismissFreeHint() },
                        colors = ButtonDefaults.buttonColors(containerColor = GoldPrimary)
                    ) {
                        Text(
                            text = if (isHi) "समझ गया (Got It)" else "Got It",
                            color = NavyDeepest,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                containerColor = NavyCardElevated,
                shape = RoundedCornerShape(16.dp)
            )
        }

        // 6. Quit / Exit Session Confirmation Dialog
        if (showQuitConfirmation) {
            AlertDialog(
                onDismissRequest = { showQuitConfirmation = false },
                icon = {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "Exit Warning",
                        tint = GoldPrimary,
                        modifier = Modifier.size(32.dp)
                    )
                },
                title = {
                    Text(
                        text = if (isHi) "क्या आप सत्र छोड़ना चाहते हैं? (Exit Quiz Session)" else "Are you sure you want to exit?",
                        color = GoldPrimary,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                },
                text = {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (isHi) 
                                "⚠️ चेतावनी: यदि आप अभी इस क्विज सत्र को छोड़ते हैं, तो आपकी वर्तमान प्रगति और जीती गई राशि (${if (state.currentQNumber > 1) state.currentPointsWon else 0} अंक) समाप्त हो सकती है। क्या आपको पूरा यकीन है?"
                            else 
                                "⚠️ Warning: You are about to exit your active quiz session. Exiting now may result in accidental progress loss. Are you sure you want to leave?",
                            color = TextPrimary,
                            textAlign = TextAlign.Center,
                            lineHeight = 20.sp
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showQuitConfirmation = false
                            viewModel.quitGame()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AlertRed),
                        modifier = Modifier.testTag("confirm_quit_button")
                    ) {
                        Text(if (isHi) "हाँ, बाहर निकलें (Yes, Exit)" else "Yes, Exit Session", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showQuitConfirmation = false }) {
                        Text(if (isHi) "नहीं, खेलते रहें (Stay)" else "Cancel", color = GoldPrimary, fontWeight = FontWeight.Bold)
                    }
                },
                containerColor = NavyCardElevated,
                shape = RoundedCornerShape(16.dp)
            )
        }

        // 6. Scratchpad Canvas Dialog
        if (showScratchpad) {
            ScratchpadDialog(
                onDismiss = { showScratchpad = false },
                isHi = isHi
            )
        }
    }
}

@Composable
fun QuizHeaderHud(
    currentQNumber: Int,
    prizeFormatted: String,
    isCheckpoint: Boolean,
    checkpointTitle: String?,
    timeRemaining: Int?,
    timerMode: TimerMode = TimerMode.TIMED,
    elapsedThinkingSeconds: Int = 0,
    hasBonusTime: Boolean = false,
    accumulatedBonusSeconds: Int = 0,
    bonusLostNotice: Boolean = false,
    isReadOnlySession: Boolean = false,
    readOnlyRemaining: Int = 0,
    isVoiceEnabled: Boolean,
    userName: String = "Challenger",
    onToggleVoice: () -> Unit,
    onOpenScratchpad: () -> Unit,
    onOpenLadder: () -> Unit,
    onQuitClick: () -> Unit,
    onToggleLanguage: () -> Unit,
    isHi: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("quiz_header_hud"),
        shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
        colors = CardDefaults.cardColors(containerColor = NavyDeepest),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(GoldPrimary.copy(alpha = 0.5f), NavyBorder)))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Group: Ladder & Scratchpad & Voice
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = onOpenLadder,
                    modifier = Modifier.testTag("open_ladder_drawer_button").size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = "Ladder",
                        tint = GoldPrimary,
                        modifier = Modifier.size(20.dp)
                    )
                }

                IconButton(
                    onClick = onOpenScratchpad,
                    modifier = Modifier.testTag("open_scratchpad_button").size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Create,
                        contentDescription = "Scratchpad",
                        tint = InfoCyan,
                        modifier = Modifier.size(19.dp)
                    )
                }

                IconButton(
                    onClick = onToggleVoice,
                    modifier = Modifier.testTag("toggle_voice_narration_button").size(36.dp)
                ) {
                    Icon(
                        imageVector = if (isVoiceEnabled) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                        contentDescription = "Voice",
                        tint = if (isVoiceEnabled) GoldGlow else TextSecondary.copy(alpha = 0.6f),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // User Name & Question Prize Info
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .background(GoldPrimary.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                        .padding(horizontal = 6.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = userName,
                        style = MaterialTheme.typography.labelSmall.copy(
                            color = GoldGlow,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Q$currentQNumber / 17 • $prizeFormatted",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = GoldGlow,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                )

                if (isCheckpoint && checkpointTitle != null) {
                    Text(
                        text = "🔒 $checkpointTitle",
                        fontSize = 10.sp,
                        color = CheckpointGold,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Timer & Actions
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (isReadOnlySession) {
                    Box(
                        modifier = Modifier
                            .background(GoldPrimary.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                            .border(1.2.dp, GoldPrimary, RoundedCornerShape(12.dp))
                            .padding(horizontal = 8.dp, vertical = 5.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.HourglassBottom,
                                contentDescription = null,
                                tint = GoldPrimary,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "00:0$readOnlyRemaining",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = GoldGlow
                            )
                        }
                    }
                } else if (timerMode == TimerMode.UNLIMITED_ELAPSED) {
                    val minutes = elapsedThinkingSeconds / 60
                    val seconds = elapsedThinkingSeconds % 60
                    val formattedTime = String.format(java.util.Locale.US, "%02d:%02d", minutes, seconds)

                    Box(
                        modifier = Modifier
                            .background(NavyCard, RoundedCornerShape(10.dp))
                            .border(1.2.dp, PurpleAccent.copy(alpha = 0.7f), RoundedCornerShape(10.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Psychology,
                                contentDescription = null,
                                tint = PurpleAccent,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = formattedTime,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = PurpleAccent
                            )
                        }
                    }
                } else if (timeRemaining != null) {
                    val minutes = timeRemaining / 60
                    val seconds = timeRemaining % 60
                    val formattedTime = String.format(java.util.Locale.US, "%02d:%02d", minutes, seconds)

                    // 4-Tier Visual Urgency Classification
                    val isFinalSeconds = timeRemaining <= 5
                    val isCritical = timeRemaining in 6..10
                    val isAttention = timeRemaining in 11..30

                    val containerBg = when {
                        isFinalSeconds -> AlertRed.copy(alpha = 0.38f)
                        isCritical -> AlertRed.copy(alpha = 0.22f)
                        isAttention -> GoldPrimary.copy(alpha = 0.18f)
                        else -> NavyCard
                    }

                    val borderColor = when {
                        isFinalSeconds -> AlertRed
                        isCritical -> AlertRed.copy(alpha = 0.85f)
                        isAttention -> GoldPrimary.copy(alpha = 0.8f)
                        else -> InfoCyan.copy(alpha = 0.6f)
                    }

                    val textColor = when {
                        isFinalSeconds || isCritical -> AlertRed
                        isAttention -> GoldGlow
                        else -> InfoCyan
                    }

                    val borderWidth = if (isFinalSeconds) 2.dp else 1.2.dp

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        if (hasBonusTime) {
                            Box(
                                modifier = Modifier
                                    .background(GoldPrimary.copy(alpha = 0.18f), RoundedCornerShape(8.dp))
                                    .border(1.dp, GoldPrimary.copy(alpha = 0.6f), RoundedCornerShape(8.dp))
                                    .padding(horizontal = 5.dp, vertical = 3.dp)
                            ) {
                                Text(
                                    text = "+${if (accumulatedBonusSeconds > 0) accumulatedBonusSeconds else 5}s Bonus",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldGlow
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .background(containerBg, RoundedCornerShape(10.dp))
                                .border(borderWidth, borderColor, RoundedCornerShape(10.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = formattedTime,
                                fontWeight = if (isFinalSeconds || isCritical) FontWeight.Black else FontWeight.Bold,
                                fontSize = 13.sp,
                                color = textColor
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .background(NavyCard, RoundedCornerShape(8.dp))
                            .border(1.dp, InfoCyan.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (isHi) "🏆 असीमित" else "🏆 Untimed",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = InfoCyan
                        )
                    }
                }

                Spacer(modifier = Modifier.width(4.dp))

                // Quit Button
                IconButton(
                    onClick = onQuitClick,
                    modifier = Modifier.testTag("quit_game_button").size(34.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "Quit",
                        tint = TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun QuizOptionCard(
    optionLetter: String,
    optionText: String,
    isSelected: Boolean,
    isDiscarded: Boolean,
    isAnswerRevealed: Boolean,
    isCorrect: Boolean,
    isLockedIn: Boolean,
    testTag: String,
    onClick: () -> Unit
) {
    val targetBgColor = when {
        isDiscarded -> NavyDeepest.copy(alpha = 0.4f)
        isAnswerRevealed && isCorrect -> SuccessGreen.copy(alpha = 0.35f)
        isAnswerRevealed && isSelected && !isCorrect -> AlertRed.copy(alpha = 0.35f)
        isSelected && isLockedIn -> GoldDark.copy(alpha = 0.4f)
        isSelected -> GoldPrimary.copy(alpha = 0.2f)
        else -> NavyCard
    }

    val targetBorderColor = when {
        isDiscarded -> Color.Transparent
        isAnswerRevealed && isCorrect -> SuccessGreen
        isAnswerRevealed && isSelected && !isCorrect -> AlertRed
        isSelected -> GoldPrimary
        else -> NavyBorder
    }

    val animatedBg by animateColorAsState(targetValue = targetBgColor, animationSpec = tween(300), label = "bg")
    val animatedBorder by animateColorAsState(targetValue = targetBorderColor, animationSpec = tween(300), label = "border")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(testTag)
            .clickable(enabled = !isDiscarded && !isLockedIn, onClick = onClick),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = animatedBg),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(animatedBorder, animatedBorder.copy(alpha = 0.5f))))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(
                        when {
                            isDiscarded -> NavyDeepest
                            isAnswerRevealed && isCorrect -> SuccessGreen
                            isAnswerRevealed && isSelected && !isCorrect -> AlertRed
                            isSelected -> GoldPrimary
                            else -> NavyCardElevated
                        },
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = optionLetter,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = when {
                        isDiscarded -> TextMuted
                        isSelected || (isAnswerRevealed && isCorrect) -> NavyDeepest
                        else -> TextPrimary
                    }
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = optionText,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = if (isDiscarded) TextMuted else TextPrimary,
                    fontWeight = if (isSelected || (isAnswerRevealed && isCorrect)) FontWeight.Bold else FontWeight.Normal,
                    textDecoration = if (isDiscarded) TextDecoration.LineThrough else TextDecoration.None
                )
            )
        }
    }
}
