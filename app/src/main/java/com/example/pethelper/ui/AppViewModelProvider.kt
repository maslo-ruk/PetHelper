package com.example.pethelper.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.pethelper.PetHelperApplication
import com.example.pethelper.data.firebaseRepositories.AuthRepository
import com.example.pethelper.data.session.AppSession
import com.example.pethelper.ui.account.AccountChangeViewModel
import com.example.pethelper.ui.account.AccountViewModel
import com.example.pethelper.ui.account.PetViewModel
import com.example.pethelper.ui.auth.LoginViewModel
import com.example.pethelper.ui.auth.RegViewModel
import com.example.pethelper.ui.chat.ChatListViewModel
import com.example.pethelper.ui.orders.OrderDialogViewModel
import com.example.pethelper.ui.ordersView.AvailableOrders
import com.example.pethelper.ui.ordersView.AvailableOrdersViewModel
import com.example.pethelper.ui.pets.PetCreateViewModel
import com.example.pethelper.ui.start.StartViewModel
import com.example.pethelper.ui.walk.WalkViewModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            val app = petApplication()
            OrderDialogViewModel(
                AppSession.userRepository,
                AppSession.sessionManager
            )
        }
        initializer {
            val app = petApplication()
            RegViewModel(AppSession.userRepository, AppSession.authRepository, AppSession.sessionManager)
        }
        initializer {
            val app = petApplication()
            LoginViewModel(AppSession.userRepository, AppSession.authRepository, AppSession.sessionManager)
        }
        initializer {
            val app = petApplication()
            AccountViewModel(AppSession.sessionManager, AppSession.userRepository)
        }
        initializer {
            val app = petApplication()
            PetCreateViewModel(AppSession.sessionManager, AppSession.userRepository)
        }
        initializer {
            val app = petApplication()
            StartViewModel(AppSession.sessionManager, AppSession.authRepository)
        }
        initializer {
            val app = petApplication()
            AccountChangeViewModel(AppSession.sessionManager, AppSession.userRepository)
        }
        initializer {
            PetViewModel()
        }
        initializer {
            AvailableOrdersViewModel(AppSession.userRepository, AppSession.sessionManager, chatRepository = AppSession.chatRepository)
        }
        initializer {
            ChatListViewModel(AppSession.chatRepository, AppSession.sessionManager)
        }
    }

}

fun CreationExtras.petApplication(): PetHelperApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as PetHelperApplication)