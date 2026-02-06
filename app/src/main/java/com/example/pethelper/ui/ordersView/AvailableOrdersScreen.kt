package com.example.pethelper.ui.ordersView

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.pethelper.data.fireBaseEntities.FOrder


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvailableOrders(viewModel: AvailableOrdersViewModel = viewModel(factory = AppViewModelProvider.Factory),
                    onOrderClick:(order: FOrder)->Unit = viewModel::acceptOrder, onBack: () -> Unit) {
    val orders by viewModel.orders.collectAsState()

    Scaffold(topBar = { TopAppBar(title = {Text("Доступные заказы")},
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
                OrderCard(order, onOrderClick) }
        }
    }
    }


@Composable
fun OrderCard(order: FOrder, onOrderClick:(order: FOrder)->Unit) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(12.dp),
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
            Column {
                Box() {
                    Column {
                        Text(text = "Заказчик: ${order.user.name} ${order.user.surname}", style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold, modifier = Modifier)
                        Text(text = "Питомец: ${order.pet.breed} ${order.pet.name}", style = MaterialTheme.typography.bodyMedium)
                        Text(order.notes, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant,)
                        Text(text = "Адрес: ${order.address}", modifier = Modifier)
                    }
                }
                Box() {
                    Column {
                        Text(text = "Дата: ${order.date}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        Text(text = "Цена: ${order.price}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            )
                    }
                }
                Button(modifier=Modifier.padding(12.dp),
                    onClick = {onOrderClick(order)}
                ) {
                    Text("Отозваться")
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}