package com.example.pethelper.ui.account

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import com.example.pethelper.data.firebaseRepositories.FireStoreRepository
import com.example.pethelper.data.firebaseRepositories.UserSessionManager
import com.example.pethelper.ui.auth.RegUiState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AccountViewModel(val userManager: UserSessionManager, val fbRepository: FireStoreRepository): ViewModel() {
    var _uiState = MutableStateFlow(AccountUiState())
    var uiState: StateFlow<AccountUiState> = _uiState.asStateFlow()

    fun init() {

    }
}