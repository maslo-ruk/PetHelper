package com.example.pethelper.ui.account

import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pethelper.data.firebaseRepositories.AuthRepository
import com.example.pethelper.data.firebaseRepositories.FireStoreRepository
import com.example.pethelper.data.firebaseRepositories.UserSessionManager
import com.example.pethelper.ui.auth.RegUiState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AccountViewModel(
    val userManager: UserSessionManager,
    val fbRepository: FireStoreRepository,
    val authRepository: AuthRepository
): ViewModel() {
    var _uiState = MutableStateFlow(AccountUiState())
    var uiState: StateFlow<AccountUiState> = _uiState.asStateFlow()
    val isLogged: StateFlow<Boolean> = authRepository.isLogged

    fun logout() {
        Log.d("AUTH", "LOGOGUT IN VIEWMODEL START")
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            authRepository.logout()
            userManager.loadCurrentUser()
            _uiState.update { it.copy(isLoading = false, success = true) }
            Log.d("AUTH", "LOGOGUT IN VIEWMODEL FINISH")

        }
    }
}