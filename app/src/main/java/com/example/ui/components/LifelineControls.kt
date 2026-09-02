package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.LifelineState
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

@Composable
fun LifelineControls(
    lifelineState: LifelineState,
    onUse5050: () -> Unit,
    onUseAskExpert: () -> Unit,
    onUseFlipQuestion: () -> Unit,
    onUsePowerPaplu: (rechargeTarget: String) -> Unit,
    isEnabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    var showPowerPapluDialog by remember { mutableStateOf(false) }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // 1. 50-50 Lifeline
        LifelineIconButton(
            title = "50-50",
            icon = Icons.AutoMirrored.Filled.HelpOutline,
            isAvailable = isEnabled && lifelineState.is5050Available && !lifelineState.is5050Exhausted,
            accentColor = InfoCyan,
            testTag = "lifeline_5050_button",
            onClick = { if (isEnabled) onUse5050() }
        )

        // 2. Ask the Expert (Tark Guru)
        LifelineIconButton(
            title = "Ask Expert 🤖",
            icon = Icons.Default.SmartToy,
            isAvailable = isEnabled && lifelineState.isExpertAvailable && !lifelineState.isExpertExhausted,
            accentColor = PurpleAccent,
            testTag = "lifeline_ask_expert_button",
            onClick = { if (isEnabled) onUseAskExpert() }
        )

        // 3. Flip the Question
        LifelineIconButton(
            title = "Flip 🔄",
            icon = Icons.Default.Autorenew,
            isAvailable = isEnabled && lifelineState.isFlipAvailable && !lifelineState.isFlipExhausted,
            accentColor = SuccessGreen,
            testTag = "lifeline_flip_button",
            onClick = { if (isEnabled) onUseFlipQuestion() }
        )

        // 4. Power Paplu ⚡
        val canUsePaplu = isEnabled && lifelineState.isPowerPapluAvailable &&
                !lifelineState.isPowerPapluExhausted &&
                (lifelineState.is5050Exhausted || lifelineState.isExpertExhausted || lifelineState.isFlipExhausted)

        LifelineIconButton(
            title = "Paplu ⚡",
            icon = Icons.Default.Bolt,
            isAvailable = canUsePaplu,
            accentColor = GoldPrimary,
            testTag = "lifeline_power_paplu_button",
            onClick = {
                if (canUsePaplu) showPowerPapluDialog = true
            }
        )
    }

    if (showPowerPapluDialog) {
        PowerPapluDialog(
            lifelineState = lifelineState,
            onDismiss = { showPowerPapluDialog = false },
            onSelectRecharge = { target ->
                showPowerPapluDialog = false
                onUsePowerPaplu(target)
            }
        )
    }
}

@Composable
fun LifelineIconButton(
    title: String,
    icon: ImageVector,
    isAvailable: Boolean,
    accentColor: Color,
    testTag: String,
    onClick: () -> Unit
) {
    val containerBg = if (isAvailable) NavyCardElevated else NavyDeepest
    val iconTint = if (isAvailable) accentColor else TextMuted
    val borderColor = if (isAvailable) accentColor.copy(alpha = 0.6f) else NavyBorder.copy(alpha = 0.3f)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable(enabled = isAvailable, onClick = onClick)
            .testTag(testTag)
            .padding(4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(containerBg)
                .border(1.5.dp, borderColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconTint,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall.copy(
                fontSize = 11.sp,
                fontWeight = if (isAvailable) FontWeight.SemiBold else FontWeight.Normal,
                color = if (isAvailable) TextPrimary else TextMuted
            )
        )
    }
}

@Composable
fun PowerPapluDialog(
    lifelineState: LifelineState,
    onDismiss: () -> Unit,
    onSelectRecharge: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Bolt,
                    contentDescription = "Power Paplu",
                    tint = GoldPrimary,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Power Paplu ⚡ सक्रिय करें",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = GoldGlow,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Power Paplu आपकी किसी एक समाप्त (exhausted) लाइफलाइन को दोबारा रीचार्ज कर सकता है। आप किसे पुनः प्राप्त करना चाहते हैं?",
                    style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                if (lifelineState.is5050Exhausted) {
                    Button(
                        onClick = { onSelectRecharge("50-50") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .testTag("recharge_5050_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = InfoCyan)
                    ) {
                        Text("Recharge 50-50 🛟", color = NavyDeepest, fontWeight = FontWeight.Bold)
                    }
                }

                if (lifelineState.isExpertExhausted) {
                    Button(
                        onClick = { onSelectRecharge("Ask the Expert") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .testTag("recharge_expert_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = PurpleAccent)
                    ) {
                        Text("Recharge Ask Expert 🤖", color = TextPrimary, fontWeight = FontWeight.Bold)
                    }
                }

                if (lifelineState.isFlipExhausted) {
                    Button(
                        onClick = { onSelectRecharge("Flip the Question") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .testTag("recharge_flip_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen)
                    ) {
                        Text("Recharge Flip Question 🔄", color = NavyDeepest, fontWeight = FontWeight.Bold)
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("रद्द करें (Cancel)", color = TextSecondary)
            }
        },
        containerColor = NavyCardElevated,
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
fun ExpertAdviceDialog(
    advice: String,
    isLoading: Boolean,
    language: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.SmartToy,
                    contentDescription = "AI Expert",
                    tint = PurpleAccent,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (language == "hi") "तर्क गुरु (Ask The Expert)" else "Tark Guru (Expert Guidance)",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = GoldGlow,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isLoading) {
                    CircularProgressIndicator(color = GoldPrimary, modifier = Modifier.size(36.dp))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (language == "hi") "तर्क गुरु सुरागों का विश्लेषण कर रहे हैं..." else "Analyzing logical structure...",
                        style = MaterialTheme.typography.bodySmall.copy(color = TextSecondary)
                    )
                } else {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = NavyDeepest),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = advice,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = TextPrimary,
                                lineHeight = 22.sp
                            ),
                            modifier = Modifier.padding(14.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = if (language == "hi") "⚠️ नियम: AI कभी प्रत्यक्ष उत्तर नहीं बताता, बल्कि सोचने की तार्किक दिशा देता है।" else "⚠️ Rule: Expert provides deduction coordinates, never the plain answer.",
                        fontSize = 11.sp,
                        color = GoldPrimary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = PurpleAccent),
                modifier = Modifier.testTag("close_expert_dialog_button")
            ) {
                Text(if (language == "hi") "समझ गया (Understood)" else "Understood", color = TextPrimary)
            }
        },
        containerColor = NavyCardElevated,
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
fun FiftyFiftyProofDialog(
    proof: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "50-50 Elimination",
                    tint = InfoCyan,
                    modifier = Modifier.size(26.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "50-50 तार्किक विलोपन (Proof)",
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = InfoCyan,
                        fontWeight = FontWeight.Bold
                    )
                )
            }
        },
        text = {
            Text(
                text = proof,
                style = MaterialTheme.typography.bodyMedium.copy(color = TextPrimary, lineHeight = 20.sp)
            )
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = InfoCyan),
                modifier = Modifier.testTag("close_5050_proof_dialog")
            ) {
                Text("जारी रखें (Continue)", color = NavyDeepest, fontWeight = FontWeight.Bold)
            }
        },
        containerColor = NavyCardElevated,
        shape = RoundedCornerShape(14.dp)
    )
}
