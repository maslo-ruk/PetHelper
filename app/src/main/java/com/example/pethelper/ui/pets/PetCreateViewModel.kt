package com.example.pethelper.ui.pets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pethelper.data.fireBaseEntities.FPet
import com.example.pethelper.data.firebaseRepositories.FireStoreRepository
import com.example.pethelper.data.firebaseRepositories.UserSessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PetCreateViewModel(val userManager: UserSessionManager, val fbRepository: FireStoreRepository): ViewModel() {
    val _uiState = MutableStateFlow(PetCreateUiState())
    val uiState: StateFlow<PetCreateUiState> = _uiState.asStateFlow()

    fun updateStateFlow(details: FPet) {
        _uiState.update { it.copy(details = details) }
    }

    fun submitPet() {
        val state = _uiState.value
        val pet = state.details

        viewModelScope.launch {
            _uiState.update {it.copy(isLoading = true)}

            try {
                fbRepository.addPet(userManager.currentUser.value!!.uid, pet)

                _uiState.update {
                    it.copy(isLoading = false, isSuccess = true)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, error = e.message ?: "Ошибка сохранения")
                }
            }
        }
    }
}