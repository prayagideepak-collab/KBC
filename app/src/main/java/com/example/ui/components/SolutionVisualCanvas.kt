package com.example.ui.components

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
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AlertRed
import com.example.ui.theme.GoldGlow
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.InfoCyan
import com.example.ui.theme.NavyBorder
import com.example.ui.theme.NavyCard
import com.example.ui.theme.NavyCardElevated
import com.example.ui.theme.NavyDeepest
import com.example.ui.theme.SuccessGreen
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

/**
 * Visual Reasoning Explanation Canvas.
 * Renders visual models of:
 * - Direction & Movement Vectors (Correct Green Path vs Incorrect Red Deviation)
 * - Mathematical Formulas & Step Computations
 * - Venn Diagrams & Syllogism Region Overlaps
 * - Sequence Timelines & Knights/Knaves Truth Matrices
 */
@Composable
fun SolutionVisualCanvas(
    diagramType: String,
    category: String,
    chosenLetter: String,
    correctLetter: String,
    isHi: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = NavyCardElevated),
        border = CardDefaults.outlinedCardBorder().copy(brush = androidx.compose.ui.graphics.Brush.horizontalGradient(listOf(NavyBorder, GoldPrimary.copy(alpha = 0.3f))))
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Timeline,
                        contentDescription = "Visual Solution",
                        tint = GoldGlow,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isHi) "तार्किक दृश्य निरूपण (Visual Model)" else "Visual Reasoning Model",
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = GoldGlow,
                            fontWeight = FontWeight.Bold
                        )
                    )
                }

                // Legend
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(8.dp).background(SuccessGreen, CircleShape))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (isHi) "सही पथ" else "Correct", fontSize = 10.sp, color = TextSecondary)

                    Spacer(modifier = Modifier.width(8.dp))

                    Box(modifier = Modifier.size(8.dp).background(AlertRed, CircleShape))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(if (isHi) "गलत पथ" else "Chosen", fontSize = 10.sp, color = TextSecondary)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            when {
                diagramType == "coordinate_path" || category.contains("Spatial", true) || category.contains("Shadow", true) || category.contains("Direction", true) -> {
                    DirectionVisualModel(isHi = isHi, chosenLetter = chosenLetter, correctLetter = correctLetter)
                }
                diagramType == "venn_logic" || category.contains("Syllogism", true) -> {
                    VennDiagramVisualModel(isHi = isHi, chosenLetter = chosenLetter, correctLetter = correctLetter)
                }
                diagramType == "matrix_grid" || category.contains("Truth", true) || category.contains("Knights", true) -> {
                    TruthMatrixVisualModel(isHi = isHi, chosenLetter = chosenLetter, correctLetter = correctLetter)
                }
                else -> {
                    FormulaCalculationVisualModel(isHi = isHi, category = category, chosenLetter = chosenLetter, correctLetter = correctLetter)
                }
            }
        }
    }
}

