package com.example.pethelper.ui.auth

data class LoginUiState(
    val details: LoginDetails= LoginDetails(),
    val error:String = "",
    val isLoading: Boolean = false,
    val success: Boolean = false
)

data class LoginDetails(
    val email:String ="",
    val password:String = ""
)