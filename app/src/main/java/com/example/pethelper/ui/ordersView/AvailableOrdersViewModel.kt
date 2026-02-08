package com.example.pethelper.ui.ordersView

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pethelper.data.fireBaseEntities.FOrder
import com.example.pethelper.data.fireBaseEntities.FPet
import com.example.pethelper.data.firebaseRepositories.ChatRepository
import com.example.pethelper.data.firebaseRepositories.FireStoreRepository
import com.example.pethelper.data.firebaseRepositories.UserSessionManager
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AvailableOrdersViewModel(
    val fbRepository: FireStoreRepository,
    val userManager: UserSessionManager,
    val chatRepository: ChatRepository
): ViewModel() {
    private val _orders = MutableStateFlow<List<FOrder>>(emptyList())
    val orders = _orders.asStateFlow()

    private val _uiState = MutableStateFlow(AvsilableOrdersUiState())
    val uiState = _uiState.asStateFlow()


    private var listener: ListenerRegistration? = null
    private var myListener: ListenerRegistration? = null


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

        if (myListener != null) return

        myListener = fbRepository.observeMyOrders(
            onChange = { orders ->
                _uiState.update { it.copy(ordersOfWorker = orders) }
            },
            onError = { error ->
                Log.e("OrdersViewModel", "Firestore error", error)
            },
            uid = userManager.currentUser.value!!.uid
        )
    }


    fun stopObservingOrders() {
        listener?.remove()
        listener = null
        myListener?.remove()
        myListener = null
    }

    override fun onCleared() {
        stopObservingOrders()
    }

    fun acceptOrder(order: FOrder) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true)  }
            try {
                val otherName = fbRepository.getUser(order.userId)
                Log.d("AceptOrder", "START")
                chatRepository.createChat(
                    order.id,
                    order.userId,
                    userManager.currentUser.value!!.uid,
                    otherName!!,
                    userManager.currentUser.value!!.user
                )
                Log.d("AceptOrder", "PROGRESS")
                fbRepository.addWorkerToOrder(userManager.currentUser.value!!.uid, order)
                Log.d("AceptOrder", "SUCCESS")
                _uiState.update { it.copy(success = true, isLoading = false) }
            } catch (e: Exception) {
                Log.d("AceptOrder", "ERROR")
                _uiState.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }
}