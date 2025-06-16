package com.devpush.animeapp.features.main.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.devpush.animeapp.R
import com.devpush.animeapp.core.navigation.Navigation
import com.devpush.animeapp.ui.theme.AnimeAppTheme
import com.devpush.animeapp.utils.applySelectedLanguage
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class MainActivity : ComponentActivity() {

    private val mainViewModel: MainViewModel by viewModel()
    private var currentActivityLanguage: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_AnimeApp)
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            val savedLanguage = mainViewModel.currentLanguage.first()
            if (currentActivityLanguage != savedLanguage) {
                Timber.tag("MainActivity").d("Applying initial locale: $savedLanguage")
                applySelectedLanguage(savedLanguage)
                currentActivityLanguage = savedLanguage
                // No recreate here, this is for initial setup before UI loads
            }
        }

        installSplashScreen().apply {
            setKeepOnScreenCondition {
                val isDataReady = mainViewModel.isInitialized.value
                !isDataReady
            }
        }

        enableEdgeToEdge()

        setContent {
            val currentTheme by mainViewModel.appTheme.collectAsState()
            val selectedLanguage by mainViewModel.currentLanguage.collectAsState()

            // Apply language and recreate if changed
            LaunchedEffect(selectedLanguage) {
                if (currentActivityLanguage != null && currentActivityLanguage != selectedLanguage) {
                    Timber.tag("MainActivity").d("Language changed from $currentActivityLanguage to $selectedLanguage. Recreating.")
                    // Update the language for the current context before recreating
                    // This ensures the recreation itself uses the new locale context if possible
                    applySelectedLanguage(selectedLanguage)
                    currentActivityLanguage = selectedLanguage // Update before recreate
                    recreate()
                } else if (currentActivityLanguage == null) {
                    // This handles the very first composition if onCreate launch didn't run first
                    // or if we want to ensure it's set before AnimeAppTheme composes
                    Timber.tag("MainActivity").d("LaunchedEffect: Setting initial language: $selectedLanguage")
                    applySelectedLanguage(selectedLanguage)
                    currentActivityLanguage = selectedLanguage
                }
            }

            AnimeAppTheme(userThemePreference = currentTheme) {
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