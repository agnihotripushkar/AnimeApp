package com.devpush.animeapp.presentation.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.devpush.animeapp.presentation.components.onBoarding.OnBoardingItem
import com.devpush.animeapp.R
import com.devpush.animeapp.presentation.components.onBoarding.OnBoardingModel
import com.devpush.animeapp.presentation.ui.theme.PrimaryGreen
import com.devpush.animeapp.presentation.ui.theme.PrimaryGreenDark
import com.devpush.animeapp.presentation.ui.theme.PrimaryGreenLight
import com.devpush.animeapp.utils.DataStoreUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


fun goToLastPage(pagerState: PagerState, coroutineScope: CoroutineScope) {
    val skipPage = pagerState.pageCount - 1
    coroutineScope.launch {
        pagerState.animateScrollToPage(skipPage)
    }
}

fun goToNextPage(
    pagerState: PagerState,
    coroutineScope: CoroutineScope,
    onGetStartedClicked: () -> Unit
) {
    val currPage = pagerState.currentPage
    if (currPage < pagerState.pageCount - 2) {
        val skipPage = pagerState.currentPage + 1
        coroutineScope.launch {
            pagerState.animateScrollToPage(skipPage)
        }
    } else {
        goToHomepage(onGetStartedClicked)
    }
}

fun goToHomepage(onGetStartedClicked: () -> Unit) {
    onGetStartedClicked
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun OnBoardingScreen(
    onGetStartedClicked: () -> Unit,
    modifier: Modifier = Modifier
) {

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
//    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    // read onboarding status from DataStore
//    val isOnboardingCompleted = DataStoreUtils.readOnboardingStatus(context)

    // Use a LaunchedEffect to read the value from DataStore when the composable enters the composition
    LaunchedEffect(key1 = Unit) {
        DataStoreUtils.updateOnboardingStatus(context, true)
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
                onClick = onGetStartedClicked,
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