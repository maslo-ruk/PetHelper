package com.example.pethelper.data.repositories

import com.example.pethelper.data.dao.PetsDao
import com.example.pethelper.data.entities.Orderr
import com.example.pethelper.data.entities.Pet
import com.example.pethelper.data.entities.User
import kotlinx.coroutines.flow.Flow

class OfflinePetsRepository(private val petsDao: PetsDao) : PetsRepository {
    override fun getAllPetsStream(): Flow<List<Pet>> = petsDao.getAllPets()
    override fun getPetStream(id: Int): Flow<Pet> = petsDao.getPet(id)
    override fun getPetByNameStream(name:String): Flow<Pet> = petsDao.getPetByName(name)
    override fun getUserStream(ownerId: Int): Flow<User> = petsDao.getOwner(ownerId)
    override suspend fun insertPet(pet: Pet) = petsDao.insert(pet)
    override suspend fun updatePet(pet: Pet) = petsDao.update(pet)
    override suspend fun deletePet(pet: Pet) = petsDao.delete(pet)
}