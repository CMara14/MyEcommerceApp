package com.example.myecommerceapp.data.local

import com.example.myecommerceapp.data.local.dao.OrderHistoryDao
import com.example.myecommerceapp.data.local.dao.OrderItemDao
import com.example.myecommerceapp.data.local.entities.OrderEntity
import com.example.myecommerceapp.data.local.entities.OrderItemEntity
import com.example.myecommerceapp.data.local.entities.OrderWithItemsAndProducts
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderLocalDataSourceImpl @Inject constructor(
    private val orderHistoryDao: OrderHistoryDao,
    private val orderItemDao: OrderItemDao
) : OrderLocalDataSource {
    override suspend fun insertOrderEntity(order: OrderEntity): Long {
        return orderHistoryDao.insertOrder(order)
    }

    override suspend fun insertOrderItems(orderItems: List<OrderItemEntity>) {
        orderItemDao.insertOrderItems(orderItems)
    }

    override fun getAllOrders(): Flow<List<OrderWithItemsAndProducts>> =
        orderHistoryDao.getAllOrdersWithItemsAndProducts()

    override suspend fun getOrderById(orderId: Int): OrderEntity? =
        orderHistoryDao.getOrderById(orderId)
}