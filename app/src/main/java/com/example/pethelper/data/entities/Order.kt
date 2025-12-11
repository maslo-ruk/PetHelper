package com.example.pethelper.data.entities
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class Orderr(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    )