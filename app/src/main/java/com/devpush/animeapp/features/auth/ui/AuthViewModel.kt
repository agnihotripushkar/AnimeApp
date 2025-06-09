package com.devpush.animeapp.features.auth.ui

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devpush.animeapp.features.auth.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val dataStore: DataStore<Preferences>,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _isRegister = MutableStateFlow(false)
    val isRegister: StateFlow<Boolean> = _isRegister.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()


    fun loginCall(email: String, password: String) {
        // check validation here before api call
        _isLoading.value = true

        viewModelScope.launch(Dispatchers.IO) {
            // Call the login API
            //authRepository.loginCall()
            delay(3000)
            // If login is successful:
//            dataStore.edit { preferences ->
//                preferences[booleanPreferencesKey(Constants.IS_LOGIN)] = true
//            }
            _isLoggedIn.value = true
            _isLoading.value = false
        }
    }

    fun registerCall(email: String, password: String) {
        _isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            delay(3000)
//            dataStore.edit { preferences ->
//                preferences[booleanPreferencesKey(Constants.IS_LOGIN)] = true
//            }
            _isRegister.value = true
            _isLoading.value = false
        }
    }
}