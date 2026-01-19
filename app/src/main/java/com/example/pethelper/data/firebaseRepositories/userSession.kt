package com.example.pethelper.data.firebaseRepositories

import com.example.pethelper.data.fireBaseEntities.FUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserSessionManager(
    private val authRepository: IAuthRepository,
    private val userRepository: FireStoreRepository
) {

    private val _currentUser = MutableStateFlow<FUser?>(null)
    val currentUser: StateFlow<FUser?> = _currentUser.asStateFlow()

    suspend fun loadCurrentUser() {
        val uid = authRepository.getCurrentUserId() ?: return
        val user = userRepository.getUser(uid)
        _currentUser.value = user
    }

    fun clear() {
        _currentUser.value = null
    }
}