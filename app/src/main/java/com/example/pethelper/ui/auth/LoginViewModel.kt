package com.example.pethelper.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pethelper.data.fireBaseEntities.FUser
import com.example.pethelper.data.firebaseRepositories.IAuthRepository
import com.example.pethelper.data.firebaseRepositories.FireStoreRepository
import com.example.pethelper.data.firebaseRepositories.UserSessionManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginViewModel(
    val fbRepository: FireStoreRepository,
    val authRepository: IAuthRepository,
    val userManager: UserSessionManager
): ViewModel() {
    private var _uiState = MutableStateFlow(LoginUiState())
    var uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun updateUiState(details: LoginDetails) {
        _uiState.update {
            it.copy(
                details = details,
                error = "",
                needsEmailVerification = false
            )
        }
    }

    fun submitRegistration() {
        val details = _uiState.value.details

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = "") }

            authRepository.login(details.email, details.password)
                .onSuccess {
                    userManager.loadCurrentUser()
                    _uiState.update { it.copy(isLoading = false, success = true) }
                }
                .onFailure { e ->
                    val msg = e.message ?: "Ошибка входа"

                    if (msg == "Email not verified") {
                        // Важно: пользователь уже разлогинен внутри authRepository.login()
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                needsEmailVerification = true,
                                error = "Почта не подтверждена. Проверьте письмо и перейдите по ссылке."
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = msg
                            )
                        }
                    }
                }
        }
    }
    fun resendVerificationEmail() {
        val details = _uiState.value.details
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = "") }

            authRepository.loginWithoutEmailCheck(details.email, details.password)
                .onSuccess {
                    authRepository.sendEmailVerification()
                        .onSuccess {
                            authRepository.logout()
                            _uiState.update {
                                it.copy(
                                    isLoading = false,
                                    error = "Письмо отправлено повторно. Проверьте почту."
                                )
                            }
                        }
                        .onFailure { e ->
                            authRepository.logout()
                            _uiState.update {
                                it.copy(isLoading = false, error = e.message ?: "Не удалось отправить письмо")
                            }
                        }
                }
                .onFailure { e ->
                    _uiState.update { it.copy(isLoading = false, error = e.message ?: "Ошибка входа") }
                }
        }
    }
}