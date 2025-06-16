package com.devpush.animeapp.features.settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devpush.animeapp.domian.repository.UserPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val userPreferencesRepository: UserPreferencesRepository): ViewModel() {

    val appTheme: StateFlow<String> = userPreferencesRepository.appThemeFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "system" // Default value, will be updated by the flow
        )

    val appLanguage: StateFlow<String> = userPreferencesRepository.appLanguageFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "en" // Default value, will be updated by the flow
        )

    fun setAppTheme(theme: String) {
        viewModelScope.launch(Dispatchers.IO) {
            userPreferencesRepository.updateAppTheme(theme)
        }
    }

    fun setAppLanguage(language: String) {
        viewModelScope.launch(Dispatchers.IO) {
            userPreferencesRepository.updateAppLanguage(language)
        }
    }

    fun performLogout() {
        viewModelScope.launch(Dispatchers.IO) {
            userPreferencesRepository.updateLoginStatus(false)
        }
    }

}