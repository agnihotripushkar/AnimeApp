package com.devpush.animeapp.features.auth.ui.login

data class LoginState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val passwordError: String? = null,
    val isLoading: Boolean = false,
    val generalError: String? = null
)

sealed interface LoginAction {
    data class EmailChanged(val email: String) : LoginAction
    data class PasswordChanged(val password: String) : LoginAction
    data object Submit : LoginAction
}

sealed interface LoginEvent {
    data object LoginSuccess : LoginEvent
}
