package com.example.pethelper.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.pethelper.data.dao.OrdersDao
import com.example.pethelper.data.dao.PetsDao
import com.example.pethelper.data.dao.UserDao
import com.example.pethelper.data.entities.Orderr
import com.example.pethelper.data.entities.Pet
import com.example.pethelper.data.entities.User
import com.example.pethelper.data.enums.PetTypeConverter

@Database(entities = [User::class, Pet::class, Orderr::class], version = 1, exportSchema = false)
@TypeConverters(PetTypeConverter::class)
abstract class MainDatabase: RoomDatabase() {
    abstract fun userDao(): UserDao

    abstract fun petDao(): PetsDao

    abstract fun orderDao(): OrdersDao

    companion object {
        @Volatile
        private var Instance: MainDatabase? = null

        fun getDatabase(context: Context): MainDatabase {
            return Instance ?: synchronized(this) { Room.databaseBuilder(context, MainDatabase::class.java, "item_database").fallbackToDestructiveMigration().build()
                .also { Instance = it }}
        }
    }
}