package com.example.pethelper.ui.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pethelper.data.fireBaseEntities.FUser
import com.example.pethelper.data.firebaseRepositories.FireStoreRepository
import com.example.pethelper.data.firebaseRepositories.UserSessionManager
import com.example.pethelper.ui.orders.OrderDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AccountChangeViewModel(val userManager: UserSessionManager, val fbRepository: FireStoreRepository): ViewModel() {
    var _uiState = MutableStateFlow(AccountUiState(user = userManager.currentUser.value!!.user))
    var uiState: StateFlow<AccountUiState> = _uiState.asStateFlow()

    fun updateUiState(details: FUser) {
        _uiState.update { it.copy(user = details) }
    }

    fun submit() {
        val state = uiState.value
        val user = state.user

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                fbRepository.updateUser(userManager.currentUser.value!!.uid, user)
                userManager.loadCurrentUser()
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        success = true
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Unknown error"
                    )
                }
            }
        }
    }
}