package com.example.pethelper.data.repositories

import com.example.pethelper.data.entities.Pet
import kotlinx.coroutines.flow.Flow

interface PetsRepository {
    fun getAllPetsStream(): Flow<List<Pet>>

    fun getPetStream(id: Int): Flow<Pet>

    suspend fun insertPet(pet:Pet)
    suspend fun deletePet(pet:Pet)
    suspend fun updatePet(pet:Pet)
}