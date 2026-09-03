package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.api.GeminiApiClient
import com.example.ui.theme.*

@Composable
fun DifficultyLevelIndicator(
    currentLevel: Int,
    totalLevels: Int = 17,
    isHindi: Boolean = false,
    modifier: Modifier = Modifier
) {
    val clampedLevel = currentLevel.coerceIn(1, totalLevels)
    val tierMeta = GeminiApiClient.getTierDetails(clampedLevel)

    val difficultyColor = when {
        tierMeta.isCheckpoint -> CheckpointGold
        clampedLevel <= 2 -> InfoCyan
        clampedLevel <= 5 -> SuccessGreen
        clampedLevel <= 9 -> GoldPrimary
        clampedLevel <= 12 -> CheckpointSilver
        clampedLevel <= 15 -> PurpleAccent
        else -> AlertRed
    }

    // Subtle animation for level transition
    val animatedProgress by animateFloatAsState(
        targetValue = clampedLevel.toFloat() / totalLevels.toFloat(),
        animationSpec = spring(dampingRatio = 0.7f, stiffness = 200f),
        label = "difficulty_progress"
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(
                    listOf(NavyCardElevated.copy(alpha = 0.9f), NavyCard.copy(alpha = 0.8f))
                ),
                RoundedCornerShape(12.dp)
            )
            .border(1.dp, difficultyColor.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
            .padding(12.dp)
            .testTag("difficulty_level_indicator")
    ) {
        // Header Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(difficultyColor.copy(alpha = 0.2f))
                        .border(1.dp, difficultyColor, RoundedCornerShape(8.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (tierMeta.isCheckpoint) Icons.Default.EmojiEvents else Icons.Default.Star,
                        contentDescription = null,
                        tint = difficultyColor,
                        modifier = Modifier.size(16.dp)
                    )
                }
                Column {
                    Text(
                        text = if (isHindi) "कठिनाई स्तर $clampedLevel / $totalLevels" else "Difficulty Level $clampedLevel / $totalLevels",
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = tierMeta.difficultyTitle,
                        color = difficultyColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // Checkpoint or Prize info
            Box(
                modifier = Modifier
                    .background(NavyDeepest, RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Text(
                    text = "${tierMeta.points} POINTS",
                    color = GoldGlow,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Segmented Progress Bar / Visual Tracker (17 segments)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (i in 1..totalLevels) {
                val isCompleted = i < clampedLevel
                val isCurrent = i == clampedLevel
                val segmentColor = when {
                    isCompleted -> SuccessGreen
                    isCurrent -> difficultyColor
                    else -> NavyBorder
                }

                val segmentScale by animateFloatAsState(
                    targetValue = if (isCurrent) 1.15f else 1f,
                    animationSpec = spring(dampingRatio = 0.5f, stiffness = 300f),
                    label = "segment_scale_$i"
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(7.dp)
                        .padding(horizontal = 1.dp)
                        .scale(1f, segmentScale)
                        .clip(RoundedCornerShape(3.5.dp))
                        .background(segmentColor)
                )
            }
        }
    }
}
