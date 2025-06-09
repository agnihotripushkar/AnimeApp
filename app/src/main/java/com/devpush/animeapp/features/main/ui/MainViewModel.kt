package com.devpush.animeapp.features.main.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.devpush.animeapp.utils.Constants
import com.devpush.animeapp.utils.DataStoreUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val _isLogin = MutableStateFlow<Boolean?>(null)
    val isLogin: StateFlow<Boolean?> = _isLogin

    private val _isOnboardingShown = MutableStateFlow<Boolean?>(null)
    val isOnboardingShown: StateFlow<Boolean?> = _isOnboardingShown

    private val _isInitialized = MutableStateFlow(false)
    val isInitialized: StateFlow<Boolean> = _isInitialized

    init {
        viewModelScope.launch {
            val context = getApplication<Application>().applicationContext
            DataStoreUtils.observeBooleanPreference(context, Constants.IS_LOGIN)
                .collect {
                    _isLogin.value = it
                }
        }

        viewModelScope.launch {
            val context = getApplication<Application>().applicationContext
            DataStoreUtils.observeBooleanPreference(context, Constants.IS_ONBOARDING_SHOWN)
                .collect {
                    _isOnboardingShown.value = it
                }
        }

        // Set initialized once both values are non-null
        viewModelScope.launch {
            combine(
                isLogin,
                isOnboardingShown
            ) { login, onboarding ->
                login != null && onboarding != null
            }.collect { ready ->
                _isInitialized.value = ready
            }
        }
    }
}