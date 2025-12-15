package com.example.pethelper.data.repositories

import com.example.pethelper.data.entities.Pet
import com.example.pethelper.data.entities.User
import kotlinx.coroutines.flow.Flow

interface PetsRepository {
    fun getAllPetsStream(): Flow<List<Pet>>

    fun getPetStream(id: Int): Flow<Pet>
    fun getPetByNameStream(name:String): Flow<Pet>

    fun getUserStream(ownerId: Int): Flow<User>

    suspend fun insertPet(pet:Pet)
    suspend fun deletePet(pet:Pet)
    suspend fun updatePet(pet:Pet)

}