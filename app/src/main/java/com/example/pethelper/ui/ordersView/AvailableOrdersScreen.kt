package com.example.pethelper.ui.ordersView

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import com.example.pethelper.data.fireBaseEntities.FPet
import com.example.pethelper.data.fireBaseEntities.FUser
import com.example.pethelper.ui.account.PetListItem


@Composable
fun AvailableOrders() {
    val orders: MutableList<String> = mutableListOf("names")
    LazyColumn {
        items(orders) { order ->
            OrderCard() }
        }
    }


@Composable
fun OrderCard() {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Box() {
            Text(text = "Имя Фамилия Пользователя")
            Text("Заказ инфо")
            Text(text = "Адрес")
        }
        Box() {
            Text(text = "Дата")
            Text(text = "Цена")
        }
    }
}