package com.devpush.animeapp.features.auth.ui.signup

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.devpush.animeapp.R
import com.devpush.animeapp.features.auth.ui.AuthViewModel
import com.devpush.animeapp.features.auth.ui.utils.Separator
import com.devpush.animeapp.ui.theme.PrimaryViolet
import com.devpush.animeapp.ui.theme.PrimaryVioletDark
import com.devpush.animeapp.ui.theme.PrimaryVioletLight
import com.devpush.animeapp.utils.WindowSize
import com.devpush.animeapp.utils.rememberWindowSizeClass
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun RegistrationScreen(
    modifier: Modifier = Modifier,
    onRegisterSuccessNavigation: () -> Unit,
    onLoginClicked: () -> Unit,
    viewModel: AuthViewModel = koinViewModel()
) {
    val windowSize = rememberWindowSizeClass(
        windowSizeClass = calculateWindowSizeClass(
            LocalContext.current as Activity
        )
    )

    val uiState by viewModel.registrationUiState.collectAsState()
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val keyboardHeight = WindowInsets.ime.getBottom(LocalDensity.current)

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(keyboardHeight) {
        coroutineScope.launch {
            if (keyboardHeight > 0) {
                scrollState.scrollBy(keyboardHeight.toFloat() * 1.5f)
            }
        }
    }

    LaunchedEffect(uiState.isRegistrationSuccess) {
        if (uiState.isRegistrationSuccess) {
            onRegisterSuccessNavigation()
            viewModel.onRegistrationHandled() // Reset the flag
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        if (windowSize == WindowSize.EXPANDED) {
            RegistrationScreenExpanded(
                modifier = modifier,
                onRegisterSuccessNavigation = onRegisterSuccessNavigation,
                onLoginClicked = onLoginClicked,
                viewModel = viewModel,
                uiState = uiState,
                passwordVisible = passwordVisible,
                onPasswordVisibilityChanged = { passwordVisible = !passwordVisible },
                confirmPasswordVisible = confirmPasswordVisible,
                onConfirmPasswordVisibilityChanged = { confirmPasswordVisible = !confirmPasswordVisible }
            )
        } else {
            RegistrationScreenCompact(
                modifier = modifier,
                onRegisterSuccessNavigation = onRegisterSuccessNavigation,
                onLoginClicked = onLoginClicked,
                viewModel = viewModel,
                uiState = uiState,
                passwordVisible = passwordVisible,
                onPasswordVisibilityChanged = { passwordVisible = !passwordVisible },
                confirmPasswordVisible = confirmPasswordVisible,
                onConfirmPasswordVisibilityChanged = { confirmPasswordVisible = !confirmPasswordVisible }
            )
        }
    }
}

@Composable
fun RegistrationScreenCompact(
    modifier: Modifier = Modifier,
    onRegisterSuccessNavigation: () -> Unit,
    onLoginClicked: () -> Unit,
    viewModel: AuthViewModel,
    uiState: RegistrationUiState,
    passwordVisible: Boolean,
    onPasswordVisibilityChanged: () -> Unit,
    confirmPasswordVisible: Boolean,
    onConfirmPasswordVisibilityChanged: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(
                Brush.verticalGradient(
                    0f to PrimaryViolet,
                    1f to PrimaryVioletDark
                )
            )
            .imePadding()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.img_coder_w),
            contentDescription = "Registration Image",
            modifier = Modifier
                .size(280.dp)
                .padding(top = 32.dp, bottom = 16.dp)
        )

        Text(
            text = stringResource(R.string.hi_there),
            modifier = Modifier.padding(vertical = 8.dp),
            color = Color.White,
            style = MaterialTheme.typography.displaySmall
        )

        Text(
            text = stringResource(R.string.get_started_with_your_account),
            modifier = Modifier.padding(bottom = 24.dp),
            color = Color.White,
            style = MaterialTheme.typography.bodyLarge
        )

        RegistrationForm(
            viewModel = viewModel,
            uiState = uiState,
            passwordVisible = passwordVisible,
            onPasswordVisibilityChanged = onPasswordVisibilityChanged,
            confirmPasswordVisible = confirmPasswordVisible,
            onConfirmPasswordVisibilityChanged = onConfirmPasswordVisibilityChanged,
            onLoginClicked = onLoginClicked
        )
    }
}

