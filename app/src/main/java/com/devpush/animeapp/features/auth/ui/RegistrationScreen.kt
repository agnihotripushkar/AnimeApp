package com.devpush.animeapp.features.auth.ui

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
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import com.devpush.animeapp.ui.theme.PrimaryViolet
import com.devpush.animeapp.ui.theme.PrimaryVioletDark
import com.devpush.animeapp.R
import com.devpush.animeapp.ui.theme.PrimaryVioletLight
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.devpush.animeapp.features.auth.ui.utils.RoundedCornerTextField
import com.devpush.animeapp.features.auth.ui.utils.Separator
import com.devpush.animeapp.utils.Constants
import com.devpush.animeapp.utils.DataStoreUtils

@Composable
fun RegistrationScreen(
    modifier: Modifier = Modifier,
    onRegisterClicked: () -> Unit,
    onLoginClicked: () -> Unit,
    viewModel: AuthViewModel = koinViewModel()
) {

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val keyboardHeight = WindowInsets.ime.getBottom(LocalDensity.current)
    val context = LocalContext.current
    val isRegister by viewModel.isRegister.collectAsState()
    val isLoadings by viewModel.isLoading.collectAsState()

    LaunchedEffect(keyboardHeight) {
        coroutineScope.launch {
            scrollState.scrollBy(keyboardHeight.toFloat())
        }
    }

    LaunchedEffect(isRegister) {
        if (isRegister) {
            DataStoreUtils.updateBooleanValue(context, Constants.IS_LOGIN,true)
            onRegisterClicked()
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
                        0f to PrimaryViolet,
                        1f to PrimaryVioletDark
                    )
                )
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(R.drawable.img_coder_w),
                contentDescription = "Registration Image",
                modifier = Modifier
                    .size(300.dp)
                    .padding(top = 32.dp, bottom = 32.dp)
            )

            Text(
                text = "Hi There!",
                modifier = Modifier.padding(vertical = 8.dp),
                color = Color.White,
                style = MaterialTheme.typography.displaySmall
            )

            Text(
                text = "Let's Get Started",
                modifier = Modifier.padding(all = 8.dp),
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )

            RoundedCornerTextField(
                leadingIconRes = R.drawable.ic_person,
                placeholderText = "Your Email",
                modifier = Modifier.padding(horizontal = 24.dp),
                onValueChange = { email = it }
            )

            Spacer(modifier = Modifier.height(16.dp))

            RoundedCornerTextField(
                leadingIconRes = R.drawable.ic_key,
                placeholderText = "Password",
                modifier = Modifier.padding(horizontal = 24.dp),
                isPasswordTextField = true,
                trailingIcon = Icons.Filled.Visibility,
                onValueChange = { password = it }
            )

            Button(
                onClick = { viewModel.registerCall(email, password) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .padding(top = 32.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryVioletDark,
                    contentColor = Color.White
                )
            ) {
                Text(text = "Create An Account")
            }

            Separator(
                modifier = Modifier.padding(vertical = 8.dp, horizontal = 8.dp)
            )

            Button(
                onClick = onLoginClicked,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .padding(bottom = 24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryVioletLight,
                    contentColor = Color.White
                )
            ) {
                Text(text = "Login")
            }

        }

        if (isLoadings) {
            CircularProgressIndicator()
        }
    }


}