@Composable
private fun DirectionVisualModel(isHi: Boolean, chosenLetter: String, correctLetter: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp)
            .background(NavyDeepest, RoundedCornerShape(8.dp))
            .border(1.dp, NavyBorder, RoundedCornerShape(8.dp))
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val w = size.width
            val h = size.height
            val start = Offset(w * 0.2f, h * 0.7f)
            val intermediate1 = Offset(w * 0.2f, h * 0.25f) // Move North
            val correctEnd = Offset(w * 0.75f, h * 0.25f)   // Turn East (Correct)
            val wrongEnd = Offset(w * 0.2f, h * 0.85f)      // Wrong turn / overshoot (User)

            // Compass Rose faint lines
            drawLine(Color.White.copy(alpha = 0.08f), Offset(w * 0.85f, h * 0.15f), Offset(w * 0.85f, h * 0.45f), strokeWidth = 1f)
            drawLine(Color.White.copy(alpha = 0.08f), Offset(w * 0.7f, h * 0.3f), Offset(w * 1.0f, h * 0.3f), strokeWidth = 1f)

            // 1. Initial Leg (Common Path)
            drawLine(
                color = InfoCyan,
                start = start,
                end = intermediate1,
                strokeWidth = 5f,
                cap = StrokeCap.Round
            )

            // 2. Correct Turn Leg (Green solid)
            drawLine(
                color = SuccessGreen,
                start = intermediate1,
                end = correctEnd,
                strokeWidth = 6f,
                cap = StrokeCap.Round
            )

            // 3. User Deviation Leg (Red dashed)
            drawLine(
                color = AlertRed,
                start = intermediate1,
                end = wrongEnd,
                strokeWidth = 4f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f), 0f),
                cap = StrokeCap.Round
            )

            // Start Node
            drawCircle(Color.White, radius = 6f, center = start)
            drawCircle(NavyDeepest, radius = 3f, center = start)

            // Correct Target Node
            drawCircle(SuccessGreen, radius = 8f, center = correctEnd)
            drawCircle(Color.White, radius = 4f, center = correctEnd)

            // Wrong Target Node
            drawCircle(AlertRed, radius = 7f, center = wrongEnd)
            drawCircle(Color.White, radius = 3f, center = wrongEnd)
        }

        // Overlay Badges
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("Start (मूल बिंदु)", fontSize = 10.sp, color = InfoCyan, fontWeight = FontWeight.Bold)
                Text("Leg 1: North", fontSize = 9.sp, color = TextSecondary)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text("✓ Correct ($correctLetter)", fontSize = 10.sp, color = SuccessGreen, fontWeight = FontWeight.Bold)
                Text("✗ Chosen ($chosenLetter)", fontSize = 10.sp, color = AlertRed, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun VennDiagramVisualModel(isHi: Boolean, chosenLetter: String, correctLetter: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .background(NavyDeepest, RoundedCornerShape(8.dp))
            .border(1.dp, NavyBorder, RoundedCornerShape(8.dp))
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.matchParentSize()) {
            val cx = size.width / 2f
            val cy = size.height / 2f
            val r = 46f

            // Set A (Left Circle)
            drawCircle(
                color = InfoCyan.copy(alpha = 0.25f),
                radius = r,
                center = Offset(cx - 30f, cy)
            )
            drawCircle(
                color = InfoCyan,
                radius = r,
                center = Offset(cx - 30f, cy),
                style = Stroke(width = 2.5f)
            )

            // Set B (Right Circle)
            drawCircle(
                color = GoldPrimary.copy(alpha = 0.25f),
                radius = r,
                center = Offset(cx + 30f, cy)
            )
            drawCircle(
                color = GoldPrimary,
                radius = r,
                center = Offset(cx + 30f, cy),
                style = Stroke(width = 2.5f)
            )

            // Intersection (Valid overlap)
            drawCircle(
                color = SuccessGreen,
                radius = 10f,
                center = Offset(cx, cy)
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Premise A (कथन 1)", fontSize = 10.sp, color = InfoCyan)
                Text("Premise B (कथन 2)", fontSize = 10.sp, color = GoldPrimary)
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text(
                    text = "तार्किक प्रतिच्छेदन (Valid Intersection) = Option $correctLetter",
                    fontSize = 11.sp,
                    color = SuccessGreen,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun TruthMatrixVisualModel(isHi: Boolean, chosenLetter: String, correctLetter: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(NavyDeepest, RoundedCornerShape(8.dp))
            .border(1.dp, NavyBorder, RoundedCornerShape(8.dp))
            .padding(10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(NavyCard, RoundedCornerShape(4.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("स्थिति / Person", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
            Text("कथन (Statement)", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
            Text("परिणाम (Outcome)", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Row 1: User's Choice (Contradiction)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(AlertRed.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Option $chosenLetter", fontSize = 11.sp, color = AlertRed, fontWeight = FontWeight.Bold)
            Text("विरोधाभास उत्पन्न (Contradiction)", fontSize = 10.sp, color = TextPrimary)
            Text("✗ अमान्य (False)", fontSize = 11.sp, color = AlertRed, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Row 2: Correct Choice (Consistent)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(SuccessGreen.copy(alpha = 0.12f), RoundedCornerShape(4.dp))
                .padding(horizontal = 8.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Option $correctLetter", fontSize = 11.sp, color = SuccessGreen, fontWeight = FontWeight.Bold)
            Text("शर्तें 100% सुसंगत (Consistent)", fontSize = 10.sp, color = TextPrimary)
            Text("✓ सत्य (Valid)", fontSize = 11.sp, color = SuccessGreen, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun FormulaCalculationVisualModel(
    isHi: Boolean,
    category: String,
    chosenLetter: String,
    correctLetter: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(NavyDeepest, RoundedCornerShape(8.dp))
            .border(1.dp, NavyBorder, RoundedCornerShape(8.dp))
            .padding(10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Functions, contentDescription = null, tint = InfoCyan, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = if (isHi) "सूत्र व गणना चरण (Mathematical Derivation):" else "Formula & Computation Steps:",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = InfoCyan
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(NavyCard, RoundedCornerShape(6.dp))
                    .padding(8.dp)
            ) {
                Column {
                    Text(if (isHi) "1. सूत्र (Formula)" else "1. Principle Formula", fontSize = 9.sp, color = TextMuted)
                    Text("Time = Distance ÷ Speed", fontSize = 10.sp, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.width(6.dp))
            Icon(Icons.Default.ArrowForward, contentDescription = null, tint = TextMuted, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(6.dp))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(NavyCard, RoundedCornerShape(6.dp))
                    .padding(8.dp)
            ) {
                Column {
                    Text(if (isHi) "2. मान प्रतिस्थापन" else "2. Substitution", fontSize = 9.sp, color = TextMuted)
                    Text("Value × Conversion (× 60)", fontSize = 10.sp, color = TextPrimary, fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.width(6.dp))
            Icon(Icons.Default.ArrowForward, contentDescription = null, tint = TextMuted, modifier = Modifier.size(14.dp))
            Spacer(modifier = Modifier.width(6.dp))

            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(SuccessGreen.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                    .border(1.dp, SuccessGreen, RoundedCornerShape(6.dp))
                    .padding(8.dp)
            ) {
                Column {
                    Text(if (isHi) "3. सही परिणाम" else "3. Proven Result", fontSize = 9.sp, color = SuccessGreen)
                    Text("Option $correctLetter ✓", fontSize = 10.sp, color = SuccessGreen, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
