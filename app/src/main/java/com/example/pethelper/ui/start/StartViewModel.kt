package com.example.pethelper.ui.start

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pethelper.data.firebaseRepositories.AuthRepository
import com.example.pethelper.data.firebaseRepositories.UserSessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StartViewModel(
    val userManager: UserSessionManager,
    val authRepository: AuthRepository
    ): ViewModel() {
    val _uiState = MutableStateFlow(StartUiState())
    val uiState: StateFlow<StartUiState> = _uiState.asStateFlow()
    val isLogged: StateFlow<Boolean> = authRepository.isLogged

    fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(state = "LoadUser") }
            userManager.loadCurrentUser()
            _uiState.update { it.copy(state = "Idle") }
        }
    }

    fun logout() {
        Log.d("AUTH", "LOGOGUT IN VIEWMODEL START")
        viewModelScope.launch {
            authRepository.logout()
            userManager.loadCurrentUser()
            Log.d("AUTH", "LOGOGUT IN VIEWMODEL FINISH")

        }
    }

}