package com.example.myecommerceapp.data.repository

import com.example.myecommerceapp.domain.model.CartItem
import com.example.myecommerceapp.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface CartRepository {
    fun getCartItems(): Flow<List<CartItem>>
    suspend fun addProductToCart(product: Product, quantity: Int)
    suspend fun updateCartItemQuantity(productId: String, quantity: Int)
    suspend fun removeProductFromCart(productId: String)
    suspend fun clearCart()
}