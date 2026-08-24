package com.example.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.GoldGlow
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.InfoCyan
import com.example.ui.theme.NavyCard
import com.example.ui.theme.NavyCardElevated
import com.example.ui.theme.NavyDeepest
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlin.math.cos
import kotlin.math.sin

/**
 * Renders visual, geometric, and diagrammatic reasoning clues on a Jetpack Compose Canvas.
 */
@Composable
fun DiagramCanvas(
    diagramType: String,
    diagramData: String,
    audioPatternType: String?,
    onPlayAudio: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    if (diagramType == "none" && audioPatternType == null) return

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .testTag("diagram_clue_card"),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = NavyCardElevated),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(GoldPrimary.copy(alpha = 0.4f), InfoCyan.copy(alpha = 0.4f))))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "तार्किक आरेख / Visual Clue",
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = GoldPrimary,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                )

                if (audioPatternType != null && onPlayAudio != null) {
                    FilledTonalButton(
                        onClick = onPlayAudio,
                        modifier = Modifier.testTag("play_rhythm_audio_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Play sound clue",
                            tint = GoldPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("ध्वनि सुनें (Play Audio)", fontSize = 12.sp, color = TextPrimary)
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            when (diagramType) {
                "coordinate_path" -> CoordinatePathVisualizer()
                "shadow_sun" -> ShadowOpticsVisualizer()
                "matrix_grid" -> MatrixGridVisualizer()
                "clock_angle" -> ClockAngleVisualizer()
                "audio_wave" -> AudioRhythmVisualizer(onPlayAudio)
                "venn_logic" -> VennLogicVisualizer()
                else -> {
                    if (audioPatternType != null) {
                        AudioRhythmVisualizer(onPlayAudio)
                    }
                }
            }
        }
    }
}

@Composable
fun CoordinatePathVisualizer() {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulse by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(tween(1000, easing = LinearEasing), RepeatMode.Reverse),
        label = "pulse"
    )

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .background(NavyDeepest, RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        val w = size.width
        val h = size.height
        val startX = w * 0.35f
        val startY = h * 0.85f
        val midX = startX
        val midY = h * 0.25f
        val endX = w * 0.75f
        val endY = midY

        // Grid lines
        for (i in 1..4) {
            drawLine(
                color = Color.White.copy(alpha = 0.07f),
                start = Offset(0f, h * i / 5f),
                end = Offset(w, h * i / 5f),
                strokeWidth = 1f
            )
            drawLine(
                color = Color.White.copy(alpha = 0.07f),
                start = Offset(w * i / 5f, 0f),
                end = Offset(w * i / 5f, h),
                strokeWidth = 1f
            )
        }

        // Vector 1: North (10 km)
        drawLine(
            color = InfoCyan,
            start = Offset(startX, startY),
            end = Offset(midX, midY),
            strokeWidth = 4f,
            cap = StrokeCap.Round
        )

        // Vector 2: East (5 km)
        drawLine(
            color = GoldPrimary,
            start = Offset(midX, midY),
            end = Offset(endX, endY),
            strokeWidth = 4f,
            cap = StrokeCap.Round
        )

        // Displacement Vector
        drawLine(
            color = SuccessGreen,
            start = Offset(startX, startY),
            end = Offset(endX, endY),
            strokeWidth = 2.5f,
            cap = StrokeCap.Round
        )

        // Start Point A
        drawCircle(color = Color.White, radius = 6f, center = Offset(startX, startY))

        // Turn Point
        drawCircle(color = InfoCyan, radius = 5f, center = Offset(midX, midY))

        // End Point B (Pulsing)
        drawCircle(color = GoldGlow, radius = 7f * pulse, center = Offset(endX, endY))
    }
}

