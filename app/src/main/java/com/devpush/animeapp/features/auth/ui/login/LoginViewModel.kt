package com.devpush.animeapp.features.auth.ui.login

import android.util.Patterns
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devpush.animeapp.domian.repository.UserPreferencesRepository
import com.devpush.animeapp.features.auth.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _state = MutableStateFlow(
        LoginState(
            email = savedStateHandle["email"] ?: "",
            password = savedStateHandle["password"] ?: ""
        )
    )
    val state = _state.asStateFlow()

    private val _events = Channel<LoginEvent>()
    val events = _events.receiveAsFlow()

    private var loginJob: Job? = null

    fun onAction(action: LoginAction) {
        when (action) {
            is LoginAction.EmailChanged -> {
                savedStateHandle["email"] = action.email
                _state.update {
                    it.copy(
                        email = action.email,
                        emailError = if (action.email.isNotEmpty() && !isValidEmail(action.email)) "Invalid email format" else null,
                        generalError = null
                    )
                }
            }
            is LoginAction.PasswordChanged -> {
                savedStateHandle["password"] = action.password
                _state.update {
                    it.copy(
                        password = action.password,
                        passwordError = if (action.password.isNotEmpty() && !isValidPassword(action.password)) "Password too short (min 6 chars)" else null,
                        generalError = null
                    )
                }
            }
            is LoginAction.Submit -> login()
        }
    }

    private fun login() {
        loginJob?.cancel()
        val currentEmail = _state.value.email
        val currentPassword = _state.value.password

        val emailValid = isValidEmail(currentEmail)
        val passwordValid = isValidPassword(currentPassword)

        _state.update {
            it.copy(
                emailError = when {
                    currentEmail.isBlank() -> "Email cannot be empty"
                    !emailValid -> "Invalid email format"
                    else -> null
                },
                passwordError = when {
                    currentPassword.isBlank() -> "Password cannot be empty"
                    !passwordValid -> "Password too short (min 6 chars)"
                    else -> null
                },
                generalError = null
            )
        }

        if (!emailValid || !passwordValid || currentEmail.isBlank() || currentPassword.isBlank()) return

        loginJob = viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoading = true, generalError = null) }
            try {
                authRepository.loginCall()
                userPreferencesRepository.updateLoginStatus(true)
                _state.update { it.copy(isLoading = false) }
                _events.send(LoginEvent.LoginSuccess)
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        generalError = "Login failed: ${e.message ?: "Unknown error"}"
                    )
                }
            }
        }
    }

    private fun isValidEmail(email: String) = Patterns.EMAIL_ADDRESS.matcher(email).matches()
    private fun isValidPassword(password: String) = password.length >= 6
}
