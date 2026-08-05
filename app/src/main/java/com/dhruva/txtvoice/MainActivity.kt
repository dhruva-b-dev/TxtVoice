package com.dhruva.txtvoice

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dhruva.txtvoice.core.navigation.TextVoiceAppNavigation
import com.dhruva.txtvoice.core.ui.theme.TxtVoiceTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: MainViewModel = hiltViewModel()
            val startDestination by viewModel.startDestination.collectAsStateWithLifecycle()
            val showRatingDialog by viewModel.showRatingDialog.collectAsStateWithLifecycle()
            val context = LocalContext.current

            TxtVoiceTheme {
                if (showRatingDialog) {
                    AlertDialog(
                        onDismissRequest = { viewModel.dismissRatingDialog() },
                        title = { Text(stringResource(R.string.rating_dialog_title)) },
                        text = { Text(stringResource(R.string.rating_dialog_message)) },
                        confirmButton = {
                            TextButton(onClick = {
                                viewModel.markAsRated()
                                val intent = Intent(Intent.ACTION_VIEW).apply {
                                    data = Uri.parse("market://details?id=${context.packageName}")
                                }
                                try {
                                    context.startActivity(intent)
                                } catch (e: Exception) {
                                    val browserIntent = Intent(Intent.ACTION_VIEW).apply {
                                        data = Uri.parse("https://play.google.com/store/apps/details?id=${context.packageName}")
                                    }
                                    context.startActivity(browserIntent)
                                }
                            }) {
                                Text(stringResource(R.string.rating_dialog_rate_now))
                            }
                        },
                        dismissButton = {
                            Row {
                                TextButton(onClick = { viewModel.markAsRated() }) {
                                    Text(stringResource(R.string.rating_dialog_never))
                                }
                                TextButton(onClick = { viewModel.dismissRatingDialog() }) {
                                    Text(stringResource(R.string.rating_dialog_later))
                                }
                            }
                        },
                        shape = RoundedCornerShape(16.dp)
                    )
                }

                startDestination?.let { destination ->
                    TextVoiceAppNavigation(
                        modifier = Modifier.fillMaxSize(),
                        startDestination = destination
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TxtVoiceTheme {
        Greeting("Android")
    }
}