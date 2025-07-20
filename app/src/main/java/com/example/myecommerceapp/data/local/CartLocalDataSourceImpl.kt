package com.example.myecommerceapp.data.local

import com.example.myecommerceapp.data.local.dao.CartItemDao
import com.example.myecommerceapp.data.local.entities.CartItemEntity
import com.example.myecommerceapp.data.local.entities.CartItemWithProductEntity
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class CartLocalDataSourceImpl @Inject constructor(
    private val cartItemDao: CartItemDao
) : CartLocalDataSource {
    override fun getCartItemsWithProducts(): Flow<List<CartItemWithProductEntity>> =
        cartItemDao.getCartItemsWithProducts()

    override suspend fun insertCartItem(cartItem: CartItemEntity) =
        cartItemDao.insertCartItem(cartItem)

    override suspend fun updateCartItem(cartItem: CartItemEntity) =
        cartItemDao.updateCartItem(cartItem)

    override suspend fun getCartItemByProductId(productId: String): CartItemEntity? =
        cartItemDao.getCartItemByProductId(productId)

    override suspend fun deleteCartItemByProductId(productId: String) =
        cartItemDao.deleteCartItemByProductId(productId)

    override suspend fun clearCart() = cartItemDao.deleteAllCartItems()
}