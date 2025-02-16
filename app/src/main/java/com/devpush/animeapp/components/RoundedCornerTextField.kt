package com.devpush.animeapp.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.devpush.animeapp.R
import com.devpush.animeapp.ui.theme.DarkTextColor

@Composable
fun RoundedCornerTextField(
    leadingIconRes: Int,
    placeholderText: String,
    modifier: Modifier = Modifier,
    trailingIcon: ImageVector? = null,
    isPasswordTextField:Boolean = false
){
    var value by remember {
        mutableStateOf("")
    }

    var passwordVisible by remember { mutableStateOf(false) }
    var trailIcon = if (passwordVisible) {
        Icons.Filled.Visibility
    }else{
        Icons.Filled.VisibilityOff
    }

    val visualTransformation = if (passwordVisible and isPasswordTextField) {
        VisualTransformation.None // Show the password
    } else if (isPasswordTextField) {
        PasswordVisualTransformation() // Hide the password
    }
    else{
        VisualTransformation.None
    }

    OutlinedTextField(
        value = value,
        onValueChange = {value = it},
        modifier = modifier
            .fillMaxWidth()
            .height(62.dp),
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            errorIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            focusedTextColor = DarkTextColor,
            unfocusedTextColor = DarkTextColor,
            focusedPlaceholderColor = DarkTextColor,
            unfocusedPlaceholderColor = DarkTextColor,
            focusedLeadingIconColor = DarkTextColor,
            unfocusedLeadingIconColor = DarkTextColor,
            focusedTrailingIconColor = DarkTextColor,
            unfocusedTrailingIconColor = DarkTextColor,
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
        ),
        shape = RoundedCornerShape(percent = 50),
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            fontWeight = FontWeight.Medium
        ),
        visualTransformation = visualTransformation,
        placeholder = {
            Text(
                text = placeholderText,
                modifier = Modifier.padding(start = 8.dp)
            )
        },
        leadingIcon = {
            Icon(
                painter = painterResource(id =leadingIconRes),
                contentDescription = "Person Icon",
                modifier = Modifier.size(24.dp)
            )
        },
        trailingIcon = {
            if (trailingIcon != null){
                IconButton(
                    onClick = {
                        passwordVisible = !passwordVisible
                    }
                ) {
                    Icon(
                        imageVector = trailIcon,
                        contentDescription = "toggle visibility Icon",
                        modifier = Modifier.size(24.dp),
                    )
                }
            }
        },
        keyboardOptions = if (isPasswordTextField){
            KeyboardOptions(keyboardType = KeyboardType.Password)
        }else{
            KeyboardOptions(keyboardType = KeyboardType.Email)
        }
    )

}