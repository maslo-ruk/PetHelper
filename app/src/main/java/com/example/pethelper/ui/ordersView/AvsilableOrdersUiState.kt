package com.example.pethelper.ui.ordersView

import com.example.pethelper.data.fireBaseEntities.FOrder

data class AvsilableOrdersUiState(
    val orders: List<FOrder> = listOf(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)
