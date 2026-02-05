package com.example.pethelper.data.fireBaseEntities

import com.google.firebase.Timestamp

data class Chat(
    val id: String = "",
    val orderId: String = "",
    val participants: List<String> = emptyList(),
    val status: String = "ACTIVE", // ACTIVE or CLOSED
    val lastMessage: String = "",
    val lastMessageAt: Timestamp? = null,
    val createdAt: Timestamp? = null
)