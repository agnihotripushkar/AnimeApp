package com.devpush.animeapp.features.auth.ui.signup

data class RegistrationUiState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val confirmPassword: String = "",
    val confirmPasswordError: String? = null,
    val isLoading: Boolean = false,
    val generalRegistrationError: String? = null,
    val isRegistrationSuccess: Boolean = false
)
