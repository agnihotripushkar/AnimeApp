package com.devpush.animeapp.features.auth.ui.login

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.devpush.animeapp.R
import com.devpush.animeapp.core.presentation.ObserveAsEvents
import com.devpush.animeapp.ui.theme.AnimeAppTheme
import com.devpush.animeapp.ui.theme.PrimaryPink
import com.devpush.animeapp.ui.theme.PrimaryPinkBlended
import com.devpush.animeapp.ui.theme.PrimaryPinkDark
import com.devpush.animeapp.ui.theme.PrimaryPinkLight
import com.devpush.animeapp.utils.DevicePosture
import com.devpush.animeapp.utils.rememberDevicePosture
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun LoginRoot(
    onOpenRegistrationClicked: () -> Unit,
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            is LoginEvent.LoginSuccess -> onLoginSuccess()
        }
    }

    LoginScreen(
        state = state,
        onAction = viewModel::onAction,
        onOpenRegistrationClicked = onOpenRegistrationClicked
    )
}

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun LoginScreen(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
    onOpenRegistrationClicked: () -> Unit
) {
    val windowSize = rememberDevicePosture(
        windowSizeClass = calculateWindowSizeClass(LocalContext.current as Activity)
    )
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val keyboardHeight = WindowInsets.ime.getBottom(LocalDensity.current)
    var passwordVisible by remember { mutableStateOf(false) }

    androidx.compose.runtime.LaunchedEffect(keyboardHeight) {
        coroutineScope.launch { scrollState.scrollBy(keyboardHeight.toFloat()) }
    }

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        when (windowSize) {
            DevicePosture.EXPANDED_WIDTH -> {
                LoginScreenExpanded(
                    state = state,
                    onAction = onAction,
                    onOpenRegistrationClicked = onOpenRegistrationClicked,
                    passwordVisible = passwordVisible,
                    onPasswordVisibilityChanged = { passwordVisible = !passwordVisible }
                )
            }
            else -> {
                LoginScreenCompact(
                    state = state,
                    onAction = onAction,
                    onOpenRegistrationClicked = onOpenRegistrationClicked,
                    passwordVisible = passwordVisible,
                    onPasswordVisibilityChanged = { passwordVisible = !passwordVisible }
                )
            }
        }
    }
}

@Composable
fun LoginScreenCompact(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
    onOpenRegistrationClicked: () -> Unit,
    passwordVisible: Boolean,
    onPasswordVisibilityChanged: () -> Unit
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(Brush.verticalGradient(0f to PrimaryPinkBlended, 1f to PrimaryPink))
            .imePadding()
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painterResource(R.drawable.img_coder_m),
            contentDescription = "Login Image",
            modifier = Modifier.size(280.dp).padding(top = 32.dp, bottom = 16.dp)
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
            fontSize = 30.sp
        )
        LoginForm(
            state = state,
            onAction = onAction,
            passwordVisible = passwordVisible,
            onPasswordVisibilityChanged = onPasswordVisibilityChanged,
            onOpenRegistrationClicked = onOpenRegistrationClicked
        )
    }
}

@Composable
fun LoginScreenExpanded(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
    onOpenRegistrationClicked: () -> Unit,
    passwordVisible: Boolean,
    onPasswordVisibilityChanged: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(0f to PrimaryPinkBlended, 1f to PrimaryPink))
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(0.5f).fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painterResource(R.drawable.img_coder_m),
                contentDescription = "Login Image",
                modifier = Modifier.size(380.dp).padding(top = 32.dp, bottom = 16.dp)
            )
        }
        Column(
            modifier = Modifier.fillMaxWidth(0.9f).fillMaxHeight().imePadding().padding(end = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
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
                fontSize = 30.sp
            )
            LoginForm(
                state = state,
                onAction = onAction,
                passwordVisible = passwordVisible,
                onPasswordVisibilityChanged = onPasswordVisibilityChanged,
                onOpenRegistrationClicked = onOpenRegistrationClicked
            )
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun LoginForm(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
    passwordVisible: Boolean,
    onPasswordVisibilityChanged: () -> Unit,
    onOpenRegistrationClicked: () -> Unit
) {
    OutlinedTextField(
        value = state.email,
        onValueChange = { onAction(LoginAction.EmailChanged(it)) },
        label = { Text(stringResource(R.string.you_email)) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
        isError = state.emailError != null,
        supportingText = { state.emailError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
        leadingIcon = { Icon(painterResource(id = R.drawable.ic_person), contentDescription = "Email Icon") },
        colors = textFieldColors()
    )

    Spacer(modifier = Modifier.height(16.dp))

    OutlinedTextField(
        value = state.password,
        onValueChange = { onAction(LoginAction.PasswordChanged(it)) },
        label = { Text(stringResource(R.string.your_password)) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
        isError = state.passwordError != null,
        supportingText = { state.passwordError?.let { Text(it, color = MaterialTheme.colorScheme.error) } },
        leadingIcon = { Icon(painterResource(id = R.drawable.ic_key), contentDescription = "Password Icon") },
        trailingIcon = {
            val image = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
            IconButton(onClick = onPasswordVisibilityChanged) {
                Icon(imageVector = image, contentDescription = null, tint = Color.White.copy(alpha = 0.7f))
            }
        },
        colors = textFieldColors()
    )

    state.generalError?.let {
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = it, color = MaterialTheme.colorScheme.error, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
    }

    Spacer(modifier = Modifier.height(32.dp))

    ButtonGroup(modifier = Modifier.fillMaxWidth()) {
        Button(
            onClick = { onAction(LoginAction.Submit) },
            modifier = Modifier.weight(1f).height(50.dp),
            enabled = !state.isLoading,
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPinkDark, contentColor = Color.White)
        ) {
            if (state.isLoading) ContainedLoadingIndicator(modifier = Modifier.size(48.dp))
            else Text(text = stringResource(R.string.login), fontSize = 16.sp)
        }
        Button(
            onClick = onOpenRegistrationClicked,
            modifier = Modifier.weight(1f).height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = PrimaryPinkLight, contentColor = Color.White)
        ) {
            Text(text = stringResource(R.string.register_here), fontSize = 16.sp)
        }
    }
}

@Composable
private fun textFieldColors() = TextFieldDefaults.colors(
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun LoginScreenPreview() {
    AnimeAppTheme {
        LoginScreen(state = LoginState(), onAction = {}, onOpenRegistrationClicked = {})
    }
}
