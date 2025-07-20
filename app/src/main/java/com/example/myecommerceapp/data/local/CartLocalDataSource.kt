package com.example.myecommerceapp.data.local

import com.example.myecommerceapp.data.local.entities.CartItemEntity
import com.example.myecommerceapp.data.local.entities.CartItemWithProductEntity
import kotlinx.coroutines.flow.Flow

interface CartLocalDataSource {
    fun getCartItemsWithProducts(): Flow<List<CartItemWithProductEntity>>
    suspend fun insertCartItem(cartItem: CartItemEntity)
    suspend fun updateCartItem(cartItem: CartItemEntity)
    suspend fun getCartItemByProductId(productId: String): CartItemEntity?
    suspend fun deleteCartItemByProductId(productId: String)
    suspend fun clearCart()
}