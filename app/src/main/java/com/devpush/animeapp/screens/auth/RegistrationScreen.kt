package com.devpush.animeapp.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.scrollBy
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import com.devpush.animeapp.components.RoundedCornerTextField
import com.devpush.animeapp.components.Separator
import com.devpush.animeapp.ui.theme.PrimaryVioletLight
import kotlinx.coroutines.launch

@Composable
fun RegistrationScreen(
    modifier: Modifier = Modifier,
    onRegisterClicked: () -> Unit,
    onLoginClicked: () -> Unit
) {

    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val keyboardHeight = WindowInsets.ime.getBottom(LocalDensity.current)

    LaunchedEffect(keyboardHeight) {
        coroutineScope.launch {
            scrollState.scrollBy(keyboardHeight.toFloat())
        }
    }

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
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        RoundedCornerTextField(
            leadingIconRes = R.drawable.ic_key,
            placeholderText = "Password",
            modifier = Modifier.padding(horizontal = 24.dp),
            isPasswordTextField = true,
            trailingIcon = Icons.Filled.Visibility
        )

        Button(
            onClick = onRegisterClicked,
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


}