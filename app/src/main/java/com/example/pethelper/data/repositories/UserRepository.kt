package com.example.pethelper.data.repositories

import com.example.pethelper.data.entities.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getAllUserStream(): Flow<List<User>>

    fun getUserStream(id: Int): Flow<User?>

    suspend fun insertUser(user: User)

    suspend fun deleteUser(user: User)

    suspend fun updateUser(user: User)
}