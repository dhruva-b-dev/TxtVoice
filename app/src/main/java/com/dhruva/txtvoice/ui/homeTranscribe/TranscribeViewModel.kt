package com.dhruva.txtvoice.ui.homeTranscribe

import androidx.lifecycle.ViewModel
import com.dhruva.txtvoice.data.VoiceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TranscribeViewModel @Inject constructor(
    private val voiceManager: VoiceManager
) : ViewModel() {

    val transcribedText = voiceManager.transcribedText
    val isListening = voiceManager.isListening

    fun toggleListening() {
        if (isListening.value) {
            voiceManager.stopListening()
        } else {
            voiceManager.startListening()
        }
    }

    fun clearText() {
        voiceManager.clearText()
    }
}
