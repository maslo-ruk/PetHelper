package com.example.pethelper.data.firebaseRepositories

import android.util.Log
import com.example.pethelper.data.fireBaseEntities.FOrder
import com.example.pethelper.data.fireBaseEntities.FPet
import com.example.pethelper.data.fireBaseEntities.FUser
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FireStoreRepository(
    private val db: FirebaseFirestore = Firebase.firestore
) {

    suspend fun addUser(userId: String, user: FUser) {
        db.collection("users").document(userId).set(user).await()
    }

    suspend fun getUser(userId: String): FUser? {
        return db.collection("users")
            .document(userId)
            .get()
            .await()
            .toObject(FUser::class.java)
    }

    suspend fun updateUser(userId: String, user: FUser) {
        db.collection("users")
            .document(userId)
            .set(user, SetOptions.merge())
            .await()
    }

    suspend fun getPetsOfUser(userId:String): List<FPet> {
        return db.collection("pets")
            .whereEqualTo("ownerId", userId)
            .get()
            .await()
            .toObjects(FPet::class.java)
    }

    suspend fun addPetPhoto(photoId:String, petId:String) {
        db.collection("pets")
            .document(petId)
            .update("photoId", photoId)
            .await()
    }

    suspend fun getPetPhoto(petId:String):String {
        val ref = db.collection("pets")
            .document(petId)
            .get()
            .await()
        val photoId = ref.getString("photoId")
        return photoId ?: ""
    }

    suspend fun addPet(pet: FPet): DocumentReference {
        return db.collection("pets")
            .add(pet)
            .await()
    }

    suspend fun getPet(petId: String): FPet? {
        return db.collection("pets")
            .document(petId)
            .get().await().toObject(FPet::class.java)
    }
    suspend fun updatePet(petId: String, pet: FPet, userId:String) {
        db.collection("pets")
            .document(petId)
            .set(pet, SetOptions.merge())
    }

    suspend fun addOrder(order: FOrder, userId: String): DocumentReference {
        return db.collection("orders")
            .add(order)
            .await()

    }

    suspend fun deleteOrder(order: FOrder) {
        db.collection("orders").document(order.id).delete().await()
    }

    suspend fun updateOrder(orderId: String, order: FOrder) {
        Log.d("FS", "updateOrder")
        db.collection("orders")
            .document(orderId)
            .set(order, SetOptions.merge())
            .await()
        val ord = db.collection("orders").document(orderId).get().await()
        Log.d("FS", "updateOrder ${ord.get("STATUS")}")
    }

    suspend fun addWorkerToOrder(userId: String, order: FOrder) {

        try {

            db.collection("users")
                .document(userId)
                .collection("orders")
                .document(order.id)
                .set(order)
                .await()

            Log.d("FS", "Order added: ${userId}")

        } catch (e: Exception) {

            Log.e("FS", "Failed to add order", e)
        }
    }

    fun observeOrder(orderId: String): Flow<FOrder> = callbackFlow {

        val listener = db
            .collection("orders")
            .document(orderId)
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    val order = snapshot.toObject(FOrder::class.java)
                    if (order != null) {
                        trySend(order)
                    }
                }
            }

        awaitClose {
            listener.remove()
        }
    }

    fun observeOrders(
        onChange: (List<FOrder>) -> Unit,
        onError: (Throwable) -> Unit
    ): ListenerRegistration {
        val ordersRef = db.collection("orders").whereEqualTo("status", "CREATED")
        Log.d("FS", "Orders ref = ${ordersRef.get()}")

        return ordersRef
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    onError(error)
                    return@addSnapshotListener
                }

                if (snapshot == null) return@addSnapshotListener

                val orders = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(FOrder::class.java)?.copy(id = doc.id)
                }

                Log.d("FS", "Docs count = ${snapshot.size()}") // ← ВАЖНО

                onChange(orders)
            }
    }

    fun observeAnsweredOrders(onChange: (List<FOrder>) -> Unit,
                              onError: (Throwable) -> Unit,
                              uid: String,
    ): ListenerRegistration {
        val ordersRef = db.collection("users").document(uid).collection("orders")
        Log.d("FS", "Orders ref = ${ordersRef.get()}")

        return ordersRef
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    onError(error)
                    return@addSnapshotListener
                }

                if (snapshot == null) return@addSnapshotListener

                val orders = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(FOrder::class.java)?.copy(id = doc.id)
                }

                Log.d("FS", "Docs count = ${snapshot.size()}") // ← ВАЖНО

                onChange(orders)
            }
    }

    fun observeMyOrders(
        onChange: (List<FOrder>) -> Unit,
        onError: (Throwable) -> Unit,
        uid: String,
    ): ListenerRegistration {
        val ordersRef = db.collection("orders").whereEqualTo("userId", uid)
        Log.d("FS", "Orders ref = ${ordersRef.get()}")

        return ordersRef
            .addSnapshotListener { snapshot, error ->

                if (error != null) {
                    onError(error)
                    return@addSnapshotListener
                }

                if (snapshot == null) return@addSnapshotListener

                val orders = snapshot.documents.mapNotNull { doc ->
                    doc.toObject(FOrder::class.java)?.copy(id = doc.id)
                }

                Log.d("FS", "Docs count = ${snapshot.size()}") // ← ВАЖНО

                onChange(orders)
            }
    }
}