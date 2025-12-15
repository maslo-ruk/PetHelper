package com.example.pethelper.data

import android.content.Context
import com.example.pethelper.data.repositories.OfflineOrderRepository
import com.example.pethelper.data.repositories.OfflinePetsRepository
import com.example.pethelper.data.repositories.OfflineUsersRepository
import com.example.pethelper.data.repositories.OrderRepository
import com.example.pethelper.data.repositories.PetsRepository
import com.example.pethelper.data.repositories.UserRepository

interface AppContainer {
    val usersRepository: UserRepository
    val petsRepository: PetsRepository
    val ordersRepository: OrderRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val usersRepository: UserRepository by lazy {
        OfflineUsersRepository(MainDatabase.getDatabase(context).userDao())
    }

    override val petsRepository: PetsRepository by lazy {
        OfflinePetsRepository(MainDatabase.getDatabase(context).petDao())
    }

    override val ordersRepository: OrderRepository by lazy {
        OfflineOrderRepository(MainDatabase.getDatabase(context).orderDao())
    }
}