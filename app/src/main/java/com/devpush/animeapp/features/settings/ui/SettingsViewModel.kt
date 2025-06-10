package com.devpush.animeapp.features.settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devpush.animeapp.domian.repository.UserPreferencesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SettingsViewModel(private val userPreferencesRepository: UserPreferencesRepository): ViewModel() {

    fun performLogout() {
        viewModelScope.launch(Dispatchers.IO) {
            userPreferencesRepository.updateLoginStatus(false)
        }
    }

}