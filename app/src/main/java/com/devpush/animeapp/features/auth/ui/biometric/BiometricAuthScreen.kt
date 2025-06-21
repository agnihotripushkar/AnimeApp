package com.devpush.animeapp.features.auth.ui.biometric

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.devpush.animeapp.R
import com.devpush.animeapp.features.auth.ui.AuthViewModel
import org.koin.androidx.compose.koinViewModel
import timber.log.Timber

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun BiometricAuthScreen(
    viewModel: AuthViewModel = koinViewModel(),
    onAuthSuccess: () -> Unit,
    onAuthCancelledOrFailed: () -> Unit // For now, a single callback for cancel/failure
) {

    val context = LocalContext.current
    val activity = context as? FragmentActivity
    val authStatus by viewModel.authStatus.collectAsState()

    var showBiometricPrompt by remember { mutableStateOf(false) }
    // Use a state to track if the initial check has been done
    var initialCheckDone by remember { mutableStateOf(false) }
    val biometricManager = BiometricManager.from(context)
    val canAuthenticate =
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS -> true
            else -> false
        }

    LaunchedEffect(Unit) {
        if (!initialCheckDone) {
            if (canAuthenticate && activity != null) {
                viewModel.updateAuthStatus(BiometricAuthStatus.IDLE) // Reset status
                showBiometricPrompt = true
            } else {
                viewModel.updateAuthStatus(BiometricAuthStatus.NOT_AVAILABLE)
            }
            initialCheckDone = true
        }
    }

    val biometricPrompt = remember(activity, viewModel) {
        activity?.let {
            BiometricPrompt(
                it,
                ContextCompat.getMainExecutor(it),
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        Timber.d("Biometric authentication succeeded")
                        viewModel.updateAuthStatus(BiometricAuthStatus.SUCCESS)
                        onAuthSuccess()
                    }

                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        Timber.e("Biometric authentication error: $errorCode - $errString")
                        if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON || errorCode == BiometricPrompt.ERROR_USER_CANCELED) {
                            viewModel.updateAuthStatus(BiometricAuthStatus.CANCELLED)
                        } else {
                            viewModel.updateAuthStatus(BiometricAuthStatus.ERROR)
                        }
                        onAuthCancelledOrFailed() // Navigate away or show error
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        Timber.w("Biometric authentication failed (e.g., wrong finger)")
                        // Prompt is usually shown again by system, or error is called after too many attempts.
                        // For now, we don't set a specific status here, relying on onAuthenticationError for final states.
                    }
                }
            )
        }
    }

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle(stringResource(R.string.biometric_prompt_title))
        .setSubtitle(stringResource(R.string.biometric_prompt_subtitle))
        .setAllowedAuthenticators(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)
        .build()

    LaunchedEffect(showBiometricPrompt, biometricPrompt, promptInfo) {
        if (showBiometricPrompt && biometricPrompt != null) {
            Timber.d("Showing biometric prompt")
            biometricPrompt.authenticate(promptInfo)
            viewModel.updateAuthStatus(BiometricAuthStatus.PROMPT_SHOWN)
            showBiometricPrompt = false // Reset trigger
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (authStatus) {
            BiometricAuthStatus.IDLE, BiometricAuthStatus.PROMPT_SHOWN -> {
                Text(stringResource(R.string.biometric_loading_text))
                Spacer(modifier = Modifier.height(16.dp))
                // Optionally, show a button to retry if stuck
                Button(onClick = {
                    if (canAuthenticate && activity != null) {
                        showBiometricPrompt = true
                    } else {
                        viewModel.updateAuthStatus(BiometricAuthStatus.NOT_AVAILABLE)
                    }
                }) {
                    Text(stringResource(R.string.biometric_retry_button))
                }
            }

            BiometricAuthStatus.SUCCESS -> {
                LoadingIndicator()
                Timber.d(stringResource(R.string.biometric_success_text))
            }

            BiometricAuthStatus.ERROR -> {
                Text(stringResource(R.string.biometric_error_text))
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onAuthCancelledOrFailed) {
                    Text(stringResource(R.string.biometric_continue_anyway_button)) // Or "Go to Login"
                }
            }

            BiometricAuthStatus.NOT_AVAILABLE -> {
                Text(stringResource(R.string.biometric_not_available_text))
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onAuthCancelledOrFailed) { // Should probably go to login or home
                    Text(stringResource(R.string.biometric_continue_anyway_button))
                }
            }

            BiometricAuthStatus.CANCELLED -> {
                Text(stringResource(R.string.biometric_cancelled_text))
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = onAuthCancelledOrFailed) {
                    Text(stringResource(R.string.biometric_continue_anyway_button))
                }
            }
        }
    }
}