package com.devpush.animeapp.features.settings.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devpush.animeapp.domian.repository.UserPreferencesRepository
import com.devpush.animeapp.features.auth.ui.biometric.BiometricAuthStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(private val userPreferencesRepository: UserPreferencesRepository) : ViewModel() {

    private val _authStatus = MutableStateFlow(BiometricAuthStatus.IDLE)

    val state = combine(
        userPreferencesRepository.appThemeFlow,
        userPreferencesRepository.appLanguageFlow,
        userPreferencesRepository.isBiometricAuthEnabledFlow,
        _authStatus
    ) { theme, language, biometricEnabled, authStatus ->
        SettingsState(
            appTheme = theme,
            appLanguage = language,
            isBiometricAuthEnabled = biometricEnabled,
            authStatus = authStatus
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = SettingsState()
    )

    private val _events = Channel<SettingsEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: SettingsAction) {
        when (action) {
            is SettingsAction.SetTheme -> viewModelScope.launch(Dispatchers.IO) {
                userPreferencesRepository.updateAppTheme(action.theme)
            }
            is SettingsAction.SetLanguage -> viewModelScope.launch(Dispatchers.IO) {
                userPreferencesRepository.updateAppLanguage(action.language)
            }
            is SettingsAction.SetBiometricEnabled -> viewModelScope.launch(Dispatchers.IO) {
                userPreferencesRepository.updateBiometricAuthEnabled(action.enabled)
            }
            is SettingsAction.UpdateAuthStatus -> _authStatus.update { action.status }
            is SettingsAction.Logout -> viewModelScope.launch(Dispatchers.IO) {
                userPreferencesRepository.updateLoginStatus(false)
                _events.send(SettingsEvent.NavigateToLogin)
            }
        }
    }
}
