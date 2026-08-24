package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.api.GeminiApiClient
import com.example.ui.theme.CheckpointBronze
import com.example.ui.theme.CheckpointGold
import com.example.ui.theme.CheckpointSilver
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldGlow
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.InfoCyan
import com.example.ui.theme.NavyBackground
import com.example.ui.theme.NavyCard
import com.example.ui.theme.NavyCardElevated
import com.example.ui.theme.NavyDeepest
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

/**
 * Displays the 17-Tier Progressive Reasoning Ladder with checkpoints (पड़ाव).
 */
@Composable
fun LadderDrawer(
    currentQuestionNumber: Int,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    // List 17 to 1 descending (top prize at the top!)
    val allTiers = (17 downTo 1).map { qNum ->
        GeminiApiClient.getTierDetails(qNum) to qNum
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(NavyBackground.copy(alpha = 0.96f))
            .padding(16.dp)
            .testTag("ladder_drawer_sheet")
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.EmojiEvents,
                        contentDescription = "Tark Ladder",
                        tint = GoldPrimary,
                        modifier = Modifier.size(26.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "तर्क सीढ़ी / 17-Tier Ladder",
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                IconButton(
                    onClick = onClose,
                    modifier = Modifier.testTag("close_ladder_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = TextSecondary
                    )
                }
            }

            Text(
                text = "🏆 Q5, Q10, Q16 सुरक्षित पड़ाव (Guaranteed Checkpoints) हैं।",
                style = MaterialTheme.typography.bodySmall.copy(
                    color = GoldGlow,
                    fontSize = 12.sp
                ),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items(allTiers) { (tier, qNum) ->
                    val isCurrent = qNum == currentQuestionNumber
                    val isPassed = qNum < currentQuestionNumber
                    val isCheckpoint = tier.isCheckpoint

                    val cardBg = when {
                        isCurrent -> GoldPrimary.copy(alpha = 0.25f)
                        isPassed -> SuccessGreen.copy(alpha = 0.12f)
                        isCheckpoint -> NavyCardElevated
                        else -> NavyCard.copy(alpha = 0.6f)
                    }

                    val borderColor = when {
                        isCurrent -> GoldPrimary
                        isPassed -> SuccessGreen.copy(alpha = 0.5f)
                        isCheckpoint -> when (qNum) {
                            5 -> CheckpointBronze
                            10 -> CheckpointSilver
                            16 -> CheckpointGold
                            17 -> GoldGlow
                            else -> GoldPrimary
                        }
                        else -> Color.Transparent
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("ladder_tier_item_$qNum"),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = cardBg),
                        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(borderColor, borderColor.copy(alpha = 0.3f))))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .background(
                                            if (isCurrent) GoldPrimary else if (isPassed) SuccessGreen else NavyDeepest,
                                            CircleShape
                                        ),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "$qNum",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isCurrent || isPassed) NavyDeepest else TextSecondary
                                    )
                                }

                                Spacer(modifier = Modifier.width(10.dp))

                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                            text = tier.prizeFormatted,
                                            style = MaterialTheme.typography.bodyMedium.copy(
                                                fontWeight = if (isCurrent || isCheckpoint) FontWeight.Bold else FontWeight.Normal,
                                                color = if (isCurrent) GoldGlow else if (isCheckpoint) Color.White else TextPrimary
                                            )
                                        )

                                        if (isCheckpoint) {
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Icon(
                                                imageVector = Icons.Default.Lock,
                                                contentDescription = "Safe Padav",
                                                tint = GoldPrimary,
                                                modifier = Modifier.size(13.dp)
                                            )
                                        }
                                    }

                                    if (tier.checkpointTitle != null) {
                                        Text(
                                            text = tier.checkpointTitle,
                                            fontSize = 10.sp,
                                            color = GoldGlow
                                        )
                                    }
                                }
                            }

                            Text(
                                text = if (tier.timeLimitSeconds != null) "${tier.timeLimitSeconds}s" else "No Limit",
                                fontSize = 11.sp,
                                color = if (tier.timeLimitSeconds != null) InfoCyan else TextSecondary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}
