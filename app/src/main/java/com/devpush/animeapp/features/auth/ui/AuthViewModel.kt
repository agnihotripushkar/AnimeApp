package com.devpush.animeapp.features.auth.ui

import android.util.Patterns
import android.app.Application
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.devpush.animeapp.domian.repository.UserPreferencesRepository
import com.devpush.animeapp.features.auth.domain.repository.AuthRepository
import com.devpush.animeapp.utils.BiometricAuthHelper
import com.devpush.animeapp.utils.BiometricAuthListener
import com.devpush.animeapp.features.auth.ui.login.LoginUiState
import com.devpush.animeapp.features.auth.ui.signup.RegistrationUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    application: Application,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val authRepository: AuthRepository
) : AndroidViewModel(application), BiometricAuthListener {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()
    private var loginJob: Job? = null

    private val _registrationUiState = MutableStateFlow(RegistrationUiState())
    val registrationUiState: StateFlow<RegistrationUiState> = _registrationUiState.asStateFlow()
    private var registrationJob: Job? = null

    private val _triggerBiometricPromptEvent = MutableSharedFlow<Unit>(replay = 0)
    val triggerBiometricPromptEvent = _triggerBiometricPromptEvent.asSharedFlow()

    private lateinit var biometricAuthHelper: BiometricAuthHelper

    init {
        // Initialize BiometricAuthHelper here if activity is not immediately available
        // Or ensure onLoginScreenLaunched is called to initialize it
    }

    fun onLoginScreenLaunched(activity: FragmentActivity) {
        // It's important that BiometricAuthHelper is initialized with the activity context for the prompt
        // but uses application context for BiometricManager if needed for checks outside activity lifecycle.
        // For simplicity, and since promptBiometricAuth takes activity, using application context for helper instance is fine.
        biometricAuthHelper = BiometricAuthHelper(getApplication<Application>().applicationContext, this)
        viewModelScope.launch {
            val isBiometricEnabled = userPreferencesRepository.isBiometricAuthEnabledFlow.first()
            val canAuthResult = biometricAuthHelper.canAuthenticate()

            if (isBiometricEnabled && canAuthResult) {
                _triggerBiometricPromptEvent.emit(Unit)
            }
        }
    }

    fun startBiometricAuthentication(activity: FragmentActivity) {
        if (!::biometricAuthHelper.isInitialized) {
            // Initialize if not already, though onLoginScreenLaunched should handle this.
            biometricAuthHelper = BiometricAuthHelper(getApplication<Application>().applicationContext, this)
        }

        if (biometricAuthHelper.canAuthenticate()) {
            biometricAuthHelper.promptBiometricAuth(activity)
        } else {
            _uiState.update { it.copy(isLoading = false, generalLoginError = "Biometric authentication not available.") }
        }
    }

    override fun onBiometricAuthSuccess() {
        // Similar to successful password login
        viewModelScope.launch(Dispatchers.IO) {
            userPreferencesRepository.updateLoginStatus(true)
            _uiState.update { it.copy(isLoading = false, isLoginSuccess = true) }
        }
    }

    override fun onBiometricAuthError(errorCode: Int, errString: CharSequence) {
        _uiState.update { it.copy(isLoading = false, generalLoginError = "Biometric Auth Error: $errString") }
    }

    override fun onBiometricAuthFailed() {
        _uiState.update { it.copy(isLoading = false, generalLoginError = "Biometric authentication failed.") }
    }

    fun onEmailChanged(email: String) {
        _uiState.update { currentState ->
            currentState.copy(
                email = email,
                emailError = if (email.isNotEmpty() && !isValidEmail(email)) "Invalid email format" else null,
                generalLoginError = null
            )
        }
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { currentState ->
            currentState.copy(
                password = password,
                passwordError = if (password.isNotEmpty() && !isValidPassword(password)) "Password too short (min 6 chars)" else null,
                generalLoginError = null
            )
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun isValidPassword(password: String): Boolean {
        return password.length >= 6
    }

    // --- Registration Actions ---
    fun onRegistrationEmailChanged(email: String) {
        _registrationUiState.update { currentState ->
            currentState.copy(
                email = email,
                emailError = if (email.isNotEmpty() && !isValidEmail(email)) "Invalid email format" else null,
                generalRegistrationError = null
            )
        }
    }

    fun onRegistrationPasswordChanged(password: String) {
        _registrationUiState.update { currentState ->
            val confirmPasswordError = if (password != currentState.confirmPassword && currentState.confirmPassword.isNotEmpty()) {
                "Passwords do not match"
            } else null
            currentState.copy(
                password = password,
                passwordError = if (password.isNotEmpty() && !isValidPassword(password)) "Password too short (min 6 chars)" else null,
                confirmPasswordError = confirmPasswordError, // Check matching if confirm password was already typed
                generalRegistrationError = null
            )
        }
    }

    fun onConfirmPasswordChanged(confirmPassword: String) {
        _registrationUiState.update { currentState ->
            val error = if (currentState.password.isNotEmpty() && confirmPassword != currentState.password) {
                "Passwords do not match"
            } else null
            currentState.copy(
                confirmPassword = confirmPassword,
                confirmPasswordError = error,
                generalRegistrationError = null
            )
        }
    }


    fun loginUser() {
        // Cancel any existing login job
        loginJob?.cancel()

        val currentEmail = _uiState.value.email
        val currentPassword = _uiState.value.password

        // Perform final validation on attempt to log in
        val isEmailFieldValid = isValidEmail(currentEmail)
        val isPasswordFieldValid = isValidPassword(currentPassword)

        _uiState.update {
            it.copy(
                emailError = if (!isEmailFieldValid) "Invalid email format" else null,
                passwordError = if (!isPasswordFieldValid) "Password too short (min 6 chars)" else if (currentPassword.isBlank()) "Password cannot be empty" else null,
                generalLoginError = null // Clear previous general errors
            )
        }

        if (!isEmailFieldValid || !isPasswordFieldValid || currentPassword.isBlank() || currentEmail.isBlank()) {
            if (currentEmail.isBlank()) _uiState.update { it.copy(emailError = "Email cannot be empty") }
            if (currentPassword.isBlank()) _uiState.update { it.copy(passwordError = "Password cannot be empty") }
            return // Don't proceed if basic validation fails
        }

        loginJob = viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true, generalLoginError = null) }
            try {
                // Simulate API call
                // val loginResponse = authRepository.loginCall(currentEmail, currentPassword)
                authRepository.loginCall()

                // if (loginResponse.isSuccessful) {
                userPreferencesRepository.updateLoginStatus(true)
                _uiState.update { it.copy(isLoading = false, isLoginSuccess = true) }
                // } else {
                //    _uiState.update { it.copy(isLoading = false, generalLoginError = "Login failed: ${loginResponse.message}") }
                // }
            } catch (e: Exception) {
                _uiState.update { it.copy(isLoading = false, generalLoginError = "Login failed: ${e.message ?: "Unknown error"}") }
            }
        }
    }

    // Call this after navigation has happened
    fun onLoginHandled() {
        _uiState.update { it.copy(isLoginSuccess = false, generalLoginError = null) }
    }

    fun registerUser() {
        registrationJob?.cancel()
        val currentState = _registrationUiState.value

        val isEmailValidReg = isValidEmail(currentState.email)
        val isPasswordValidReg = isValidPassword(currentState.password)
        val doPasswordsMatch = currentState.password == currentState.confirmPassword

        _registrationUiState.update {
            it.copy(
                emailError = if (!isEmailValidReg) "Invalid email format" else if (currentState.email.isBlank()) "Email cannot be empty" else null,
                passwordError = if (!isPasswordValidReg) "Password too short (min 6 chars)" else if (currentState.password.isBlank()) "Password cannot be empty" else null,
                confirmPasswordError = if (!doPasswordsMatch) "Passwords do not match" else if (currentState.confirmPassword.isBlank()) "Confirm password" else null,
                generalRegistrationError = null
            )
        }

        if (!isEmailValidReg || currentState.email.isBlank() ||
            !isPasswordValidReg || currentState.password.isBlank() ||
            currentState.confirmPassword.isBlank() || !doPasswordsMatch) {
            return
        }

        registrationJob = viewModelScope.launch(Dispatchers.IO) {
            _registrationUiState.update { it.copy(isLoading = true, generalRegistrationError = null) }
            try {
                // Simulate API call for registration
                // val registrationResponse = authRepository.registerCall(currentState.email, currentState.password)
                delay(2000)

                // if (registrationResponse.isSuccessful) {
                userPreferencesRepository.updateLoginStatus(true)
                userPreferencesRepository.updateOnboardingShownStatus(false)
                _registrationUiState.update { it.copy(isLoading = false, isRegistrationSuccess = true) }
                // } else {
                //    _registrationUiState.update { it.copy(isLoading = false, generalRegistrationError = "Registration failed: ${registrationResponse.message}") }
                // }
            } catch (e: Exception) {
                _registrationUiState.update { it.copy(isLoading = false, generalRegistrationError = "Registration failed: ${e.message ?: "Unknown error"}") }
            }
        }
    }

    fun onRegistrationHandled() {
        _registrationUiState.update { it.copy(isRegistrationSuccess = false, generalRegistrationError = null) }
    }

}