@Composable
fun ShadowOpticsVisualizer() {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .background(NavyDeepest, RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        val w = size.width
        val h = size.height
        val groundY = h * 0.75f
        val poleX = w * 0.55f

        // Ground line
        drawLine(color = Color.Gray, start = Offset(w * 0.05f, groundY), end = Offset(w * 0.95f, groundY), strokeWidth = 2f)

        // Light Source (Sun) - East (Right side)
        val sunX = w * 0.85f
        val sunY = h * 0.25f
        drawCircle(color = GoldGlow, radius = 18f, center = Offset(sunX, sunY))
        drawCircle(color = GoldPrimary.copy(alpha = 0.3f), radius = 28f, center = Offset(sunX, sunY))

        // Vertical Pole
        val poleTopY = h * 0.35f
        drawLine(color = Color.White, start = Offset(poleX, groundY), end = Offset(poleX, poleTopY), strokeWidth = 6f, cap = StrokeCap.Round)

        // Light Ray passing pole top
        drawLine(
            color = GoldPrimary.copy(alpha = 0.5f),
            start = Offset(sunX, sunY),
            end = Offset(w * 0.2f, groundY),
            strokeWidth = 2f
        )

        // Shadow on West (Left side)
        drawLine(
            color = InfoCyan,
            start = Offset(poleX, groundY + 1f),
            end = Offset(w * 0.2f, groundY + 1f),
            strokeWidth = 7f,
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun MatrixGridVisualizer() {
    val rows = listOf(
        listOf("4", "9", "2"),
        listOf("3", "5", "7"),
        listOf("8", "1", "?")
    )

    Column(
        modifier = Modifier
            .background(NavyDeepest, RoundedCornerShape(8.dp))
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        rows.forEachIndexed { rIdx, row ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEachIndexed { cIdx, cell ->
                    val isTarget = cell == "?"
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .background(
                                if (isTarget) GoldPrimary.copy(alpha = 0.25f) else NavyCard,
                                RoundedCornerShape(6.dp)
                            )
                            .border(
                                width = if (isTarget) 2.dp else 1.dp,
                                color = if (isTarget) GoldPrimary else NavyCardElevated,
                                shape = RoundedCornerShape(6.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = cell,
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = if (isTarget) GoldGlow else TextPrimary,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
            }
            if (rIdx < 2) Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun ClockAngleVisualizer() {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(130.dp)
            .background(NavyDeepest, RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val r = 50.dp.toPx()

        // Dial
        drawCircle(color = NavyCard, radius = r, center = Offset(cx, cy))
        drawCircle(color = GoldPrimary, radius = r, center = Offset(cx, cy), style = Stroke(width = 3f))

        // Center dot
        drawCircle(color = GoldGlow, radius = 5f, center = Offset(cx, cy))

        // 12 o'clock (Minute hand)
        drawLine(
            color = InfoCyan,
            start = Offset(cx, cy),
            end = Offset(cx, cy - r * 0.8f),
            strokeWidth = 4f,
            cap = StrokeCap.Round
        )

        // 3 o'clock (Hour hand)
        drawLine(
            color = GoldPrimary,
            start = Offset(cx, cy),
            end = Offset(cx + r * 0.6f, cy),
            strokeWidth = 6f,
            cap = StrokeCap.Round
        )

        // 90 degree arc
        drawArc(
            color = GoldGlow.copy(alpha = 0.4f),
            startAngle = 270f,
            sweepAngle = 90f,
            useCenter = true,
            topLeft = Offset(cx - 20f, cy - 20f),
            size = Size(40f, 40f)
        )
    }
}

@Composable
fun AudioRhythmVisualizer(onPlay: (() -> Unit)?) {
    val beats = listOf("1 (धिन - प्रबल)", "2 (तिन)", "3 (तिन)")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(NavyDeepest, RoundedCornerShape(8.dp))
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        beats.forEachIndexed { index, beatName ->
            val isAccent = index == 0
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(if (isAccent) 38.dp else 28.dp)
                        .background(
                            if (isAccent) GoldPrimary else InfoCyan.copy(alpha = 0.5f),
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${index + 1}",
                        fontWeight = FontWeight.Bold,
                        color = if (isAccent) NavyDeepest else Color.White,
                        fontSize = if (isAccent) 16.sp else 12.sp
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = beatName,
                    fontSize = 11.sp,
                    color = if (isAccent) GoldGlow else TextSecondary,
                    fontWeight = if (isAccent) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

@Composable
fun VennLogicVisualizer() {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
            .background(NavyDeepest, RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        val cx = size.width / 2f
        val cy = size.height / 2f

        // Outer set (Conductors)
        drawCircle(
            color = InfoCyan.copy(alpha = 0.25f),
            radius = 45.dp.toPx(),
            center = Offset(cx - 20f, cy)
        )
        drawCircle(
            color = InfoCyan,
            radius = 45.dp.toPx(),
            center = Offset(cx - 20f, cy),
            style = Stroke(width = 2f)
        )

        // Inner set (Metals)
        drawCircle(
            color = GoldPrimary.copy(alpha = 0.35f),
            radius = 24.dp.toPx(),
            center = Offset(cx - 20f, cy)
        )
        drawCircle(
            color = GoldPrimary,
            radius = 24.dp.toPx(),
            center = Offset(cx - 20f, cy),
            style = Stroke(width = 2f)
        )

        // Element X completely outside
        val xPos = Offset(cx + 65.dp.toPx(), cy)
        drawCircle(color = Color.Red, radius = 7f, center = xPos)
    }
}
