package com.example.pethelper.data.repositories

import com.example.pethelper.data.dao.PetsDao
import com.example.pethelper.data.entities.Orderr
import com.example.pethelper.data.entities.Pet
import kotlinx.coroutines.flow.Flow

class OfflinePetsRepository(private val petsDao: PetsDao) : PetsRepository {
    override fun getAllPetsStream(): Flow<List<Pet>> = petsDao.getAllPets()
    override fun getPetStream(id: Int): Flow<Pet> = petsDao.getPet(id)
    override suspend fun insertPet(pet: Pet) = petsDao.insert(pet)
    override suspend fun updatePet(pet: Pet) = petsDao.update(pet)
    override suspend fun deletePet(pet: Pet) = petsDao.delete(pet)
}