package com.dhruva.txtvoice.core.navigation

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.navigation3.runtime.NavKey
import com.dhruva.txtvoice.R
import kotlinx.serialization.Serializable

@Serializable
sealed interface TxtVoiceNavigationKeys : NavKey {
    // Non-serializable properties must be marked @Transient
    // Declared as plain properties (no annotations needed here)
    @get:DrawableRes
    val icon: Int

    @get:StringRes
    val label: Int
}

@Serializable
data object HomeTranscribeRoute : TxtVoiceNavigationKeys {
    override val icon: Int = R.drawable.transcribe
    override val label: Int = R.string.transcribe_label
}

//@Serializable
//data object SettingsRoute : TxtVoiceNavigationKeys {
//    override val icon: Int = R.drawable.settings
//    override val label: Int = R.string.settings_label
//}

@Serializable
data object SpeakRoute : TxtVoiceNavigationKeys {
    override val icon: Int = R.drawable.speak
    override val label: Int = R.string.speak_label
}

//@Serializable
//data object HistoryRoute : TxtVoiceNavigationKeys {
//    override val icon: Int = R.drawable.history
//    override val label: Int = R.string.history_label
//}
