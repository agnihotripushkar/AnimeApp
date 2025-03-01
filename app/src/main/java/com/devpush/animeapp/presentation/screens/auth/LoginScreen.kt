package com.devpush.animeapp.presentation.screens.auth

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devpush.animeapp.R
import com.devpush.animeapp.presentation.components.RoundedCornerTextField
import com.devpush.animeapp.presentation.components.Separator
import com.devpush.animeapp.presentation.ui.theme.PrimaryPink
import com.devpush.animeapp.presentation.ui.theme.PrimaryPinkBlended
import com.devpush.animeapp.presentation.ui.theme.PrimaryPinkDark
import com.devpush.animeapp.presentation.ui.theme.PrimaryPinkLight
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onOpenRegistrationClicked: () -> Unit,
    onLoginClicked: () -> Unit,
    viewModel: AuthViewModel = koinViewModel()
) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val keyboardHeight = WindowInsets.ime.getBottom(LocalDensity.current)
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(keyboardHeight) {
        coroutineScope.launch {
            scrollState.scrollBy(keyboardHeight.toFloat())
        }
    }

    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            onLoginClicked()
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
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painterResource(R.drawable.img_coder_m),
                contentDescription = "Login Image",
                modifier = Modifier
                    .size(300.dp)
                    .padding(top = 32.dp, bottom = 32.dp)
            )

            Text(
                text = "Welcome back!",
                modifier = Modifier.padding(all = 8.dp),
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = Color.White
            )

            Text(
                text = "Please Login", modifier = Modifier.padding(all = 8.dp),
                fontWeight = FontWeight.Bold, color = Color.White, fontSize = 32.sp,
            )

            RoundedCornerTextField(
                leadingIconRes = R.drawable.ic_person,
                placeholderText = "You email",
                modifier = Modifier.padding(horizontal = 24.dp),
                onValueChange = { email = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            RoundedCornerTextField(
                leadingIconRes = R.drawable.ic_key,
                placeholderText = "Your Password",
                modifier = Modifier.padding(horizontal = 24.dp),
                trailingIcon = Icons.Filled.VisibilityOff,
                isPasswordTextField = true,
                onValueChange = { password = it }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { viewModel.loginCall(email, password) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryPinkDark,
                    contentColor = Color.White
                )
            ) {
                Text(text = "Login")
            }

            Separator(
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp)
            )

            Button(
                onClick = onOpenRegistrationClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .padding(bottom = 24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryPinkLight,
                    contentColor = Color.White
                )
            ) {
                Text(text = "Register Here")
            }


        }

        if (isLoading) {
            CircularProgressIndicator()
        }
    }
}