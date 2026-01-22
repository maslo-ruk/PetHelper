package com.example.pethelper.ui.orders

import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pethelper.data.fireBaseEntities.FOrder
import com.example.pethelper.data.fireBaseEntities.FPet
import com.example.pethelper.data.firebaseRepositories.FireStoreRepository
import com.example.pethelper.data.firebaseRepositories.UserSessionManager
import com.example.pethelper.data.repositories.OrderRepository
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
        val order = state.details.toFOrder()

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                fbRepository.addOrder(order)

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