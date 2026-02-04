package com.example.pethelper.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.pethelper.data.enums.PetTypes

@Entity(tableName = "pets",
        foreignKeys = [ForeignKey(entity = User::class,
        parentColumns = ["id"],
        childColumns = ["ownerId"],
        onDelete = ForeignKey.CASCADE)],
    indices = [Index("ownerId")])
data class Pet(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val age: Int,
    val type: PetTypes,
    val description: String,

    val ownerId: Int
)