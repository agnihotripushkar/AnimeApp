package com.devpush.animeapp.features.main.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.devpush.animeapp.R
import com.devpush.animeapp.core.navigation.Navigation
import com.devpush.animeapp.ui.theme.AnimeAppTheme
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_AnimeApp)
        super.onCreate(savedInstanceState)

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                val isDataReady = mainViewModel.isInitialized.value
                !isDataReady
            }
        }

        enableEdgeToEdge()

        setContent {
            AnimeAppTheme {
                val isInitialized by mainViewModel.isInitialized.collectAsState()
                if (isInitialized) {
                    Navigation(mainViewModel = mainViewModel)
                } else {
                    Timber.tag("MainActivity")
                        .d("setContent: Showing nothing as still initializing (splash should be visible).")
                }
            }
        }
    }
}