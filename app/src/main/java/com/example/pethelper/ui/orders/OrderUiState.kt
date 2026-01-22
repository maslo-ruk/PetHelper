package com.example.pethelper.ui.orders

import com.example.pethelper.data.fireBaseEntities.FPet

data class OrderUiState(
    val details: OrderDetails = OrderDetails(),
    val error:String = "",
    val isLoading: Boolean = false,
    val success: Boolean = false
)

data class OrderDetails(
    val id:String="",
    val pet: FPet = FPet(),
    val userId: String="",
    val address:String="",
    val date:String="",
    val time: String="",
    val price:Int=0,
    val notes: String="",
)