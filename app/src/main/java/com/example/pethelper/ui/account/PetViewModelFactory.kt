package com.example.pethelper.ui.account

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pethelper.data.session.AppSession
import com.example.pethelper.ui.chat.ChatScreenViewModel

class PetViewModelFactory(

    private val petId: String
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PetViewModel(petId = petId, userManager = AppSession.sessionManager,fbRepository = AppSession.userRepository) as T
    }
}