package com.example.pethelper.data.entities
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "orders")
data class Order(
    @PrimaryKey(autoGenerate = true)
    val id: String = "",
    val petId: Int,
    val address:String ,
    val date:String,
    val time: String,
    val price:Int,
    val notes: String,
    val customerId: String = "",
    val status: String = "ACTIVE" // ACTIVE OR DONE
)