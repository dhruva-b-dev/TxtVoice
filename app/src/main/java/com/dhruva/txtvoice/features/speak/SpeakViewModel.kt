package com.dhruva.txtvoice.features.speak

import androidx.lifecycle.ViewModel
import com.dhruva.txtvoice.core.speech.VoiceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SpeakViewModel @Inject constructor(
    private val voiceManager: VoiceManager
) : ViewModel() {

    fun speak(text: String) {
        if (text.isNotBlank()) {
            voiceManager.speak(text)
        }
    }
}
