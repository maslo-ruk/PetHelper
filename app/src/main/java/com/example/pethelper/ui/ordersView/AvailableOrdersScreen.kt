package com.example.pethelper.ui.ordersView

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pethelper.Constants
import com.example.pethelper.data.fireBaseEntities.FOrder
import com.google.firebase.firestore.auth.User


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AvailableOrders(viewModel: AvailableOrdersViewModel = viewModel(factory = AppViewModelProvider.Factory),
                    onOrderClick:(order: FOrder)->Unit = viewModel::acceptOrder, onBack: () -> Unit) {
    val orders by viewModel.orders.collectAsState()
    val uiState by viewModel.uiState.collectAsState()


    Scaffold(topBar = {
        CenterAlignedTopAppBar(
            title = {Text("Доступные заказы",
                fontSize = 25.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )},
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBackIos,
                    contentDescription = "Назад",
                    tint = Color.White
                )
            }
        },
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color(0xFF690005),
                titleContentColor = Color.White,
                navigationIconContentColor = Color.White)
        )
            }) { innerPadding ->
        LaunchedEffect(Unit) {
            viewModel.startObservingOrders()
        }
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(brush = Constants.GRADIENT_BRUSH),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(orders) { order ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                Log.d("STUPIDO", order.id)
                Log.d("STUPIDO", uiState.ordersOfWorker.size.toString())
                for (i in uiState.ordersOfWorker) {
                    Log.d("STUPIDO000", i.id)
                    if (i.id == order.id) return@items
                }
                OrderCard(order, onOrderClick, uiState.ordersOfWorker) }
            }
        }
    }
}


@Composable
fun OrderCard(order: FOrder, onOrderClick:(order: FOrder)->Unit, myOrders:List<FOrder>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF690005)
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Box() {
                    Column {
                        Text(
                            text = "Заказчик: ${order.user.name} ${order.user.surname}",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier,
                            color = Color.White,
                        )
                        Text(
                            text = "Питомец: ${order.pet.breed} ${order.pet.name}",
                            //style = MaterialTheme.typography.bodyMedium,
                            color = Color.White,
                        )
                        Text(
                            order.notes,
                            style = MaterialTheme.typography.bodyMedium,
                            //color = MaterialTheme.colorScheme.onSurfaceVariant,
                            color = Color.White,
                        )
                        Text(text = "Адрес: ${order.address}", modifier = Modifier,
                            color = Color.White,)
                    }
                }
                Box() {
                    Column {
                        Text(
                            text = "Дата: ${order.date}",
                            //style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(
                            text = "Цена: ${order.price}",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF690005),
                        )
                    }
                }
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(38.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color(0xFF690005)),
                    onClick = { onOrderClick(order) }
                ) {
                    Text("Отозваться",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF690005),)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}