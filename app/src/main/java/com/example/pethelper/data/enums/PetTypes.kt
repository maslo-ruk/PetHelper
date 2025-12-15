package com.example.pethelper.data.enums

import androidx.room.TypeConverter

enum class PetTypes {
    DOG,
    CAT,
    PARROT,
    FISH,
    HAMSTER,
    CHINCHILLA,
    OTHER
}

class PetTypeConverter {

    @TypeConverter
    fun fromPetType(type: PetTypes): String = type.name

    @TypeConverter
    fun toPetType(value: String): PetTypes = PetTypes.valueOf(value)
}