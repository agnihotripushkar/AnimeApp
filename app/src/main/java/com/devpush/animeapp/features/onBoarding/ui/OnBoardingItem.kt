package com.devpush.animeapp.features.onBoarding.ui

import android.content.res.Resources
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.devpush.animeapp.features.onBoarding.domain.OnBoardingModel
import com.devpush.animeapp.ui.theme.DarkTextColor

@Composable
fun OnBoardingItem(
    page: OnBoardingModel,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyLarge
) {
    val context = LocalContext.current
    val resources: Resources = context.resources

    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = resources.getString(page.title),
            style = MaterialTheme.typography.headlineLarge,
            color = DarkTextColor,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 32.dp, horizontal = 16.dp)
        )

        Image(
            painter = painterResource(id = page.image),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .aspectRatio(1f)
                .padding(vertical = 32.dp)

        )

        Text(
            text = resources.getString(page.text),
            modifier = Modifier.padding(vertical = 32.dp, horizontal = 16.dp),
            style = style,
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}