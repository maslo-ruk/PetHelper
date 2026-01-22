package com.example.pethelper.data.firebaseRepositories

import com.example.pethelper.data.fireBaseEntities.FPet
import com.example.pethelper.data.fireBaseEntities.FUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserSessionManager(
    private val authRepository: IAuthRepository,
    private val userRepository: FireStoreRepository
) {

    private val _currentUser = MutableStateFlow<UserData?>(null)
    val currentUser: StateFlow<UserData?> = _currentUser.asStateFlow()

    private val _pets = MutableStateFlow<List<FPet>>(emptyList())
    val pets: StateFlow<List<FPet>> = _pets.asStateFlow()

    suspend fun loadCurrentUser() {
        val uid = authRepository.getCurrentUserId() ?: return
        val user = userRepository.getUser(uid) ?: return
        val pets = userRepository.getPetsOfUser(uid)
        _pets.value = pets
        _currentUser.value = UserData(user, uid)
    }

    fun clear() {
        _currentUser.value = null
    }
}

data class UserData(
    val user:FUser = FUser(),
    val uid:String = ""
)