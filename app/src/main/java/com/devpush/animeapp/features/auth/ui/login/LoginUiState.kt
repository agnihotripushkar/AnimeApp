package com.devpush.animeapp.features.auth.ui.login

data class LoginUiState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val isLoading: Boolean = false,
    val generalLoginError: String? = null,
    val isLoginSuccess: Boolean = false
)
