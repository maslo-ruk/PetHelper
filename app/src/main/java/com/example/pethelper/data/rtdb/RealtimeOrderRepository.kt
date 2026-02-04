package com.example.pethelper.data.rtdb

import com.google.firebase.database.FirebaseDatabase

class RealtimeOrderRepository {
    private val db = FirebaseDatabase.getInstance()
    private val rootRef = db.getReference("activeOrders")

    fun updateLocation(
        orderId:String,
        lat:Double,
        lon:Double
    ) {
        val data = mapOf(
            "location/lat" to lat,
            "location/lng" to lon,
            "lastPing" to System.currentTimeMillis()
        )

        rootRef.child(orderId).updateChildren(data)
    }
}