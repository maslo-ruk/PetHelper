package com.example.pethelper.data.fireBaseEntities

import com.google.firebase.firestore.DocumentReference

data class FUser(
    val type:Int=0,
    val login: String="",
    val name:String="",
    val surname:String="",
    val phoneNumber:String="",
    val address:String="",
    val birthDate: String="",
    val photoId: String=""
)