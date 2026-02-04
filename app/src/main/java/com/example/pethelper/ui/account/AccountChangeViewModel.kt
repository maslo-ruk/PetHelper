package com.example.pethelper.ui.account

import androidx.lifecycle.ViewModel
import com.example.pethelper.data.fireBaseEntities.FUser
import com.example.pethelper.data.firebaseRepositories.FireStoreRepository
import com.example.pethelper.data.firebaseRepositories.UserSessionManager
import com.example.pethelper.ui.orders.OrderDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AccountChangeViewModel(val userManager: UserSessionManager, val fbRepository: FireStoreRepository): ViewModel() {
    var _uiState = MutableStateFlow(AccountUiState())
    var uiState: StateFlow<AccountUiState> = _uiState.asStateFlow()

    fun updateUiState(details: FUser) {
        _uiState.update { it.copy(user = details) }
    }

    fun submit() {

    }
}