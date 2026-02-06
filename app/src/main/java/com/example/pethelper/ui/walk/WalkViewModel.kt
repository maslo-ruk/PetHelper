package com.example.pethelper.ui.walk

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.pethelper.data.rtdb.RealtimeOrderRepository
import com.example.pethelper.service.WalkServiceController
import com.example.pethelper.ui.auth.LoginScreen
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class WalkViewModel(
    val rtdbRepository: RealtimeOrderRepository,
    val orderId:String): ViewModel() {
    private val _location = MutableStateFlow<Pair<Double, Double>?>(null)
    val location: StateFlow<Pair<Double, Double>?> = _location.asStateFlow()

    private val _locating = MutableStateFlow<Boolean>(false)
    val locating: StateFlow<Boolean> = _locating.asStateFlow()

    private var listener: ValueEventListener? = null

    fun startObserving(orderId:String) {
        Log.d("WalkViewModel", "startObserving")
        if (listener != null) {
            Log.d("WalkViewModel","Listener is not null")
            return
        }
        Log.d("WalkViewModel", "listenerIsNull")
        listener = rtdbRepository.observeLocation(orderId) { lat, lon ->
            _location.value = Pair(lat, lon)
        }
        _locating.value = true
        Log.d("WalkViewModel", "Location updated: ${location.value?.first}, ${location.value?.second}")
    }

    fun stopObserving(orderId: String) {
        Log.d("WalkViewModel", "Stopped observing")
        listener?.let {
            rtdbRepository.removeListener(orderId, it)
            listener = null
        }
        _locating.value = false
    }

    fun onWalkStarted(
        orderId: String
    ) {
        startObserving(orderId)
    }

    fun onWalkFinished(
        orderId: String
    ) {
        stopObserving(orderId)
    }

    override fun onCleared() {
        listener?.let { rtdbRepository.removeListener("orderId", it) }
    }
}