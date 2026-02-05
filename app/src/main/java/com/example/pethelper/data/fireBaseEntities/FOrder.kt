package com.example.pethelper.data.fireBaseEntities

import com.example.pethelper.data.enums.OrderStatus

data class FOrder (
    val id:String="",
    val pet: FPet = FPet(),
    val user: FUser = FUser(),
    val userId: String="",
    val helperId: String="",
    val address:String="",
    val date:String="",
    val time: String="",
    val price:Int=0,
    val notes: String="",
    val STATUS: String = OrderStatus.CREATED.toString()
)