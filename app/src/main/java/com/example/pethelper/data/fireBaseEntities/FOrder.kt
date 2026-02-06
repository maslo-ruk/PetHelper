package com.example.pethelper.data.fireBaseEntities

import com.example.pethelper.data.enums.OrderStatus
import com.google.firebase.Timestamp

data class FOrder (
    val id:String="",
    val pet: FPet = FPet(),
    val user: FUser = FUser(),
    val userId: String="",
    val workerId: String="",
    val address:String="",
    val date:String="",
    val time: String="",
    val price:Int=0,
    val notes: String="",
    val status: String = OrderStatus.CREATED.toString(),
    val createdAt: Timestamp? = null,
    val closedAt: Timestamp? = null
)