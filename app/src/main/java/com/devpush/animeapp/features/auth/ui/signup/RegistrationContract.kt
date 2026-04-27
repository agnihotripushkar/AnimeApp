package com.devpush.animeapp.features.auth.ui.signup

data class RegistrationState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val confirmPassword: String = "",
    val confirmPasswordError: String? = null,
    val isLoading: Boolean = false,
    val generalError: String? = null
)

sealed interface RegistrationAction {
    data class EmailChanged(val email: String) : RegistrationAction
    data class PasswordChanged(val password: String) : RegistrationAction
    data class ConfirmPasswordChanged(val confirmPassword: String) : RegistrationAction
    data object Submit : RegistrationAction
}

sealed interface RegistrationEvent {
    data object RegistrationSuccess : RegistrationEvent
}
