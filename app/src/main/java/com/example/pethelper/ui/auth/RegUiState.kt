package com.example.pethelper.ui.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.pethelper.data.fireBaseEntities.FUser


data class RegUiState(
    val details: RegDetails= RegDetails(),
    val error:String = "",
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val verificationEmailSent: Boolean = false,
    val waitingForEmailVerification: Boolean = false
)

data class RegDetails(
    val email:String = "",
    val password:String = "",
    val role:Int = 0,
    val firstName:String = "",
    val lastName:String = "",
    val phone:String = "",
    val address:String = "",
    val birthDate:String = ""
)

fun RegDetails.toFUser(): FUser = FUser(
    type = role,
    login = email,
    name = firstName,
    surname = lastName,
    phoneNumber = phone,
    address = address,
    birthDate = birthDate
)

