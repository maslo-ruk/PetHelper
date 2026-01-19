package com.example.pethelper.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.pethelper.PetHelperApplication
import com.example.pethelper.data.firebaseRepositories.AuthRepository
import com.example.pethelper.data.session.AppSession
import com.example.pethelper.ui.auth.LoginViewModel
import com.example.pethelper.ui.auth.RegViewModel
import com.example.pethelper.ui.orders.OrderDialogViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            OrderDialogViewModel(AppSession.userRepository)
        }
        initializer {
            RegViewModel(AppSession.userRepository, AppSession.authRepository, AppSession.sessionManager)
        }
        initializer {
            LoginViewModel(AppSession.userRepository, AppSession.authRepository, AppSession.sessionManager)
        }
    }

}

fun CreationExtras.petApplication(): PetHelperApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as PetHelperApplication)