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
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pethelper.data.fireBaseEntities.FPet
import com.example.pethelper.data.fireBaseEntities.FUser
import com.example.pethelper.ui.AppViewModelProvider
import com.example.pethelper.ui.account.PetListItem
import com.example.pethelper.ui.orders.OrderDialogViewModel
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import com.example.pethelper.data.fireBaseEntities.FOrder


@Composable
fun AvailableOrders(viewModel: AvailableOrdersViewModel = viewModel(factory = AppViewModelProvider.Factory)) {
    val orders by viewModel.orders.collectAsState()

    LazyColumn {
        items(orders) { order ->
            OrderCard(order) }
        }
    }


@Composable
fun OrderCard(order: FOrder) {

    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Box() {
            Text(text = "${order.user.name} ${order.user.surname}")
            Text(order.notes)
            Text(text = order.address)
        }
        Box() {
            Text(text = order.date)
            Text(text = order.price.toString())
        }
    }
}