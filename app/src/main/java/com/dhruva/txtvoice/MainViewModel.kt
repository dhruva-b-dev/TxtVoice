package com.dhruva.txtvoice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dhruva.txtvoice.core.data.PreferenceManager
import com.dhruva.txtvoice.core.navigation.HomeTranscribeRoute
import com.dhruva.txtvoice.core.navigation.OnboardingRoute
import com.dhruva.txtvoice.core.navigation.TxtVoiceNavigationKeys
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val preferenceManager: PreferenceManager
) : ViewModel() {

    private val _startDestination = MutableStateFlow<TxtVoiceNavigationKeys?>(null)
    val startDestination: StateFlow<TxtVoiceNavigationKeys?> = _startDestination.asStateFlow()

    private val _showRatingDialog = MutableStateFlow(false)
    val showRatingDialog: StateFlow<Boolean> = _showRatingDialog.asStateFlow()

    init {
        viewModelScope.launch {
            preferenceManager.incrementLaunchCount()
        }

        preferenceManager.isOnboardingCompleted
            .onEach { completed ->
                _startDestination.value = if (completed) HomeTranscribeRoute else OnboardingRoute
            }
            .launchIn(viewModelScope)

        combine(
            preferenceManager.appLaunchCount,
            preferenceManager.hasRatedApp
        ) { count, hasRated ->
            // Show prompt exactly on the 5th launch if not rated before
            count == 5 && !hasRated
        }.onEach { show ->
            _showRatingDialog.value = show
        }.launchIn(viewModelScope)
    }

    fun dismissRatingDialog() {
        _showRatingDialog.value = false
    }

    fun markAsRated() {
        viewModelScope.launch {
            preferenceManager.setHasRatedApp(true)
            _showRatingDialog.value = false
        }
    }
}
