package com.example.pethelper.data.fireBaseEntities

data class FOrder (
    val id:String="",
    val petId: String="",
    val userId: String="",
    val address:String="",
    val date:String="",
    val time: String="",
    val price:Int=0,
    val notes: String=""
)