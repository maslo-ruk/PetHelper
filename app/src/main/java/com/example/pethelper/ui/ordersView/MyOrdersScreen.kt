package com.example.pethelper.ui.ordersView

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pethelper.data.fireBaseEntities.FOrder
import com.example.pethelper.ui.AppViewModelProvider


@Composable
fun MyOrders(viewModel: AvailableOrdersViewModel = viewModel(factory = AppViewModelProvider.Factory)) {
    val orders by viewModel.orders.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.startObservingOrders()
    }


    LazyColumn {
        items(orders) { order ->
            OrderCard(order) }
    }
}