@Composable
fun RegistrationScreenExpanded(
    modifier: Modifier = Modifier,
    onRegisterSuccessNavigation: () -> Unit,
    onLoginClicked: () -> Unit,
    viewModel: AuthViewModel,
    uiState: RegistrationUiState,
    passwordVisible: Boolean,
    onPasswordVisibilityChanged: () -> Unit,
    confirmPasswordVisible: Boolean,
    onConfirmPasswordVisibilityChanged: () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    0f to PrimaryViolet,
                    1f to PrimaryVioletDark
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(R.drawable.img_coder_w),
                contentDescription = "Registration Image",
                modifier = Modifier
                    .size(380.dp)
                    .padding(top = 32.dp, bottom = 16.dp)
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .imePadding()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(R.string.hi_there),
                modifier = Modifier.padding(vertical = 8.dp),
                color = Color.White,
                style = MaterialTheme.typography.displaySmall
            )

            Text(
                text = stringResource(R.string.get_started_with_your_account),
                modifier = Modifier.padding(bottom = 24.dp),
                color = Color.White,
                style = MaterialTheme.typography.bodyLarge
            )

            RegistrationForm(
                viewModel = viewModel,
                uiState = uiState,
                passwordVisible = passwordVisible,
                onPasswordVisibilityChanged = onPasswordVisibilityChanged,
                confirmPasswordVisible = confirmPasswordVisible,
                onConfirmPasswordVisibilityChanged = onConfirmPasswordVisibilityChanged,
                onLoginClicked = onLoginClicked
            )
        }
    }
}

@Composable
fun RegistrationForm(
    viewModel: AuthViewModel,
    uiState: RegistrationUiState,
    passwordVisible: Boolean,
    onPasswordVisibilityChanged: () -> Unit,
    confirmPasswordVisible: Boolean,
    onConfirmPasswordVisibilityChanged: () -> Unit,
    onLoginClicked: () -> Unit
) {
    // --- Email TextField ---
    OutlinedTextField(
        value = uiState.email,
        onValueChange = { viewModel.onRegistrationEmailChanged(it) },
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
                painterResource(id = R.drawable.ic_person),
                contentDescription = stringResource(R.string.your_email_hint)
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
        onValueChange = { viewModel.onRegistrationPasswordChanged(it) },
        label = { Text(stringResource(R.string.your_password)) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Next
        ),
        isError = uiState.passwordError != null,
        supportingText = {
            uiState.passwordError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        },
        leadingIcon = {
            Icon(
                painterResource(id = R.drawable.ic_key),
                contentDescription = "Password Icon"
            )
        },
        trailingIcon = {
            val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
            IconButton(onClick = { onPasswordVisibilityChanged() }) {
                Icon(imageVector = image, "Toggle password visibility", tint = Color.White.copy(alpha = 0.7f))
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

    Spacer(modifier = Modifier.height(16.dp))

    // --- Confirm Password TextField ---
    OutlinedTextField(
        value = uiState.confirmPassword,
        onValueChange = { viewModel.onConfirmPasswordChanged(it) },
        label = { Text(stringResource(R.string.confirm_password_hint)) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done // Last field
        ),
        isError = uiState.confirmPasswordError != null,
        supportingText = {
            uiState.confirmPasswordError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
        },
        leadingIcon = {
            Icon(
                painterResource(id = R.drawable.ic_key),
                contentDescription = stringResource(R.string.confirm_password_hint)
            )
        },
        trailingIcon = {
            val image = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
            IconButton(onClick = { onConfirmPasswordVisibilityChanged() }) {
                Icon(imageVector = image, "Toggle confirm password visibility", tint = Color.White.copy(alpha = 0.7f))
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

    // --- General Registration Error ---
    uiState.generalRegistrationError?.let {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = it,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        )
    }

    Spacer(modifier = Modifier.height(32.dp))

    Button(
        onClick = { viewModel.registerUser() },
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        enabled = !uiState.isLoading,
        colors = ButtonDefaults.buttonColors(
            containerColor = PrimaryVioletDark,
            contentColor = Color.White,
            disabledContainerColor = PrimaryVioletDark.copy(alpha = 0.5f) // Visual cue for disabled
        )
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
        } else {
            Text(text = stringResource(R.string.create_an_account))
        }
    }

    Separator(
        modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp)
    )

    Button(
        onClick = onLoginClicked,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = PrimaryVioletLight,
            contentColor = Color.White
        )
    ) {
        Text(text = stringResource(R.string.login_instead))
    }
}