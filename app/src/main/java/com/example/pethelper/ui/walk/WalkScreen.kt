package com.example.pethelper.ui.walk

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun WalkScreen(
    orderId: String,
    viewModel: WalkViewModel
) {
    val location by viewModel.location.collectAsState()

    LaunchedEffect(orderId) {
        viewModel.startObserving(orderId)
    }

    if (location == null) {
        Text("Ожидание геолокации…")
    } else {
        val (lat, lng) = location!!
        Text("Широта: $lat\nДолгота: $lng")
    }
}