package com.example.myecommerceapp.data.repository

import com.example.myecommerceapp.domain.model.CartItem
import com.example.myecommerceapp.domain.model.Order
import kotlinx.coroutines.flow.Flow

interface OrderRepository {
    suspend fun createOrder(cartItems: List<CartItem>, totalAmount: Double, totalItems: Int): Long
    fun getAllOrders(): Flow<List<Order>>
}