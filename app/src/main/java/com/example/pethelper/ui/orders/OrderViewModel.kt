package com.example.pethelper.ui.orders

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pethelper.data.fireBaseEntities.FOrder
import com.example.pethelper.data.fireBaseEntities.FPet
import com.example.pethelper.data.firebaseRepositories.FireStoreRepository
import com.example.pethelper.data.firebaseRepositories.UserSessionManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OrderDialogViewModel(
    val fbRepository: FireStoreRepository,
    val userManager: UserSessionManager
) : ViewModel() {
    var _uiState = MutableStateFlow(OrderUiState())
    var uiState: StateFlow<OrderUiState> = _uiState.asStateFlow()


    fun updateUiState(details: OrderDetails) {
        _uiState.update { it.copy(details = details) }
    }

    fun submitOrder() {
        val state = _uiState.value
        var order = state.details.toFOrder().copy(userId = userManager.currentUser.value!!.uid, user = userManager.currentUser.value!!.user)
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                val ord = fbRepository.addOrder(order, userManager.currentUser.value!!.uid)
                order = order.copy(id=ord.id)
                fbRepository.updateOrder(ord.id, order)
                _uiState.update {
                    it.copy(isLoading = false, success = true)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Ошибка сохранения"
                    )
                }
            }
        }
    }
}

fun OrderDetails.toFOrder(): FOrder = FOrder(
    id = id,
    pet = pet,
    userId = userId,
    date = date,
    time = time,
    address = address,
    price = price,
    notes = notes,
)