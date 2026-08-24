package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Undo
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.ui.theme.AlertRed
import com.example.ui.theme.GoldGlow
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.InfoCyan
import com.example.ui.theme.NavyBackground
import com.example.ui.theme.NavyBorder
import com.example.ui.theme.NavyCardElevated
import com.example.ui.theme.NavyDeepest
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

data class DrawingStroke(
    val path: List<Offset>,
    val color: Color,
    val strokeWidth: Float
)

@Composable
fun ScratchpadDialog(
    onDismiss: () -> Unit,
    isHi: Boolean = true,
    modifier: Modifier = Modifier
) {
    val strokes = remember { mutableStateListOf<DrawingStroke>() }
    var currentPath = remember { mutableStateListOf<Offset>() }
    var selectedColor by remember { mutableStateOf(GoldPrimary) }
    var selectedStrokeWidth by remember { mutableFloatStateOf(6f) }

    val colorPalette = listOf(
        GoldPrimary,
        InfoCyan,
        Color.White,
        SuccessGreen,
        AlertRed
    )

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = modifier
                .fillMaxSize()
                .padding(12.dp)
                .testTag("scratchpad_dialog"),
            color = NavyBackground.copy(alpha = 0.96f),
            shape = RoundedCornerShape(16.dp),
            border = CardDefaults.outlinedCardBorder().copy(
                brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                    listOf(GoldPrimary.copy(alpha = 0.8f), InfoCyan.copy(alpha = 0.4f))
                )
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
            ) {
                // Header Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Create,
                            contentDescription = null,
                            tint = GoldPrimary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isHi) "तर्क स्क्रैचपैड (Deduction Canvas)" else "Logic Scratchpad & Notes",
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = GoldGlow,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Undo Button
                        IconButton(
                            onClick = {
                                if (strokes.isNotEmpty()) {
                                    strokes.removeAt(strokes.size - 1)
                                }
                            },
                            enabled = strokes.isNotEmpty()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Undo,
                                contentDescription = "Undo",
                                tint = if (strokes.isNotEmpty()) GoldPrimary else TextSecondary.copy(alpha = 0.3f)
                            )
                        }

                        // Clear Button
                        IconButton(
                            onClick = {
                                strokes.clear()
                                currentPath.clear()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Clear All",
                                tint = AlertRed.copy(alpha = 0.9f)
                            )
                        }

                        // Close Button
                        IconButton(onClick = onDismiss) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close",
                                tint = Color.White
                            )
                        }
                    }
                }

                // Drawing Canvas Area
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(NavyDeepest)
                        .border(1.dp, NavyBorder, RoundedCornerShape(12.dp))
                        .pointerInput(selectedColor, selectedStrokeWidth) {
                            detectDragGestures(
                                onDragStart = { offset ->
                                    currentPath.clear()
                                    currentPath.add(offset)
                                },
                                onDrag = { change, _ ->
                                    change.consume()
                                    currentPath.add(change.position)
                                },
                                onDragEnd = {
                                    if (currentPath.isNotEmpty()) {
                                        strokes.add(
                                            DrawingStroke(
                                                path = currentPath.toList(),
                                                color = selectedColor,
                                                strokeWidth = selectedStrokeWidth
                                            )
                                        )
                                        currentPath.clear()
                                    }
                                },
                                onDragCancel = {
                                    currentPath.clear()
                                }
                            )
                        }
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        // Draw previously committed strokes
                        for (stroke in strokes) {
                            if (stroke.path.size > 1) {
                                val p = Path()
                                p.moveTo(stroke.path[0].x, stroke.path[0].y)
                                for (i in 1 until stroke.path.size) {
                                    p.lineTo(stroke.path[i].x, stroke.path[i].y)
                                }
                                drawPath(
                                    path = p,
                                    color = stroke.color,
                                    style = Stroke(
                                        width = stroke.strokeWidth,
                                        cap = StrokeCap.Round,
                                        join = StrokeJoin.Round
                                    )
                                )
                            }
                        }

                        // Draw active stroke
                        if (currentPath.size > 1) {
                            val activeP = Path()
                            activeP.moveTo(currentPath[0].x, currentPath[0].y)
                            for (i in 1 until currentPath.size) {
                                activeP.lineTo(currentPath[i].x, currentPath[i].y)
                            }
                            drawPath(
                                path = activeP,
                                color = selectedColor,
                                style = Stroke(
                                    width = selectedStrokeWidth,
                                    cap = StrokeCap.Round,
                                    join = StrokeJoin.Round
                                )
                            )
                        }
                    }

                    if (strokes.isEmpty() && currentPath.isEmpty()) {
                        Text(
                            text = if (isHi) "✍️ यहाँ वेन आरेख, समीकरण या सुराग लिखें..." else "✍️ Sketch Venn diagrams, formulas, or timeline notes here...",
                            color = TextSecondary.copy(alpha = 0.4f),
                            fontSize = 13.sp,
                            modifier = Modifier
                                .align(Alignment.Center)
                                .padding(16.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Bottom Controls: Colors and Stroke Width
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Color Palette
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        colorPalette.forEach { color ->
                            val isSelected = selectedColor == color
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(color)
                                    .border(
                                        width = if (isSelected) 3.dp else 1.dp,
                                        color = if (isSelected) Color.White else NavyBorder,
                                        shape = CircleShape
                                    )
                                    .clickable { selectedColor = color }
                            )
                        }
                    }

                    // Stroke Thickness Toggles
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        listOf(4f to "Fine", 8f to "Med", 14f to "Bold").forEach { (width, label) ->
                            val isSelected = selectedStrokeWidth == width
                            Box(
                                modifier = Modifier
                                    .background(
                                        if (isSelected) GoldPrimary else NavyCardElevated,
                                        RoundedCornerShape(8.dp)
                                    )
                                    .border(1.dp, if (isSelected) GoldPrimary else NavyBorder, RoundedCornerShape(8.dp))
                                    .clickable { selectedStrokeWidth = width }
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) NavyDeepest else TextPrimary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
