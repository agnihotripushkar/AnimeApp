package com.devpush.animeapp.features.auth.ui.biometric

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BiometricAuthViewModel : ViewModel() {

    private val _state = MutableStateFlow(BiometricAuthState())
    val state = _state.asStateFlow()

    private val _events = Channel<BiometricAuthEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: BiometricAuthAction) {
        when (action) {
            is BiometricAuthAction.StatusChanged -> handleStatusChange(action.status)
            is BiometricAuthAction.Retry -> _state.update { it.copy(authStatus = BiometricAuthStatus.IDLE) }
        }
    }

    private fun handleStatusChange(status: BiometricAuthStatus) {
        _state.update { it.copy(authStatus = status) }
        when (status) {
            BiometricAuthStatus.SUCCESS -> viewModelScope.launch { _events.send(BiometricAuthEvent.NavigateToHome) }
            BiometricAuthStatus.CANCELLED, BiometricAuthStatus.ERROR -> viewModelScope.launch { _events.send(BiometricAuthEvent.NavigateToLogin) }
            else -> {}
        }
    }
}
