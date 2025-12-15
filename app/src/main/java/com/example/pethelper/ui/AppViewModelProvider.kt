package com.example.pethelper.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.pethelper.PetHelperApplication
import com.example.pethelper.ui.orders.OrderDialogViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            OrderDialogViewModel(
                petApplication().container.ordersRepository,
                petApplication().container.petsRepository
            )
        }
    }
}

fun CreationExtras.petApplication(): PetHelperApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as PetHelperApplication)