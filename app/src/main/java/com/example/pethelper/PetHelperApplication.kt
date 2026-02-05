package com.example.pethelper

import android.app.Application
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.pethelper.data.firebaseRepositories.AuthRepository
import com.example.pethelper.data.firebaseRepositories.FireStoreRepository
import com.example.pethelper.data.firebaseRepositories.UserSessionManager
import com.example.pethelper.data.session.AppSession
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import com.yandex.mapkit.MapKitFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PetHelperApplication : Application() {
    lateinit var sessionManager: UserSessionManager
        private set

    override fun onCreate() {
        super.onCreate()
        AppSession.init(Firebase.firestore)
        CoroutineScope(Dispatchers.IO).launch {
            AppSession.sessionManager.loadCurrentUser()
        }
        MapKitFactory.setApiKey("f42b771e-795f-4f7f-b7d0-edb44919d52c")
        MapKitFactory.initialize(this)
    }
}
