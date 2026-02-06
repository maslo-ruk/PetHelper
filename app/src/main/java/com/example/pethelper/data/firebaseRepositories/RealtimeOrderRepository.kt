package com.example.pethelper.data.firebaseRepositories

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RealtimeOrderRepository {
    private val db = FirebaseDatabase.getInstance("https://pethelper-4efbe-default-rtdb.europe-west1.firebasedatabase.app")
    private val rootRef = db.getReference("activeOrders")

    fun updateLocation(
        orderId:String,
        lat:Double,
        lon:Double
    ) {
        val data = mapOf(
            "lat" to lat,
            "lon" to lon,
            "lastPing" to System.currentTimeMillis()
        )

        rootRef.child(orderId).updateChildren(data)
    }

    fun observeLocation(
        orderId:String,
        onChange: (lat:Double, lon:Double) -> Unit
    ): ValueEventListener {
        val ref = rootRef.child(orderId)

        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val lat = snapshot.child("lat").getValue(Double::class.java)
                val lon = snapshot.child("lon").getValue(Double::class.java)
                if (lat != null && lon != null) {
                    onChange(lat, lon)
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        }

        ref.addValueEventListener(listener)
        return listener
    }

    /** Отписка */
    fun removeListener(orderId: String, listener: ValueEventListener) {
        rootRef.child(orderId)
            .removeEventListener(listener)
    }

    /** Очистка после завершения заказа */
    fun clearOrder(orderId: String) {
        rootRef.child(orderId).removeValue()
    }
}