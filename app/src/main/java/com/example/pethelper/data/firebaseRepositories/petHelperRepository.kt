package com.example.pethelper.data.firebaseRepositories

import com.example.pethelper.data.fireBaseEntities.FOrder
import com.example.pethelper.data.fireBaseEntities.FPet
import com.example.pethelper.data.fireBaseEntities.FUser
import com.google.firebase.Firebase
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

    suspend fun addPet(userId: String, pet: FPet) {
        db.collection("users")
            .document(userId)
            .collection("pets")
            .add(pet)
            .await()
    }

    suspend fun addOrder(order: FOrder) {
        db.collection("orders")
            .add(order)
            .await()
    }
}