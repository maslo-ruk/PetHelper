package com.example.pethelper.data.fireBaseEntities

import com.google.firebase.Timestamp

data class Message(
    val id: String = "",
    val chatId: String = "",
    val senderId: String = "",
    val text: String = "",
    val createdAt: Timestamp? = null
)