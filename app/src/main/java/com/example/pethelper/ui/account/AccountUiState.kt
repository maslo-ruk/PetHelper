package com.example.pethelper.ui.account

import com.example.pethelper.data.fireBaseEntities.FUser

data class AccountUiState(
    val user: FUser=FUser(),
    val isLoading:Boolean = false,
    val success: Boolean = false,
    val error:String = ""
    )