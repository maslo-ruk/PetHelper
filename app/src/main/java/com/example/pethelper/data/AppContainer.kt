package com.example.pethelper.data

import android.content.Context
import com.example.pethelper.data.repositories.OfflineUsersRepository
import com.example.pethelper.data.repositories.UserRepository

interface AppContainer {
    val itemsRepository: UserRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val itemsRepository: UserRepository by lazy {
        OfflineUsersRepository(MainDatabase.getDatabase(context).userDao())
    }
}