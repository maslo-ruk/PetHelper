package com.example.pethelper.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.pethelper.data.entities.Orderr
import com.example.pethelper.data.entities.User
import kotlinx.coroutines.flow.Flow

@Dao
interface OrdersDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: Orderr)
    @Update
    suspend fun update(item: Orderr)
    @Delete
    suspend fun delete(item: Orderr)

    @Query("SELECT * from orders WHERE id = :id")
    fun getUser(id: Int): Flow<Orderr>

    @Query("SELECT * from orders")
    fun getAllUsers(): Flow<List<Orderr>>
}