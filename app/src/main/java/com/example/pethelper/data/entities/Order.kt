package com.example.pethelper.data.entities
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class Orderr(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val petId: Int,
    val address:String ,
    val date:String,
    val time: String,
    val price:Int,
    val notes: String
)