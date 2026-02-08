package com.example.pethelper.ui.ordersView

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pethelper.data.fireBaseEntities.FOrder
import com.example.pethelper.data.firebaseRepositories.ChatRepository
import com.example.pethelper.data.firebaseRepositories.FireStoreRepository
import com.example.pethelper.data.firebaseRepositories.UserSessionManager
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MyOrdersViewModel(
    val fbRepository: FireStoreRepository,
    val userManager: UserSessionManager,
    val chatRepository: ChatRepository
): ViewModel() {
    private val _orders = MutableStateFlow<List<FOrder>>(emptyList())
    val orders = _orders.asStateFlow()

    val _uiState = MutableStateFlow(MyOrdersUiState())
    val uiState : StateFlow<MyOrdersUiState> = _uiState

    private var listener: ListenerRegistration? = null

    fun startObservingOrders() {
        if (listener != null) return

        listener = fbRepository.observeMyOrders(
            onChange = { orders ->
                _orders.value = orders
            },
            onError = { error ->
                Log.e("OrdersViewModel", "Firestore error", error)
            },
            uid = userManager.currentUser.value!!.uid
        )
    }

    fun deleteOrder(order: FOrder) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            chatRepository.finishChatsById(order.id)
            fbRepository.deleteOrder(order)
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun stopObservingOrders() {
        listener?.remove()
        listener = null
    }

    override fun onCleared() {
        stopObservingOrders()
    }
}

data class MyOrdersUiState(
    val isLoading:Boolean = false,
    val error:String = ""
)