package com.example.pethelper.ui.ordersView

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.pethelper.data.fireBaseEntities.FOrder
import com.example.pethelper.data.firebaseRepositories.FireStoreRepository
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class AvailableOrdersViewModel(val fbRepository: FireStoreRepository): ViewModel() {
    private val _orders = MutableStateFlow<List<FOrder>>(emptyList())
    val orders = _orders.asStateFlow()

    private var listener: ListenerRegistration? = null

    fun startObservingOrders() {
        if (listener != null) return

        listener = fbRepository.observeOrders(
            onChange = { orders ->
                _orders.value = orders
            },
            onError = { error ->
                Log.e("OrdersViewModel", "Firestore error", error)
            }
        )
    }

    fun stopObservingOrders() {
        listener?.remove()
        listener = null
    }

    override fun onCleared() {
        stopObservingOrders()
    }

    fun acceptOrder(orderId:String) {

    }
}