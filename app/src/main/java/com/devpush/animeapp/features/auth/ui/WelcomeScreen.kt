package com.devpush.animeapp.features.auth.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devpush.animeapp.R
import com.devpush.animeapp.ui.theme.DarkTextColor
import com.devpush.animeapp.ui.theme.Pink80
import com.devpush.animeapp.ui.theme.Purple80
import com.devpush.animeapp.ui.theme.PurpleGrey80

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    onOpenLoginClicked: () -> Unit
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    0f to Purple80,
                    0.6f to PurpleGrey80,
                    1f to Pink80
                )
            )
            .padding(all = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        Image(
            painterResource(R.drawable.img_welcome),
            contentDescription = "Anime Logo",
            modifier = Modifier
                .size(300.dp)
                .padding(top = 32.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Let's Start Coding!",
            modifier = Modifier.padding(horizontal = 24.dp),
            style = MaterialTheme.typography.headlineLarge,
            color = DarkTextColor,
            fontWeight = FontWeight.Black,
        )
        Text(
            text = "Create a beautiful Login App using\nKotlin, Jetpack Compose, and Material3",
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 24.dp),
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = DarkTextColor,
        )

        Spacer(modifier = Modifier.height(24.dp))

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FilledIconButton(
                onClick = onOpenLoginClicked,
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp)
            ) {
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(text = "continue",
                        modifier = Modifier.padding(vertical = 8.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 18.sp,
                        color = Color.White
                    )
                    Icon(imageVector = Icons.Filled.ChevronRight,
                        contentDescription = "Right",
                        modifier = Modifier.size(24.dp))
                }
            }
        }


    }


}