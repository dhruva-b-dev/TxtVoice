package com.dhruva.txtvoice.data

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VoiceManager @Inject constructor(
    @ApplicationContext private val context: Context
) : RecognitionListener {

    private var speechRecognizer: SpeechRecognizer? = null
    private var tts: TextToSpeech? = null

    private val _transcribedText = MutableStateFlow("")
    val transcribedText: StateFlow<String> = _transcribedText.asStateFlow()

    private val _isListening = MutableStateFlow(false)
    val isListening: StateFlow<Boolean> = _isListening.asStateFlow()

    // Keep track of previously finalized text to prevent duplication
    private var finalizedText = ""
    private val mainHandler = Handler(Looper.getMainLooper())

    init {
        tts = TextToSpeech(context) { status ->
            if (status != TextToSpeech.ERROR) {
                tts?.language = Locale.getDefault()
            }
        }
    }

    private fun getRecognizerIntent(): Intent {
        return Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.packageName)
            // Some devices work better with these for continuous listening
            putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 1000L)
            putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 1000L)
        }
    }

    fun startListening() {
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            Log.e("VoiceManager", "Speech recognition is not available on this device")
            return
        }

        _isListening.value = true
        resetRecognizer()
        speechRecognizer?.startListening(getRecognizerIntent())
    }

    private fun resetRecognizer() {
        speechRecognizer?.destroy()
        speechRecognizer = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            SpeechRecognizer.createOnDeviceSpeechRecognizer(context)
        } else {
            SpeechRecognizer.createSpeechRecognizer(context)
        }
        speechRecognizer?.setRecognitionListener(this)
    }

    fun stopListening() {
        _isListening.value = false
        speechRecognizer?.stopListening()
        speechRecognizer?.cancel()
    }

    fun speak(text: String) {
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    fun clearText() {
        finalizedText = ""
        _transcribedText.value = ""
    }

    override fun onReadyForSpeech(params: Bundle?) {
        Log.d("VoiceManager", "onReadyForSpeech")
    }
    override fun onBeginningOfSpeech() {
        Log.d("VoiceManager", "onBeginningOfSpeech")
    }
    override fun onRmsChanged(rmsdB: Float) {}
    override fun onBufferReceived(buffer: ByteArray?) {}
    override fun onEndOfSpeech() {
        Log.d("VoiceManager", "onEndOfSpeech")
        // Don't set _isListening to false here to allow continuous recognition
    }

    override fun onError(error: Int) {
        val errorMessage = when (error) {
            SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
            SpeechRecognizer.ERROR_CLIENT -> "Client side error"
            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions"
            SpeechRecognizer.ERROR_NETWORK -> "Network error"
            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
            SpeechRecognizer.ERROR_NO_MATCH -> "No match"
            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "RecognitionService busy"
            SpeechRecognizer.ERROR_SERVER -> "Error from server"
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech input"
            else -> "Unknown error: $error"
        }
        Log.e("VoiceManager", "Speech recognition error: $errorMessage")
        
        if (_isListening.value) {
            // Restart if it's a timeout or no match, which happens often in continuous mode
            mainHandler.postDelayed({
                if (_isListening.value) {
                    resetRecognizer()
                    speechRecognizer?.startListening(getRecognizerIntent())
                }
            }, 500)
        }
    }

    override fun onResults(results: Bundle?) {
        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        if (!matches.isNullOrEmpty()) {
            val newText = matches[0]
            finalizedText = if (finalizedText.isEmpty()) newText else "$finalizedText $newText"
            _transcribedText.value = finalizedText
        }

        if (_isListening.value) {
            // Restart recognizer for continuous listening
            mainHandler.post {
                if (_isListening.value) {
                    speechRecognizer?.startListening(getRecognizerIntent())
                }
            }
        }
    }

    override fun onPartialResults(partialResults: Bundle?) {
        val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        if (!matches.isNullOrEmpty()) {
            val partial = matches[0]
            // Show finalized text + the current partial guess
            _transcribedText.value = if (finalizedText.isEmpty()) partial else "$finalizedText $partial"
        }
    }

    override fun onEvent(eventType: Int, params: Bundle?) {}
}
