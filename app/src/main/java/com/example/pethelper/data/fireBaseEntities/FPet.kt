package com.example.pethelper.data.fireBaseEntities

import com.example.pethelper.data.enums.PetTypes

data class FPet(
    val id:String="",
    val name: String="",
    val age: Int=0,
    val type: PetTypes= PetTypes.DOG,
    val description: String="",

    val ownerId: Int=0
)