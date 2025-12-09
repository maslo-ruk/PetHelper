package com.example.pethelper.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.pethelper.data.entities.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: User)
    @Update
    suspend fun update(item: User)
    @Delete
    suspend fun delete(item: User)

    @Query("SELECT * from users WHERE id = :id")
    fun getItem(id: Int): Flow<User>
}