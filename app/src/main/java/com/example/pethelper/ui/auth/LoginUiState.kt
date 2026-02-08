package com.example.pethelper.ui.auth

data class LoginUiState(
    val details: LoginDetails= LoginDetails(),
    val error:String = "",
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val verificationEmailSent: Boolean = false,
    val waitingForEmailVerification: Boolean = false,
    val needsEmailVerification: Boolean = false
)

data class LoginDetails(
    val email:String ="",
    val password:String = ""
)