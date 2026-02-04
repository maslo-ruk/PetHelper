package com.example.pethelper.ui.walk

import androidx.lifecycle.ViewModel
import com.example.pethelper.data.rtdb.RealtimeOrderRepository
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class WalkViewModel(val rtdbRepository: RealtimeOrderRepository): ViewModel() {
    private val _location = MutableStateFlow<Pair<Double, Double>?>(null)
    val location: StateFlow<Pair<Double, Double>?> = _location.asStateFlow()

    private var listener: ValueEventListener? = null

    fun startObserving(orderId:String) {
        if (listener != null) return

        listener = rtdbRepository.observeLocation(orderId) { lat, lng ->
            _location.value = lat to lng
        }
    }

    override fun onCleared() {
        listener?.let { rtdbRepository.removeListener("orderId", it) }
    }
}