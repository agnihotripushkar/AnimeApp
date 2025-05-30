package com.devpush.animeapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.devpush.animeapp.navigation.Navigation
import com.devpush.animeapp.presentation.ui.theme.WeatherAppTheme

class MainActivity : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_AnimeApp)
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                // Keep splash screen until authViewModel.isInitialized is true
                !authViewModel.isInitialized.value
            }
        }
        enableEdgeToEdge()
        setContent {
            WeatherAppTheme {
                Navigation(authViewModel) // Pass the updated ViewModel
            }
        }
    }
}
