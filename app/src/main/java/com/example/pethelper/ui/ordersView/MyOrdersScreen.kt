package com.example.pethelper.ui.ordersView

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pethelper.data.fireBaseEntities.FOrder
import com.example.pethelper.ui.AppViewModelProvider


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyOrders(
    viewModel: MyOrdersViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onBack:()->Unit,
    onOrderClick:(order:FOrder)->Unit = viewModel::deleteOrder
) {
    val orders by viewModel.orders.collectAsState()
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.startObservingOrders()
    }

    Scaffold(topBar = { TopAppBar(title = {Text("Мои заказы")},
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(painterResource(id = android.R.drawable.ic_media_previous), contentDescription = "Назад")
            }
        }) }) { innerPadding ->
        LaunchedEffect(Unit) {
            viewModel.startObservingOrders()
        }
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(orders) { order ->
                MyOrderCard(order, onOrderClick) }
        }
    }
}

@Composable
fun MyOrderCard(order: FOrder, onOrderClick:(order: FOrder)->Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Box() {
                    Column {
                        Text(
                            text = "Статус: ${remakeStatus(order.status)}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                        )
                        Text(
                            text = "Питомец: ${order.pet.breed} ${order.pet.name}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            order.notes,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(text = "Адрес: ${order.address}", modifier = Modifier)
                    }
                }
                Box() {
                    Column {
                        Text(
                            text = "Дата: ${order.date}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(
                            text = "Время: ${order.time}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(
                            text = "Цена: ${order.price}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                }
                Button(
                    modifier = Modifier.padding(12.dp),
                    onClick = { onOrderClick(order) }
                ) {
                    Text("Удалить заказ")
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

fun remakeStatus(status:String):String {
    return when(status) {
        "CREATED" -> "Заказ в ожидании"
        "ACCEPTED" -> "Выгульщик найден"
        "STARTED" -> "Заказ в процессе"
        "ENDED BY WORKER" -> "Заказ ожидает завершения"
        "ENDED" -> "Заказ завершен"
        else -> "Что от не так"
    }
}


