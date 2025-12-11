package com.example.pethelper.data.repositories

import com.example.pethelper.data.dao.OrdersDao
import com.example.pethelper.data.dao.UserDao
import com.example.pethelper.data.entities.Orderr
import kotlinx.coroutines.flow.Flow

class OfflineOrderRepository(private val orderDao: OrdersDao) : OrderRepository {
    override fun getAllOrderStream(): Flow<List<Orderr>> = orderDao.getAllOrders();

    override fun getOrderStream(id: Int): Flow<Orderr?> = orderDao.getOrder(id)

    override suspend fun insertOrder(user: Orderr) = orderDao.insert(user)

    override suspend fun deleteOrder(user: Orderr) = orderDao.delete(user)

    override suspend fun updateOrder(user: Orderr) = orderDao.update(user)
}