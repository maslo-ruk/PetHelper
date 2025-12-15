package com.example.pethelper.ui.orders

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pethelper.ui.orders.OrderUiState
import com.example.pethelper.data.entities.Orderr
import com.example.pethelper.data.repositories.OrderRepository
import com.example.pethelper.data.repositories.PetsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class OrderDialogViewModel(
    private val ordersRepository: OrderRepository,
    private val petsRepository: PetsRepository

) : ViewModel() {

    var _uiState = MutableStateFlow(OrderUiState())
    var uiState: StateFlow<OrderUiState> = _uiState.asStateFlow()

    fun updateUiState(details: OrderDetails) {
        _uiState.update { it.copy(details = details) }
    }

    fun submitOrder() {
        val state = _uiState.value
        // 1️⃣ Валидация
        if (state.details.petId == -1) {
            _uiState.update {
                it.copy(error = "Заполните все поля")
            }
            return
        }

        // 2️⃣ Создание Entity
        val order = state.details.toOrderr()

        // 3️⃣ Запись в БД
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            try {
                ordersRepository.insertOrder(order)

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

fun OrderDetails.toOrderr(): Orderr = Orderr(
    petId = petId,
    date = date,
    time = time,
    address = address,
    price = price,
    notes = notes
)