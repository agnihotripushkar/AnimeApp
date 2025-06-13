package com.devpush.animeapp.features.main.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devpush.animeapp.domian.repository.UserPreferencesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel(private val userPreferencesRepository: UserPreferencesRepository) : ViewModel() {

    val isOnboardingShown: StateFlow<Boolean?> = userPreferencesRepository.isOnboardingShownFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _isLogin = MutableStateFlow<Boolean?>(null)
    val isLogin: StateFlow<Boolean?> = _isLogin.asStateFlow()


    val isInitialized: StateFlow<Boolean> = _isLogin.map { loginStatus ->
        val ready = loginStatus != null
        ready
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = false
    )

    init {
        viewModelScope.launch {
            val loginStatusFromRepo = userPreferencesRepository.isLoginFlow.firstOrNull()
            _isLogin.value = loginStatusFromRepo ?: false
            Timber.tag("MainViewModel").d("Login status loaded: ${_isLogin.value}")
        }
    }

    fun saveIsOnboardingShown(value: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.updateOnboardingShownStatus(value)
        }
    }

}