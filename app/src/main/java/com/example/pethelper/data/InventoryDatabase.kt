package com.example.pethelper.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.pethelper.data.dao.UserDao
import com.example.pethelper.data.entities.User

@Database(entities = [User::class], version = 1, exportSchema = false)
abstract class MainDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var Instance: MainDatabase? = null

        fun getDatabase(context: Context): MainDatabase {
            return Instance ?: synchronized(this) { Room.databaseBuilder(context, MainDatabase::class.java, "item_database").fallbackToDestructiveMigration().build()
                .also { Instance = it }}
        }
    }
}