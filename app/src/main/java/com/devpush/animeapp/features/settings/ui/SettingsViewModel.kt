package com.devpush.animeapp.features.settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devpush.animeapp.domian.repository.UserPreferencesRepository
import com.devpush.animeapp.features.auth.ui.biometric.BiometricAuthStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val userPreferencesRepository: UserPreferencesRepository): ViewModel() {

    private val _authStatus = MutableStateFlow(BiometricAuthStatus.IDLE)
    val authStatus: StateFlow<BiometricAuthStatus> = _authStatus.asStateFlow()

    fun updateAuthStatus(status: BiometricAuthStatus) {
        _authStatus.value = status
    }


    val appTheme: StateFlow<String> = userPreferencesRepository.appThemeFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "system"
        )

    val appLanguage: StateFlow<String> = userPreferencesRepository.appLanguageFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "en"
        )

    val isBiometricAuthEnabled: StateFlow<Boolean> = userPreferencesRepository.isBiometricAuthEnabledFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )

    fun setAppTheme(theme: String) {
        viewModelScope.launch(Dispatchers.IO) {
            userPreferencesRepository.updateAppTheme(theme)
        }
    }

    fun setAppLanguage(language: String) {
        viewModelScope.launch(Dispatchers.IO) {
            userPreferencesRepository.updateAppLanguage(language)
        }
    }

    fun setBiometricAuthEnabled(enabled: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            userPreferencesRepository.updateBiometricAuthEnabled(enabled)
        }
    }

    fun performLogout() {
        viewModelScope.launch(Dispatchers.IO) {
            userPreferencesRepository.updateLoginStatus(false)
        }
    }

}