package com.devpush.animeapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devpush.animeapp.domian.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel(private val userPreferencesRepository: UserPreferencesRepository) :
    ViewModel() {

    companion object {
        private const val TAG = "MainViewModel"
        private const val SUBSCRIPTION_TIMEOUT = 5_000L
    }

    val isOnboardingShown: StateFlow<Boolean?> = userPreferencesRepository.isOnboardingShownFlow
        .map { value ->
            Timber.tag(TAG).d("Onboarding shown state loaded: $value")
            value
        }
        .stateIn(scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(SUBSCRIPTION_TIMEOUT),
            initialValue = null)

    // Refactored to use proper StateFlow pattern directly from repository
    val isLogin: StateFlow<Boolean?> = userPreferencesRepository.isLoginFlow
        .map { value ->
            Timber.tag(TAG).d("Login state loaded: $value")
            value
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(SUBSCRIPTION_TIMEOUT),
            initialValue = null // Start with null to indicate loading state
        )

    val appTheme: StateFlow<String> = userPreferencesRepository.appThemeFlow
        .map { value ->
            Timber.tag(TAG).d("App theme loaded: $value")
            value
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(SUBSCRIPTION_TIMEOUT), "system")

    val currentLanguage: StateFlow<String> = userPreferencesRepository.appLanguageFlow
        .map { value ->
            Timber.tag(TAG).d("App language loaded: $value")
            value
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(SUBSCRIPTION_TIMEOUT), "en")

    val isBiometricAuthEnabled: StateFlow<Boolean?> =
        userPreferencesRepository.isBiometricAuthEnabledFlow
            .map { value ->
                Timber.tag(TAG).d("Biometric auth enabled state loaded: $value")
                value
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(SUBSCRIPTION_TIMEOUT),
                initialValue = null // Start with null to indicate loading state
            )

    // Fixed isInitialized logic with proper null handling and comprehensive logging
    val isInitialized: StateFlow<Boolean> =
        combine(
            isLogin,
            isBiometricAuthEnabled,
            isOnboardingShown
        ) { loginStatus, biometricStatus, onboardingStatus ->
            val allStatesLoaded = loginStatus != null && 
                                 biometricStatus != null && 
                                 onboardingStatus != null
            
            Timber.tag(TAG).d(
                "State loading check - Login: $loginStatus, Biometric: $biometricStatus, " +
                "Onboarding: $onboardingStatus, All loaded: $allStatesLoaded"
            )
            
            if (allStatesLoaded) {
                Timber.tag(TAG).i("All navigation-critical states loaded successfully")
            }
            
            allStatesLoaded
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(SUBSCRIPTION_TIMEOUT),
            initialValue = false
        )

    init {
        Timber.tag(TAG).d("MainViewModel initialized - starting state loading")
    }

    fun saveIsOnboardingShown(value: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.updateOnboardingShownStatus(value)
        }
    }

}