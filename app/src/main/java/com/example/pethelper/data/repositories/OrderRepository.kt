package com.example.pethelper.data.repositories

import com.example.pethelper.data.entities.Orderr
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    fun getAllOrderStream(): Flow<List<Orderr>>

    fun getOrderStream(id: Int): Flow<Orderr?>

    suspend fun insertOrder(user: Orderr)

    suspend fun deleteOrder(user: Orderr)

    suspend fun updateOrder(user: Orderr)
}