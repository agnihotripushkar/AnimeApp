package com.devpush.animeapp.features.auth.ui.biometric

enum class BiometricAuthStatus {
    IDLE, // Initial state, waiting to start
    PROMPT_SHOWN, // Prompt is currently displayed
    SUCCESS,
    ERROR, // Authentication failed (recoverable or not)
    NOT_AVAILABLE,
    CANCELLED // User cancelled the prompt
}