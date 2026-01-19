package com.example.pethelper.ui.account

import androidx.lifecycle.ViewModel
import com.example.pethelper.ui.auth.RegUiState
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AccountViewModel(val auth: FirebaseAuth): ViewModel() {
    var _uiState = MutableStateFlow(AccountUiState())
    var uiState: StateFlow<AccountUiState> = _uiState.asStateFlow()

    fun init() {

    }
}