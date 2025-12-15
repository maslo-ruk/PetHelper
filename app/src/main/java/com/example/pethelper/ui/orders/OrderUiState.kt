package com.example.pethelper.ui.orders

data class OrderUiState(
    val details: OrderDetails = OrderDetails(),
    val error:String = "",
    val isLoading: Boolean = false,
    val success: Boolean = false
)

data class OrderDetails(
    val id:Int = 0,
    val petId: Int = -1,
    val address:String = "",
    val date:String = "",
    val time: String = "",
    val price:Int = 0,
    val notes: String = "",
)