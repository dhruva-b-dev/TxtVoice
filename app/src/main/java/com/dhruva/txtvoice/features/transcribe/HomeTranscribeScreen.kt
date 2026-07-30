package com.dhruva.txtvoice.features.transcribe

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.SpeakerPhone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalLocale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.dhruva.txtvoice.R
import com.dhruva.txtvoice.core.ui.components.CommonSecondaryButton
import com.dhruva.txtvoice.core.ui.components.CommonYellowButton
import com.dhruva.txtvoice.core.ui.theme.TxtVoiceTheme

@Composable
fun HomeTranscribeScreen(
    modifier: Modifier = Modifier,
    viewModel: TranscribeViewModel = hiltViewModel()
) {

    val isListening by viewModel.isListening.collectAsStateWithLifecycle()
    val transcribedText by viewModel.transcribedText.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val activity = context as? Activity
    val view = LocalView.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var showRationale by rememberSaveable { mutableStateOf(false) }
    var showPermanentDenial by rememberSaveable { mutableStateOf(false) }

    // Stop listening when the app goes to background
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_PAUSE || event == Lifecycle.Event.ON_STOP) {
                if (isListening) {
                    viewModel.toggleListening() // This will call stopListening in VoiceManager
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Keep screen on while listening
    DisposableEffect(isListening) {
        if (isListening) {
            view.keepScreenOn = true
        }
        onDispose {
            view.keepScreenOn = false
        }
    }

    //to stop the mic when screen changes/closes
    DisposableEffect(Unit) {
        onDispose {
            viewModel.clearText()
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                viewModel.toggleListening()
            } else {
                // If not granted, check if we should show rationale or if it's permanently denied
                if (activity != null && !ActivityCompat.shouldShowRequestPermissionRationale(
                        activity,
                        Manifest.permission.RECORD_AUDIO
                    )
                ) {
                    showPermanentDenial = true
                }
            }
        }
    )

    if (showRationale) {
        AlertDialog(
            onDismissRequest = { showRationale = false },
            title = { Text(stringResource(R.string.permission_rationale_title)) },
            text = { Text(stringResource(R.string.permission_rationale_message)) },
            confirmButton = {
                TextButton(onClick = {
                    showRationale = false
                    permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                }) {
                    Text(stringResource(R.string.ok_button_label))
                }
            },
            dismissButton = {
                TextButton(onClick = { showRationale = false }) {
                    Text(stringResource(R.string.cancel_button_label))
                }
            }
        )
    }

    if (showPermanentDenial) {
        AlertDialog(
            onDismissRequest = { showPermanentDenial = false },
            title = { Text(stringResource(R.string.permission_permanent_denial_title)) },
            text = { Text(stringResource(R.string.permission_permanent_denial_message)) },
            confirmButton = {
                TextButton(onClick = {
                    showPermanentDenial = false
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                    context.startActivity(intent)
                }) {
                    Text(stringResource(R.string.open_settings_label))
                }
            },
            dismissButton = {
                TextButton(onClick = { showPermanentDenial = false }) {
                    Text(stringResource(R.string.cancel_button_label))
                }
            }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(12.dp)
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
                    ), onClick = { /* Log cleanup for release */ }, label = {
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
                        leadingIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    ), onClick = { /* Log cleanup for release */ }, label = {
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
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }, shape = RoundedCornerShape(16.dp)
                )

            }

        }

        Box(
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
                .height(350.dp) // Fixed height to handle scrolling better
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline, // Match your text field border color
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
                readOnly = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.primary
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
                    width = 1.dp, color = MaterialTheme.colorScheme.outline,
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
                    val permissionCheckResult =
                        ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
                    if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                        viewModel.toggleListening()
                    } else {
                        if (activity != null && ActivityCompat.shouldShowRequestPermissionRationale(
                                activity,
                                Manifest.permission.RECORD_AUDIO
                            )
                        ) {
                            showRationale = true
                        } else {
                            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                        }
                    }
                }, modifier = Modifier.weight(0.5f)
            )

            CommonSecondaryButton(
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
