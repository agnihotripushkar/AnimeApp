package com.devpush.animeapp.features.settings.ui

import com.devpush.animeapp.features.auth.ui.biometric.BiometricAuthStatus

data class SettingsState(
    val appTheme: String = "system",
    val appLanguage: String = "en",
    val isBiometricAuthEnabled: Boolean = false,
    val authStatus: BiometricAuthStatus = BiometricAuthStatus.IDLE
)

sealed interface SettingsAction {
    data class SetTheme(val theme: String) : SettingsAction
    data class SetLanguage(val language: String) : SettingsAction
    data class SetBiometricEnabled(val enabled: Boolean) : SettingsAction
    data class UpdateAuthStatus(val status: BiometricAuthStatus) : SettingsAction
    data object Logout : SettingsAction
}

sealed interface SettingsEvent {
    data object NavigateToLogin : SettingsEvent
}
