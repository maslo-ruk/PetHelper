package com.example.pethelper.ui.account

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pethelper.data.fireBaseEntities.FPet
import com.example.pethelper.data.firebaseRepositories.FireStoreRepository
import com.example.pethelper.data.firebaseRepositories.UserSessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PetViewModel(val fbRepository: FireStoreRepository, val userManager: UserSessionManager, val petId: String): ViewModel() {
    var _uiState = MutableStateFlow(PetUiState())
    var uiState: StateFlow<PetUiState> = _uiState.asStateFlow()

    fun updateStateFlow(pet: FPet) {
        _uiState.update { it.copy(pet = pet) }
    }

    init {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            Log.d("PET", petId)
            val pet = fbRepository.getPet(petId)
            if (pet == null) {
                _uiState.update { it.copy(isLoading = false, error = "Питомец не найден") }
                Log.d("PET1234", "PET NOT FOUND")
            } else {
                _uiState.update { it.copy(pet = pet, isLoading = false, success = true) }
                Log.d("PET1234", "PET FOUND!!!!!")
            }
        }
    }

    fun deletePet() {
        viewModelScope.launch {
            fbRepository.deletePet(petId)
            _uiState.update { it.copy(isDeleted = true) }
        }
    }


}

data class PetUiState(
    val pet: FPet = FPet(),
    val error:String = "",
    val isLoading:Boolean = false,
    val isDeleted:Boolean = false,
    val success:Boolean = false
)
