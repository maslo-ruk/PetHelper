package com.example.pethelper.data.firebaseRepositories

import com.example.pethelper.data.fireBaseEntities.FOrder
import com.example.pethelper.data.fireBaseEntities.FPet
import com.example.pethelper.data.fireBaseEntities.FUser
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObjects
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
        return db.collection("users")
            .document(userId)
            .collection("pets")
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

    suspend fun addPet(userId: String, pet: FPet): DocumentReference {
        return db.collection("users")
            .document(userId)
            .collection("pets")
            .add(pet)
            .await()
    }

    suspend fun updatePet(petId: String, pet: FPet) {
        db.collection("pets")
            .document(petId)
            .set(pet, SetOptions.merge())
    }

    suspend fun addOrder(order: FOrder) {
        db.collection("orders")
            .add(order)
            .await()
        db.collection("users")
            .document(order.userId)
            .collection("orders")
            .add(order)
            .await()
    }

    suspend fun addWorkerToOrder(orderId:String, worker:FUser) {
        db.collection("orders")
            .document(orderId)
            .collection("workers")
            .add(worker)
            .await()
    }
}