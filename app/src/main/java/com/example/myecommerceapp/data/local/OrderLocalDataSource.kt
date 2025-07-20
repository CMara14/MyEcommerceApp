package com.example.myecommerceapp.data.local

import com.example.myecommerceapp.data.local.entities.OrderEntity
import com.example.myecommerceapp.data.local.entities.OrderItemEntity
import com.example.myecommerceapp.data.local.entities.OrderWithItemsAndProducts
import kotlinx.coroutines.flow.Flow

interface OrderLocalDataSource {
    fun getAllOrders(): Flow<List<OrderWithItemsAndProducts>>
    suspend fun getOrderById(orderId: Int): OrderEntity?
    suspend fun insertOrderEntity(order: OrderEntity): Long
    suspend fun insertOrderItems(orderItems: List<OrderItemEntity>)
}