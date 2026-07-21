package com.dhruva.txtvoice.features.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.PrivacyTip
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dhruva.txtvoice.R
import com.dhruva.txtvoice.core.ui.theme.DarkBackground
import com.dhruva.txtvoice.core.ui.theme.YellowPrimary
import com.dhruva.txtvoice.core.ui.theme.DarkGray
import com.dhruva.txtvoice.core.ui.theme.DarkSurface
import com.dhruva.txtvoice.core.ui.theme.LightGray
import com.dhruva.txtvoice.core.ui.theme.TxtVoiceTheme

@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    var textSize by remember { mutableFloatStateOf(1f) }
    var speechSpeed by remember { mutableFloatStateOf(1f) }
    var highContrast by remember { mutableStateOf(true) }
    var visualHaptics by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(scrollState)
            .padding(16.dp)
    ) {
        SettingsSectionHeader(
            icon = Icons.Default.AccessibilityNew,
            title = stringResource(R.string.accessibility_label),
            color = YellowPrimary
        )

        Card(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                SettingSliderItem(
                    label = stringResource(R.string.transcription_text_size_label),
                    value = textSize,
                    valueLabel = "${(textSize * 100).toInt()}%",
                    onValueChange = { textSize = it }
                )
                Spacer(modifier = Modifier.height(16.dp))
                SettingSliderItem(
                    label = stringResource(R.string.speech_speed_label),
                    value = speechSpeed,
                    valueLabel = "${String.format("%.1f", speechSpeed)}x",
                    onValueChange = { speechSpeed = it }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = stringResource(R.string.voice_selection_label),
                    color = LightGray,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
                VoiceSelectionCard()
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        SettingsSectionHeader(
            icon = Icons.Default.Visibility,
            title = stringResource(R.string.display_feedback_label),
            color = YellowPrimary
        )

        Card(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                SettingToggleItem(
                    label = stringResource(R.string.high_contrast_mode_label),
                    description = stringResource(R.string.high_contrast_mode_desc),
                    checked = highContrast,
                    onCheckedChange = { highContrast = it }
                )
                Spacer(modifier = Modifier.height(16.dp))
                SettingToggleItem(
                    label = stringResource(R.string.visual_haptics_label),
                    description = stringResource(R.string.visual_haptics_desc),
                    checked = visualHaptics,
                    onCheckedChange = { visualHaptics = it }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        InfoSection(
            icon = Icons.Default.PrivacyTip,
            title = stringResource(R.string.private_secure_label),
            description = stringResource(R.string.private_secure_desc),
            color = Color(0xFF8BA2B5)
        )

        Spacer(modifier = Modifier.height(16.dp))

        InfoSection(
            icon = Icons.Default.Bolt,
            title = stringResource(R.string.high_fidelity_label),
            description = stringResource(R.string.high_fidelity_desc),
            color = Color(0xFF8BA2B5)
        )
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun SettingsSectionHeader(icon: ImageVector, title: String, color: Color) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = title.uppercase(LocalLocale.current.platformLocale),
            color = color,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )
    }
}

@Composable
fun SettingSliderItem(label: String, value: Float, valueLabel: String, onValueChange: (Float) -> Unit) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, color = LightGray, style = MaterialTheme.typography.bodyMedium)
            Text(text = valueLabel, color = YellowPrimary, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
        }
        Slider(
            value = value,
            onValueChange = onValueChange,
            colors = SliderDefaults.colors(
                thumbColor = YellowPrimary,
                activeTrackColor = YellowPrimary,
                inactiveTrackColor = Color(0xFF24292F)
            )
        )
    }
}

@Composable
fun VoiceSelectionCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF101217)),
        border = BorderStroke(1.dp, Color(0xFF24292F)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = "Neutral Clarity", color = LightGray, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                Text(text = "Default System Voice", color = DarkGray, style = MaterialTheme.typography.labelSmall)
            }
            Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null, tint = LightGray)
        }
    }
}

@Composable
fun SettingToggleItem(label: String, description: String, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, color = LightGray, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
            Text(text = description, color = DarkGray, style = MaterialTheme.typography.labelSmall)
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.Black,
                checkedTrackColor = YellowPrimary,
                uncheckedThumbColor = Color.White,
                uncheckedTrackColor = Color(0xFF24292F),
                uncheckedBorderColor = Color.Transparent
            )
        )
    }
}

@Composable
fun InfoSection(icon: ImageVector, title: String, description: String, color: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = title, color = color, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = description,
                color = DarkGray,
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 20.sp
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SettingsScreenPreview() {
    TxtVoiceTheme {
        SettingsScreen()
    }
}
