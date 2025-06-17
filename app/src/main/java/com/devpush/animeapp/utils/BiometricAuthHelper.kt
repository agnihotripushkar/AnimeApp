package com.devpush.animeapp.utils

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.devpush.animeapp.R


class BiometricAuthHelper(
    private val context: Context,
    private val listener: BiometricAuthListener
) {

    private val biometricManager = BiometricManager.from(context)

    fun canAuthenticate(): Boolean {
        val authenticators =
            BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL
        val result = biometricManager.canAuthenticate(authenticators)
        return result == BiometricManager.BIOMETRIC_SUCCESS
    }

    fun promptBiometricAuth(activity: FragmentActivity) {
        if (!canAuthenticate()) {
            listener.onBiometricAuthError(
                BiometricPrompt.ERROR_NO_BIOMETRICS,
                "Biometric authentication is not available."
            )
            return
        }

        val executor = ContextCompat.getMainExecutor(context)
        val biometricPrompt = BiometricPrompt(activity, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    listener.onBiometricAuthError(errorCode, errString)
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    listener.onBiometricAuthSuccess()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    listener.onBiometricAuthFailed()
                }
            })

        val authenticators =
            BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL

        val promptInfoBuilder = BiometricPrompt.PromptInfo.Builder()
            .setTitle(context.getString(R.string.biometric_auth_title))
            .setSubtitle(context.getString(R.string.biometric_auth_subtitle))
            .setAllowedAuthenticators(authenticators)

        val promptInfo = promptInfoBuilder.build()
        biometricPrompt.authenticate(promptInfo)
    }
}
