package com.dhruva.txtvoice.features.transcribe

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.SpeakerPhone
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.dhruva.txtvoice.R
import com.dhruva.txtvoice.core.ui.components.CommonBlackButton
import com.dhruva.txtvoice.core.ui.components.CommonYellowButton
import com.dhruva.txtvoice.core.ui.theme.DarkBackground
import com.dhruva.txtvoice.core.ui.theme.DarkGray
import com.dhruva.txtvoice.core.ui.theme.LightGray
import com.dhruva.txtvoice.core.ui.theme.Mustard
import com.dhruva.txtvoice.core.ui.theme.TxtVoiceTheme
import com.dhruva.txtvoice.core.ui.theme.YellowBorder
import com.dhruva.txtvoice.core.ui.theme.YellowPrimary

@Composable
fun HomeTranscribeScreen(
    modifier: Modifier = Modifier,
    viewModel: TranscribeViewModel = hiltViewModel()
) {

    val isListening by viewModel.isListening.collectAsStateWithLifecycle()
    val transcribedText by viewModel.transcribedText.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                viewModel.toggleListening()
            }
        }
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(12.dp)
            .background(DarkBackground)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            if (isListening) {
                AssistChip(
                    colors = AssistChipDefaults.assistChipColors(
                        leadingIconContentColor = MaterialTheme.colorScheme.onPrimary,
                        labelColor = MaterialTheme.colorScheme.onPrimary,
                        containerColor = MaterialTheme.colorScheme.primary
                    ), onClick = { Log.d("Assist chip", "Listening...") }, label = {
                        Text(
                            text = stringResource(R.string.listening_label).uppercase(
                                LocalLocale.current.platformLocale
                            ),
                            fontWeight = FontWeight.Bold
                        )
                    }, leadingIcon = {
                        Icon(
                            Icons.Filled.FiberManualRecord,
                            contentDescription = stringResource(R.string.listening_label),
                            Modifier.size(AssistChipDefaults.IconSize)
                        )
                    }, shape = RoundedCornerShape(16.dp)
                )
            } else {
                AssistChip(
                    colors = AssistChipDefaults.assistChipColors(
                        leadingIconContentColor = Mustard,
                        labelColor = LightGray,
                        containerColor = DarkGray
                    ), onClick = { Log.d("Assist chip", "Paused...") }, label = {
                        Text(
                            text = stringResource(R.string.paused_label).uppercase(
                                LocalLocale.current.platformLocale
                            ),
                            fontWeight = FontWeight.Bold
                        )
                    }, leadingIcon = {
                        Icon(
                            Icons.Filled.FiberManualRecord,
                            contentDescription = stringResource(R.string.listening_label),
                            Modifier.size(AssistChipDefaults.IconSize),
                            tint = YellowPrimary
                        )
                    }, shape = RoundedCornerShape(16.dp)
                )

            }

        }

        Box(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .weight(0.6f)
                .border(
                    width = 1.dp, color = YellowBorder, // Match your text field border color
                    shape = RoundedCornerShape(4.dp) // Match your text field corner radius
                ),

            contentAlignment = Alignment.Center
        ) {
            OutlinedTextField(
                value = transcribedText.ifEmpty { stringResource(R.string.transcribe_default_val) },
                onValueChange = {},
                label = { Text("") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = false,
                maxLines = 100,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DarkBackground,
                    unfocusedBorderColor = DarkBackground,
                    focusedTextColor = LightGray,
                    unfocusedTextColor = DarkGray,
                    focusedContainerColor = DarkBackground,
                    unfocusedContainerColor = DarkBackground,
                    cursorColor = YellowPrimary
                )
            )
        }

        val composition by rememberLottieComposition(
            LottieCompositionSpec.RawRes(R.raw.audio_wave)
        )

        val progress by animateLottieCompositionAsState(
            composition = composition,
            isPlaying = isListening,
            iterations = LottieConstants.IterateForever
        )

        Box(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .height(100.dp)
                .border(
                    width = 1.dp, color = YellowBorder,
                    shape = RoundedCornerShape(4.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            LottieAnimation(
                composition = composition, progress = { progress })
        }

        val yellowButtonLabel = if (isListening) R.string.pause_label else R.string.speak_label
        val yellowButtonIcon =
            if (isListening) Icons.Filled.Pause else Icons.Default.SpeakerPhone

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {

            CommonYellowButton(
                leadingIcon = yellowButtonIcon, text = yellowButtonLabel, onButtonClick = {
                    val permissionCheckResult = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
                    if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                        viewModel.toggleListening()
                    } else {
                        permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                    }
                }, modifier = Modifier.weight(0.5f)
            )

            CommonBlackButton(
                leadingIcon = Icons.Default.Clear,
                text = R.string.clear_label,
                onButtonClick = {
                    viewModel.clearText()
                },
                modifier = Modifier.weight(0.5f)
            )
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun HomeTranscribeScreenPreview() {
    TxtVoiceTheme {
        HomeTranscribeScreen()
    }
}
