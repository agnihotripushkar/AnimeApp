package com.devpush.animeapp.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.devpush.animeapp.components.onBoarding.OnBoardingItem
import com.devpush.animeapp.R
import com.devpush.animeapp.components.onBoarding.OnBoardingModel
import com.devpush.animeapp.ui.theme.DarkTextColor
import com.devpush.animeapp.ui.theme.PrimaryGreen
import com.devpush.animeapp.ui.theme.PrimaryGreenDark
import com.devpush.animeapp.ui.theme.PrimaryGreenLight
import com.devpush.animeapp.ui.theme.PrimaryPinkLight
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

// Define the DataStore name
private val ONBOARDING_PREFERENCES = "onboarding_preferences"

// Define the key for the boolean value
private val IS_ONBOARDING_SHOWN = booleanPreferencesKey("is_onboarding_shown")

// Extension function to access DataStore (Best Practice)
val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = ONBOARDING_PREFERENCES)

suspend fun readOnboardingStatus(context: Context): Boolean {
    val preferences =
        context.dataStore.data.firstOrNull() // Use firstOrNull() to handle potential null
    return preferences?.get(IS_ONBOARDING_SHOWN) == true // Provide default value
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun OnBoardingScreen(modifier: Modifier = Modifier) {

    val onBoardList = listOf(
        OnBoardingModel(
            R.string.onBoarding_title1,
            R.string.onBoarding_text1,
            R.drawable.img_welcome
        ),
        OnBoardingModel(
            R.string.onBoarding_title2,
            R.string.onBoarding_text2,
            R.drawable.img_coder_m
        ),
        OnBoardingModel(
            R.string.onBoarding_title3,
            R.string.onBoarding_text3,
            R.drawable.img_coder_w
        )
    )

    val pagerState = rememberPagerState(initialPage = 0, pageCount = { onBoardList.size })
    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // State to hold the onboarding shown status
    var isOnboardingShown by remember { mutableStateOf(false) }

    // Use a LaunchedEffect to read the value from DataStore when the composable enters the composition
    LaunchedEffect(key1 = Unit) {
        isOnboardingShown = readOnboardingStatus(context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    0f to PrimaryGreen,
                    1f to PrimaryGreenDark
                )
            )
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = modifier
                .fillMaxWidth()
                .weight(0.8f)
                .padding(horizontal = 16.dp)
        ) { page ->
            OnBoardingItem(onBoardList[page])
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.1f)
                .padding(horizontal = 16.dp)
        ) {
            repeat(onBoardList.size) { index ->
                val isSelected = pagerState.currentPage == index
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .width(if (isSelected) 18.dp else 8.dp)
                        .height(if (isSelected) 8.dp else 8.dp)
                        .border(
                            width = 1.dp,
                            color = Color(0xFF707784),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .background(
                            color = if (isSelected) Color(0xFF3B6C64) else Color(0xFFFFFFFF),
                            shape = CircleShape
                        )
                )
            }

        }

        Row(
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.1f)
                .padding(horizontal = 16.dp)
        ) {
            Button(
                onClick = {
                    val skipPage = pagerState.pageCount - 1
                    coroutineScope.launch {
                        pagerState.animateScrollToPage(skipPage)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryGreenLight,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Skip",
                    style = TextStyle(
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                    )
                )
            }
        }


    }
}