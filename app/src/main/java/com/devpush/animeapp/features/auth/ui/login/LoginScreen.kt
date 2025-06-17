package com.devpush.animeapp.features.auth.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults // Make sure this is from androidx.compose.material3
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devpush.animeapp.R
import com.devpush.animeapp.ui.theme.PrimaryPink
import com.devpush.animeapp.ui.theme.PrimaryPinkBlended
import com.devpush.animeapp.ui.theme.PrimaryPinkDark
import com.devpush.animeapp.ui.theme.PrimaryPinkLight
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.devpush.animeapp.utils.findActivity
// import android.util.Log // Removed
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.devpush.animeapp.features.auth.ui.AuthViewModel
import com.devpush.animeapp.features.auth.ui.utils.Separator
import com.devpush.animeapp.ui.theme.AnimeAppTheme

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onOpenRegistrationClicked: () -> Unit,
    onLoginSuccessNavigation: () -> Unit,
    viewModel: AuthViewModel = koinViewModel()
) {

    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val keyboardHeight = WindowInsets.ime.getBottom(LocalDensity.current)
    var passwordVisible by remember { mutableStateOf(false) }
    // val context = LocalContext.current as FragmentActivity // For Biometric Prompt // Old way
    val context = LocalContext.current
    val activity = remember(context) { context.findActivity() }


    LaunchedEffect(key1 = Unit, key2 = activity) { // Runs once when the screen is composed
        activity?.let { fragActivity ->
            viewModel.onLoginScreenLaunched(fragActivity)
        }
    }

    LaunchedEffect(key1 = viewModel.triggerBiometricPromptEvent, key2 = activity) {
        viewModel.triggerBiometricPromptEvent.collect {
            activity?.let { fragActivity ->
                viewModel.startBiometricAuthentication(fragActivity)
            }
        }
    }

    LaunchedEffect(keyboardHeight) {
        coroutineScope.launch {
            scrollState.scrollBy(keyboardHeight.toFloat())
        }
    }

    LaunchedEffect(uiState.isLoginSuccess) {
        if (uiState.isLoginSuccess) {
            onLoginSuccessNavigation()
            viewModel.onLoginHandled() // Reset the flag in ViewModel
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .background(
                    Brush.verticalGradient(
                        0f to PrimaryPinkBlended,
                        1f to PrimaryPink
                    )
                )
                .imePadding()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painterResource(R.drawable.img_coder_m),
                contentDescription = "Login Image",
                modifier = Modifier
                    .size(280.dp)
                    .padding(top = 32.dp, bottom = 16.dp)
            )

            Text(
                text = stringResource(R.string.welcome_back),
                modifier = Modifier.padding(bottom = 4.dp),
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = Color.White
            )

            Text(
                text = stringResource(R.string.please_login),
                modifier = Modifier.padding(bottom = 24.dp),
                fontWeight = FontWeight.Bold,
                color = Color.White,
                fontSize = 30.sp,
            )

            OutlinedTextField(
                value = uiState.email,
                onValueChange = { viewModel.onEmailChanged(it) },
                label = { Text(stringResource(R.string.you_email)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                ),
                isError = uiState.emailError != null,
                supportingText = {
                    uiState.emailError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                },
                leadingIcon = {
                    Icon(
                        painterResource(id = R.drawable.ic_person), // Ensure this drawable exists
                        contentDescription = "Email Icon"
                    )
                },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White.copy(alpha = 0.1f),
                    unfocusedContainerColor = Color.White.copy(alpha = 0.1f),
                    disabledContainerColor = Color.White.copy(alpha = 0.05f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLeadingIconColor = Color.White,
                    unfocusedLeadingIconColor = Color.White.copy(alpha = 0.7f),
                    errorTextColor = MaterialTheme.colorScheme.error,
                    cursorColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White.copy(alpha = 0.7f)
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            // --- Password TextField ---
            OutlinedTextField(
                value = uiState.password,
                onValueChange = { viewModel.onPasswordChanged(it) },
                label = { Text(stringResource(R.string.your_password)) },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                visualTransformation = if (passwordVisible) VisualTransformation.None
                else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                isError = uiState.passwordError != null,
                supportingText = {
                    uiState.passwordError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                },
                leadingIcon = {
                    Icon(
                        painterResource(id = R.drawable.ic_key), // Ensure this drawable exists
                        contentDescription = "Password Icon"
                    )
                },
                trailingIcon = {
                    val image =
                        if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    val description = if (passwordVisible) "Hide password" else "Show password"
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = image,
                            contentDescription = description,
                            tint = Color.White.copy(alpha = 0.7f)
                        )
                    }
                },

                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White.copy(alpha = 0.1f),
                    unfocusedContainerColor = Color.White.copy(alpha = 0.1f),
                    disabledContainerColor = Color.White.copy(alpha = 0.05f),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedLeadingIconColor = Color.White,
                    unfocusedLeadingIconColor = Color.White.copy(alpha = 0.7f),
                    errorTextColor = MaterialTheme.colorScheme.error,
                    cursorColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White.copy(alpha = 0.7f)
                )
            )

            // --- General Login Error ---
            uiState.generalLoginError?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }


            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { viewModel.loginUser() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = !uiState.isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryPinkDark,
                    contentColor = Color.White
                )
            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(text = stringResource(R.string.login), fontSize = 16.sp)
                }
            }

            Separator(
                modifier = Modifier.padding(vertical = 16.dp)
            )

            // --- Register Button ---
            Button(
                onClick = onOpenRegistrationClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryPinkLight,
                    contentColor = Color.White
                )
            ) {
                Text(text = stringResource(R.string.register_here), fontSize = 16.sp)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    AnimeAppTheme {
        LoginScreen(
            onOpenRegistrationClicked = {},
            onLoginSuccessNavigation = {}
        )
    }
}