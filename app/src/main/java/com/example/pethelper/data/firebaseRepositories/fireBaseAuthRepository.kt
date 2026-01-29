package com.example.pethelper.data.firebaseRepositories

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

interface IAuthRepository {
    suspend fun register(email: String, password: String): Result<String>
    suspend fun login(email: String, password: String): Result<String>
    fun logout()
    fun getCurrentUserId(): String?
}

class AuthRepository(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
) : IAuthRepository {
    override suspend fun register(email: String, password: String): Result<String> {
        return try {
            val result = auth
                .createUserWithEmailAndPassword(email, password)
                .await()

            Result.success(result.user!!.uid)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun login(email: String, password: String): Result<String> {
        return try {
            val result = auth
                .signInWithEmailAndPassword(email, password)
                .await()

            Result.success(result.user!!.uid)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    override fun logout() {
        auth.signOut()
    }

    override fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    fun isLogged():Boolean {
        return auth.currentUser != null
    }

}