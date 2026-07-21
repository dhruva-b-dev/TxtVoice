package com.dhruva.txtvoice.features.speak

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WavingHand
import androidx.compose.material.icons.filled.Wc
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.hilt.navigation.compose.hiltViewModel
import com.dhruva.txtvoice.R
import com.dhruva.txtvoice.core.ui.components.CommonYellowButton
import com.dhruva.txtvoice.core.ui.theme.DarkBackground
import com.dhruva.txtvoice.core.ui.theme.DarkGray
import com.dhruva.txtvoice.core.ui.theme.DarkSurface
import com.dhruva.txtvoice.core.ui.theme.LightGray
import com.dhruva.txtvoice.core.ui.theme.TxtVoiceTheme
import com.dhruva.txtvoice.core.ui.theme.YellowBorder
import com.dhruva.txtvoice.core.ui.theme.YellowPrimary

@Composable
fun SpeakScreen(
    modifier: Modifier = Modifier,
    viewModel: SpeakViewModel = hiltViewModel()
) {
    var textToSpeak by remember { mutableStateOf("") }
    var highlightedLabel by remember { mutableStateOf("") }

    val quickPhrases = listOf(
        QuickPhraseItem(stringResource(R.string.hello_label), Icons.Filled.WavingHand),
        QuickPhraseItem(stringResource(R.string.thank_you_label), Icons.Filled.Favorite),
        QuickPhraseItem(stringResource(R.string.bathroom_label), Icons.Filled.Wc),
        QuickPhraseItem(stringResource(R.string.need_help_label), Icons.Filled.Star),
        QuickPhraseItem(stringResource(R.string.yes_label), Icons.Filled.CheckCircle),
        QuickPhraseItem(stringResource(R.string.no_label), Icons.Filled.Cancel),
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.type_to_speak_label).uppercase(LocalLocale.current.platformLocale),
            color = DarkGray,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = textToSpeak,
            onValueChange = { textToSpeak = it },
            placeholder = { Text(stringResource(R.string.type_placeholder), color = DarkGray) },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            trailingIcon = {
                if (textToSpeak.isNotEmpty()) {
                    IconButton(onClick = { textToSpeak = "" }) {
                        Icon(
                            imageVector = Icons.Filled.Clear,
                            contentDescription = stringResource(R.string.clear_label),
                            tint = DarkGray
                        )
                    }
                }
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = LightGray,
                unfocusedBorderColor = YellowBorder,
                focusedTextColor = LightGray,
                unfocusedTextColor = LightGray,
                cursorColor = YellowPrimary
            ),
            shape = RoundedCornerShape(4.dp)
        )

        CommonYellowButton(
            leadingIcon = Icons.AutoMirrored.Filled.VolumeUp,
            text = R.string.speak_label,
            onButtonClick = {
                viewModel.speak(textToSpeak)
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .height(56.dp)
        )

        Text(
            text = stringResource(R.string.quick_phrases_label).uppercase(LocalLocale.current.platformLocale),
            color = DarkGray,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(bottom = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(quickPhrases) { phrase ->
                val isHighlighted = highlightedLabel == phrase.label
                QuickPhraseCard(
                    phrase = phrase,
                    isHighlighted = isHighlighted,
                    onClick = {
                        highlightedLabel = phrase.label
                        viewModel.speak(phrase.label)
                    }
                )
            }
        }
    }
}

data class QuickPhraseItem(
    val label: String,
    val icon: ImageVector
)

@Composable
fun QuickPhraseCard(
    phrase: QuickPhraseItem,
    isHighlighted: Boolean,
    onClick: () -> Unit
) {
    val borderColor = if (isHighlighted) Color(0xFFFFB4AB) else Color.Transparent
    val labelColor = if (isHighlighted) Color(0xFFFFB4AB) else LightGray
    val iconColor = if (isHighlighted) Color(0xFFFFB4AB) else LightGray

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .border(
                width = if (isHighlighted) 1.dp else 0.dp,
                color = borderColor,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = DarkSurface
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = phrase.icon,
                contentDescription = phrase.label,
                tint = iconColor,
                modifier = Modifier.size(28.dp)
            )
            Text(
                text = phrase.label,
                color = labelColor,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SpeakScreenPreview() {
    TxtVoiceTheme {
        SpeakScreen()
    }
}
