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

class RegViewModel(
    val fbRepository: FireStoreRepository,
    val authRepository: IAuthRepository,
    val userManager: UserSessionManager
    ): ViewModel() {
    var _uiState = MutableStateFlow(RegUiState())
    var uiState: StateFlow<RegUiState> = _uiState.asStateFlow()

    fun updateUiState(details: RegDetails) {
        _uiState.update { it.copy(details = details) }
    }

    fun submitRegistration() {
        val state = _uiState.value

        val user = state.details.toFUser()
        val details = state.details

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = "") }
            authRepository.register(user.login, details.password)
                .onSuccess { authRepository.sendEmailVerification().onSuccess {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            verificationEmailSent = true,
                            waitingForEmailVerification = true,
                            error = ""
                        )
                    }
                }
                    .onFailure {e ->
                        authRepository.logout()
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = e.message ?: "Не удалось отправить письмо подтверждения"
                            )
                        }
                    }
//                    uid ->
//                    fbRepository.addUser(uid, user)
//                    userManager.loadCurrentUser()
//                    _uiState.update {
//                        it.copy(isLoading = false, success = true)
//                    }
                }
                .onFailure {
                    e -> _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = e.message ?: "Ошибка регистрации"
                        )
                    }
                }
        }
    }
    fun checkEmailVerifiedAndFinish() {
        val state = _uiState.value
        val user = state.details.toFUser()
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = "") }

            authRepository.reloadAndCheckEmailVerified()
                .onSuccess { verified ->
                    if (!verified) {
                        _uiState.update { it.copy(isLoading = false, error ="Почта не подтверждена") }
                        return@onSuccess
                    }
                    val uid = authRepository.getCurrentUserId()
                    if (uid == null) {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = "Не удалось получить пользователя. Попробуйте войти заново."
                            )
                        }
                        return@onSuccess
                    }
                    runCatching {
                        fbRepository.addUser(uid, user)
                        userManager.loadCurrentUser()
                    }.onSuccess {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                success = true,
                                waitingForEmailVerification = false
                            )
                        }
                    }.onFailure { e ->
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                error = e.message ?: "Ошибка сохранения профиля"
                            )
                        }
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = e.message ?: "Ошибка проверки подтверждения"
                        )
                    }
                }

        }
}
    fun resendVerificationEmail() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = "") }
            authRepository.sendEmailVerification()
                .onSuccess {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            verificationEmailSent = true,
                            error = ""
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = e.message ?: "Не удалось отправить письмо повторно"
                        )
                    }
                }
        }
    }
}