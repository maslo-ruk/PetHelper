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
            _uiState.update { it.copy(isLoading = true) }
            authRepository.register(user.login, details.password)
                .onSuccess { uid ->
                    fbRepository.addUser(uid, user)
                    userManager.loadCurrentUser()
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

fun RegDetails.toFUser(): FUser = FUser(
    type=role,
    login=email,
    name=firstName,
    surname=lastName,
    phoneNumber=phone,
    address=address,
    birthDate=birthDate
)