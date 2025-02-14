package com.devpush.animeapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devpush.animeapp.R
import com.devpush.animeapp.ui.theme.Pink80
import com.devpush.animeapp.ui.theme.Purple80
import com.devpush.animeapp.ui.theme.PurpleGrey80

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onOpenRegistrationClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    0f to Purple80,
                    0.6f to PurpleGrey80,
                    1f to Pink80
                )
            ),
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

        var filledText by remember {
            mutableStateOf("")
        }

        TextField(
            value = filledText,
            onValueChange = { filledText = it },
            enabled = true,
            textStyle = LocalTextStyle.current.copy(
                textAlign = TextAlign.Right,
            ),
            label = {
                Text(text = "Enter your email")
            }
        )

        var filledPass by remember {
            mutableStateOf("")
        }
        var passwordVisible by remember { mutableStateOf(false) }

        val visualTransformation = if (passwordVisible) {
            VisualTransformation.None // Show the password
        } else {
            PasswordVisualTransformation() // Hide the password
        }

        OutlinedTextField(
            value = filledPass,
            modifier = Modifier.padding(all = 8.dp),
            onValueChange = { filledPass = it },

            textStyle = LocalTextStyle.current.copy(
                color = Color.Black,
                textAlign = TextAlign.Right,
            ),
            visualTransformation = visualTransformation,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val icon = if(passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(icon, contentDescription = "Visibility")
                }
            },
            label = {
                Text(text = "Enter your password")
            }

        )


    }

}