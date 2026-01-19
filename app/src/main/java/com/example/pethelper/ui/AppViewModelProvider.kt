package com.example.pethelper.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.pethelper.PetHelperApplication
import com.example.pethelper.data.firebaseRepositories.AuthRepository
import com.example.pethelper.ui.auth.LoginViewModel
import com.example.pethelper.ui.auth.RegViewModel
import com.example.pethelper.ui.orders.OrderDialogViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            OrderDialogViewModel(Firebase.firestore)
        }
        initializer {
            RegViewModel(Firebase.firestore, AuthRepository())
        }
        initializer {
            LoginViewModel(AuthRepository())
        }
    }

}

fun CreationExtras.petApplication(): PetHelperApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as PetHelperApplication)