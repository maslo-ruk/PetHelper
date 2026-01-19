package com.example.pethelper.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pethelper.data.fireBaseEntities.FUser
import com.example.pethelper.data.firebaseRepositories.IAuthRepository
import com.example.pethelper.data.firebaseRepositories.FireStoreRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    val authRepository: IAuthRepository
): ViewModel() {
    var _uiState = MutableStateFlow(LoginUiState())
    var uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun updateUiState(details: LoginDetails) {
        _uiState.update { it.copy(details = details) }
    }

    fun submitRegistration() {
        val state = _uiState.value

        val details = state.details

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            authRepository.login(details.email, details.password)
                .onSuccess { uid ->
                    _uiState.update {
                        it.copy(isLoading = false, success = true)
                    }
                }
                .onFailure {
                        e -> _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Ошибка сохранения"
                    )
                }
                }
        }
    }
}