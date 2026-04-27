package com.devpush.animeapp.features.auth.ui.signup

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.ButtonGroup
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import com.devpush.animeapp.features.auth.ui.signup.RegistrationViewModel
import com.devpush.animeapp.features.auth.ui.signup.RegistrationState
import com.devpush.animeapp.features.auth.ui.signup.RegistrationAction
import com.devpush.animeapp.features.auth.ui.signup.RegistrationEvent
import com.devpush.animeapp.core.presentation.ObserveAsEvents
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devpush.animeapp.ui.theme.PrimaryViolet
import com.devpush.animeapp.ui.theme.PrimaryVioletDark
import com.devpush.animeapp.ui.theme.PrimaryVioletLight
import com.devpush.animeapp.utils.DevicePosture
import com.devpush.animeapp.utils.rememberDevicePosture
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun RegistrationScreen(
    modifier: Modifier = Modifier,
    onRegisterSuccessNavigation: () -> Unit,
    onLoginClicked: () -> Unit,
    viewModel: RegistrationViewModel = koinViewModel()
) {
    val windowSize = rememberDevicePosture(
        windowSizeClass = calculateWindowSizeClass(
            LocalContext.current as Activity
        )
    )

    val state by viewModel.state.collectAsStateWithLifecycle()
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

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is RegistrationEvent.RegistrationSuccess -> onRegisterSuccessNavigation()
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        when (windowSize) {
            DevicePosture.PHONE_COMPACT -> {
                RegistrationScreenCompact(
                    modifier = modifier,
                    onRegisterSuccessNavigation = onRegisterSuccessNavigation,
                    onLoginClicked = onLoginClicked,
                    onAction = viewModel::onAction,
                    state = state,
                    passwordVisible = passwordVisible,
                    onPasswordVisibilityChanged = { passwordVisible = !passwordVisible },
                    confirmPasswordVisible = confirmPasswordVisible,
                    onConfirmPasswordVisibilityChanged = { confirmPasswordVisible = !confirmPasswordVisible }
                )
            }

            DevicePosture.TABLET_COMPACT_PORTRAIT -> {
                // TODO Handle Tablet vertical Orientation
                RegistrationScreenCompact(
                    modifier = modifier,
                    onRegisterSuccessNavigation = onRegisterSuccessNavigation,
                    onLoginClicked = onLoginClicked,
                    onAction = viewModel::onAction,
                    state = state,
                    passwordVisible = passwordVisible,
                    onPasswordVisibilityChanged = { passwordVisible = !passwordVisible },
                    confirmPasswordVisible = confirmPasswordVisible,
                    onConfirmPasswordVisibilityChanged = { confirmPasswordVisible = !confirmPasswordVisible }
                )
            }
            DevicePosture.MEDIUM_WIDTH -> {
                RegistrationScreenCompact(
                    modifier = modifier,
                    onRegisterSuccessNavigation = onRegisterSuccessNavigation,
                    onLoginClicked = onLoginClicked,
                    onAction = viewModel::onAction,
                    state = state,
                    passwordVisible = passwordVisible,
                    onPasswordVisibilityChanged = { passwordVisible = !passwordVisible },
                    confirmPasswordVisible = confirmPasswordVisible,
                    onConfirmPasswordVisibilityChanged = { confirmPasswordVisible = !confirmPasswordVisible }
                )
            }
            DevicePosture.EXPANDED_WIDTH -> {
                RegistrationScreenExpanded(
                    modifier = modifier,
                    onRegisterSuccessNavigation = onRegisterSuccessNavigation,
                    onLoginClicked = onLoginClicked,
                    onAction = viewModel::onAction,
                    state = state,
                    passwordVisible = passwordVisible,
                    onPasswordVisibilityChanged = { passwordVisible = !passwordVisible },
                    confirmPasswordVisible = confirmPasswordVisible,
                    onConfirmPasswordVisibilityChanged = { confirmPasswordVisible = !confirmPasswordVisible }
                )
            }
        }
    }
}

@Composable
fun RegistrationScreenCompact(
    modifier: Modifier = Modifier,
    onRegisterSuccessNavigation: () -> Unit,
    onLoginClicked: () -> Unit,
    onAction: (RegistrationAction) -> Unit,
    state: RegistrationState,
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
            onAction = onAction,
            state = state,
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
    onAction: (RegistrationAction) -> Unit,
    state: RegistrationState,
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
                onAction = onAction,
                state = state,
                passwordVisible = passwordVisible,
                onPasswordVisibilityChanged = onPasswordVisibilityChanged,
                confirmPasswordVisible = confirmPasswordVisible,
                onConfirmPasswordVisibilityChanged = onConfirmPasswordVisibilityChanged,
                onLoginClicked = onLoginClicked
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun RegistrationForm(
    onAction: (RegistrationAction) -> Unit,
    state: RegistrationState,
    passwordVisible: Boolean,
    onPasswordVisibilityChanged: () -> Unit,
    confirmPasswordVisible: Boolean,
    onConfirmPasswordVisibilityChanged: () -> Unit,
    onLoginClicked: () -> Unit
) {
    // --- Email TextField ---
    OutlinedTextField(
        value = state.email,
        onValueChange = { onAction(RegistrationAction.EmailChanged(it)) },
        label = { Text(stringResource(R.string.you_email)) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        isError = state.emailError != null,
        supportingText = {
            state.emailError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
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
        value = state.password,
        onValueChange = { onAction(RegistrationAction.PasswordChanged(it)) },
        label = { Text(stringResource(R.string.your_password)) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Next
        ),
        isError = state.passwordError != null,
        supportingText = {
            state.passwordError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
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
        value = state.confirmPassword,
        onValueChange = { onAction(RegistrationAction.ConfirmPasswordChanged(it)) },
        label = { Text(stringResource(R.string.confirm_password_hint)) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done // Last field
        ),
        isError = state.confirmPasswordError != null,
        supportingText = {
            state.confirmPasswordError?.let { Text(it, color = MaterialTheme.colorScheme.error) }
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
    state.generalError?.let {
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = it,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        )
    }

    Spacer(modifier = Modifier.height(32.dp))

    ButtonGroup(
        modifier = Modifier.fillMaxWidth()
    ) {
        Button(
            onClick = { onAction(RegistrationAction.Submit) },
            modifier = Modifier
                .weight(1f)
                .height(50.dp),
            enabled = !state.isLoading,
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryVioletDark,
                contentColor = Color.White,
                disabledContainerColor = PrimaryVioletDark.copy(alpha = 0.5f)
            )
        ) {
            if (state.isLoading) {
                ContainedLoadingIndicator(modifier = Modifier.size(48.dp))
            } else {
                Text(text = stringResource(R.string.create_an_account))
            }
        }

        Button(
            onClick = onLoginClicked,
            modifier = Modifier
                .weight(1f)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryVioletLight,
                contentColor = Color.White
            )
        ) {
            Text(text = stringResource(R.string.login_instead))
        }
    }
}