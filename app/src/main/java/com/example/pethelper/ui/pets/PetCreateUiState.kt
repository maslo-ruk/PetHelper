package com.example.pethelper.ui.pets

import com.example.pethelper.data.fireBaseEntities.FPet

data class PetCreateUiState(
    val details: FPet = FPet(),
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String = ""
)
