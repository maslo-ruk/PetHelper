package com.example.pethelper.ui.account

import com.example.pethelper.data.fireBaseEntities.FUser

data class AccountUiState(
    val uid:String = "",
    val user: FUser=FUser(),

    )