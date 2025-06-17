package com.devpush.animeapp.utils

interface BiometricAuthListener {
    fun onBiometricAuthSuccess()
    fun onBiometricAuthError(errorCode: Int, errString: CharSequence)
    fun onBiometricAuthFailed()
}