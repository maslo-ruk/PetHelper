package com.example.pethelper.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 0,
    val type:Int,
    val login: String,
    val password: String,
    val name:String,
    val surname:String,
    val phoneNumber:String,
    val birthDate: String
)