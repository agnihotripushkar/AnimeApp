package com.devpush.animeapp.screens

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devpush.animeapp.R
import com.devpush.animeapp.components.RoundedCornerTextField
import com.devpush.animeapp.components.Separator
import com.devpush.animeapp.ui.theme.Pink80
import com.devpush.animeapp.ui.theme.PrimaryPink
import com.devpush.animeapp.ui.theme.PrimaryPinkBlended
import com.devpush.animeapp.ui.theme.PrimaryPinkDark
import com.devpush.animeapp.ui.theme.PrimaryPinkLight
import com.devpush.animeapp.ui.theme.Purple80
import com.devpush.animeapp.ui.theme.PurpleGrey80
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onOpenRegistrationClicked: () -> Unit,
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
            modifier = Modifier.padding(horizontal = 24.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        RoundedCornerTextField(
            leadingIconRes = R.drawable.ic_key,
            placeholderText = "Your Password",
            modifier = Modifier.padding(horizontal = 24.dp),
            trailingIcon = Icons.Filled.VisibilityOff,
            isPasswordTextField = true
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onLoginClicked,
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

}