package com.example.pethelper

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.pethelper.data.AppContainer
import com.example.pethelper.data.AppDataContainer
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class PetHelperApplication : Application() {

    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}
