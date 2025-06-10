package com.devpush.animeapp.features.main.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devpush.animeapp.domian.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel(private val userPreferencesRepository: UserPreferencesRepository) : ViewModel() {

    init {
        Timber.tag("MainViewModel").d("Initializing...")
    }

    val isLogin: StateFlow<Boolean?> = userPreferencesRepository.isLoginFlow
        .map { loginStatus ->
            Timber.tag("MainViewModel").d("isLoginFlow emitted: $loginStatus")
            loginStatus
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val isOnboardingShown: StateFlow<Boolean?> = userPreferencesRepository.isOnboardingShownFlow
        .map { onboardingStatus ->
            Timber.tag("MainViewModel").d("isOnboardingShownFlow emitted: $onboardingStatus")
            onboardingStatus
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    //TODO combine is not working. splash Screen is getting stuck.
//    val isInitialized: StateFlow<Boolean> = isLogin
//        .combine(isOnboardingShown) { login, onboarding -> // Explicit combine call
//            Timber.tag("MainViewModel").d("Combine: login=$login, onboarding=$onboarding")
//            val initialized = login != null && onboarding != null
//            Timber.tag("MainViewModel").d("isInitialized will be: $initialized")
//            initialized
//        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)


    fun saveIsOnboardingShown(value: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.updateOnboardingShownStatus(value)
        }
    }

    fun performLogout() {
        viewModelScope.launch {
            userPreferencesRepository.updateLoginStatus(false)
        }
    }

    fun userLoggedIn(isNewUser: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.updateLoginStatus(true)
            if (isNewUser) {
                userPreferencesRepository.updateOnboardingShownStatus(false)
            }
        }
    }

    fun onboardingCompleted() {
        viewModelScope.launch {
            userPreferencesRepository.updateOnboardingShownStatus(true)
        }
    }

}