package com.devpush.animeapp.features.main.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.devpush.animeapp.R
import com.devpush.animeapp.core.navigation.Navigation
import com.devpush.animeapp.ui.theme.WeatherAppTheme

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_AnimeApp)
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                // Keep splash screen until authViewModel.isInitialized is true
                mainViewModel.isOnboardingShown.value?.not() == true
            }
        }
        enableEdgeToEdge()
        setContent {
            WeatherAppTheme {
                Navigation(mainViewModel)
            }
        }
    }
}