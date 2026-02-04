package com.example.pethelper.ui.account

import androidx.lifecycle.ViewModel
import com.example.pethelper.data.fireBaseEntities.FPet
import com.example.pethelper.data.firebaseRepositories.FireStoreRepository
import com.example.pethelper.data.firebaseRepositories.UserSessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PetViewModel(): ViewModel() {
    var _uiState = MutableStateFlow(PetUiState())
    var uiState: StateFlow<PetUiState> = _uiState.asStateFlow()

    fun updateStateFlow(pet: FPet) {
        _uiState.update { it.copy(pet = pet) }
    }
}

data class PetUiState(
    val pet: FPet = FPet()
)