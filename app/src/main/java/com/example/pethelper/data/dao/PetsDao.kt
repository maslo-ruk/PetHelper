package com.example.pethelper.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.pethelper.data.entities.Pet
import com.example.pethelper.data.entities.User
import kotlinx.coroutines.flow.Flow

@Dao
interface PetsDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Pet)
    @Update
    suspend fun update(item: Pet)
    @Delete
    suspend fun delete(item: Pet)

    @Query("SELECT * from pets WHERE id = :id")
    fun getPet(id: Int): Flow<Pet>

    @Query("SELECT * FROM pets WHERE name = :name")
    fun getPetByName(name:String): Flow<Pet>

    @Query("SELECT * from pets")
    fun getAllPets(): Flow<List<Pet>>

    @Query("SELECT * FROM users WHERE id = :ownerId")
    fun getOwner(ownerId: Int): Flow<User>
}