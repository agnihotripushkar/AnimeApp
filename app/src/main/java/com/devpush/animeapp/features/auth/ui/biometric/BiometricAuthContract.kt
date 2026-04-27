package com.devpush.animeapp.features.auth.ui.biometric

data class BiometricAuthState(
    val authStatus: BiometricAuthStatus = BiometricAuthStatus.IDLE
)

sealed interface BiometricAuthAction {
    data object Retry : BiometricAuthAction
    data class StatusChanged(val status: BiometricAuthStatus) : BiometricAuthAction
}

sealed interface BiometricAuthEvent {
    data object NavigateToHome : BiometricAuthEvent
    data object NavigateToOnboarding : BiometricAuthEvent
    data object NavigateToLogin : BiometricAuthEvent
}
