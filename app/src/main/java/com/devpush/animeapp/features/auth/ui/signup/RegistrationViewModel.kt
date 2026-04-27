package com.devpush.animeapp.features.auth.ui.signup

import android.util.Patterns
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devpush.animeapp.domian.repository.UserPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegistrationViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _state = MutableStateFlow(
        RegistrationState(
            email = savedStateHandle["email"] ?: "",
            password = savedStateHandle["password"] ?: "",
            confirmPassword = savedStateHandle["confirmPassword"] ?: ""
        )
    )
    val state = _state.asStateFlow()

    private val _events = Channel<RegistrationEvent>()
    val events = _events.receiveAsFlow()

    private var registrationJob: Job? = null

    fun onAction(action: RegistrationAction) {
        when (action) {
            is RegistrationAction.EmailChanged -> {
                savedStateHandle["email"] = action.email
                _state.update {
                    it.copy(
                        email = action.email,
                        emailError = if (action.email.isNotEmpty() && !isValidEmail(action.email)) "Invalid email format" else null,
                        generalError = null
                    )
                }
            }
            is RegistrationAction.PasswordChanged -> {
                savedStateHandle["password"] = action.password
                _state.update { current ->
                    val confirmError = if (action.password != current.confirmPassword && current.confirmPassword.isNotEmpty()) "Passwords do not match" else null
                    current.copy(
                        password = action.password,
                        passwordError = if (action.password.isNotEmpty() && !isValidPassword(action.password)) "Password too short (min 6 chars)" else null,
                        confirmPasswordError = confirmError,
                        generalError = null
                    )
                }
            }
            is RegistrationAction.ConfirmPasswordChanged -> {
                savedStateHandle["confirmPassword"] = action.confirmPassword
                _state.update { current ->
                    current.copy(
                        confirmPassword = action.confirmPassword,
                        confirmPasswordError = if (current.password.isNotEmpty() && action.confirmPassword != current.password) "Passwords do not match" else null,
                        generalError = null
                    )
                }
            }
            is RegistrationAction.Submit -> register()
        }
    }

    private fun register() {
        registrationJob?.cancel()
        val current = _state.value

        val emailValid = isValidEmail(current.email)
        val passwordValid = isValidPassword(current.password)
        val passwordsMatch = current.password == current.confirmPassword

        _state.update {
            it.copy(
                emailError = if (!emailValid || current.email.isBlank()) "Invalid email format" else null,
                passwordError = if (!passwordValid || current.password.isBlank()) "Password too short (min 6 chars)" else null,
                confirmPasswordError = when {
                    current.confirmPassword.isBlank() -> "Confirm password"
                    !passwordsMatch -> "Passwords do not match"
                    else -> null
                },
                generalError = null
            )
        }

        if (!emailValid || current.email.isBlank() || !passwordValid || current.password.isBlank() || current.confirmPassword.isBlank() || !passwordsMatch) return

        registrationJob = viewModelScope.launch(Dispatchers.IO) {
            _state.update { it.copy(isLoading = true, generalError = null) }
            try {
                delay(2000)
                userPreferencesRepository.updateLoginStatus(true)
                userPreferencesRepository.updateOnboardingShownStatus(false)
                _state.update { it.copy(isLoading = false) }
                _events.send(RegistrationEvent.RegistrationSuccess)
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        generalError = "Registration failed: ${e.message ?: "Unknown error"}"
                    )
                }
            }
        }
    }

    private fun isValidEmail(email: String) = Patterns.EMAIL_ADDRESS.matcher(email).matches()
    private fun isValidPassword(password: String) = password.length >= 6
}
