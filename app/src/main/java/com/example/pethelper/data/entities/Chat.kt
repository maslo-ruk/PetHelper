package com.example.pethelper.data.entities

data class Chat(
    val id: String = "",
    val orderId: String = "",
    val participants: List<String> = emptyList(),
    val status: String = "ACTIVE", // ACTIVE or CLOSED
    val lastMessage: String = "",
)