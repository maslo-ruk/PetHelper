package com.example.pethelper.data.session

import com.example.pethelper.data.firebaseRepositories.AuthRepository
import com.example.pethelper.data.firebaseRepositories.ChatRepository
import com.example.pethelper.data.firebaseRepositories.FireStoreRepository
import com.example.pethelper.data.firebaseRepositories.UserSessionManager
import com.example.pethelper.data.firebaseRepositories.RealtimeOrderRepository
import com.google.firebase.firestore.FirebaseFirestore

object AppSession {
    lateinit var sessionManager: UserSessionManager
    lateinit var authRepository : AuthRepository
    lateinit var userRepository : FireStoreRepository
    lateinit var rtdbRepository: RealtimeOrderRepository
    lateinit var chatRepository: ChatRepository
    fun init(db: FirebaseFirestore) {
        authRepository = AuthRepository()
        userRepository = FireStoreRepository(db)
        sessionManager = UserSessionManager(authRepository, userRepository)
        rtdbRepository = RealtimeOrderRepository()
        chatRepository = ChatRepository()
    }
}