package com.example.pethelper.data.repositories

import com.example.pethelper.data.dao.UserDao
import com.example.pethelper.data.entities.User
import kotlinx.coroutines.flow.Flow

class OfflineUsersRepository(private val userDao: UserDao) : UserRepository {
    override fun getAllUserStream(): Flow<List<User>> = userDao.getAllUsers()

    override fun getUserStream(id: Int): Flow<User?> = userDao.getUser(id)

    override suspend fun insertUser(user: User) = userDao.insert(user)

    override suspend fun deleteUser(user: User) = userDao.delete(user)

    override suspend fun updateUser(user: User) = userDao.update(user)
}