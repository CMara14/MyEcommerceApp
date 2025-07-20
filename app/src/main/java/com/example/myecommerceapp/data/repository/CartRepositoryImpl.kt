package com.example.myecommerceapp.data.repository

import com.example.myecommerceapp.data.local.CartLocalDataSource
import com.example.myecommerceapp.data.local.entities.CartItemEntity
import com.example.myecommerceapp.domain.model.CartItem
import com.example.myecommerceapp.domain.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CartRepositoryImpl @Inject constructor(
    private val cartLocalDataSource: CartLocalDataSource,
    private val productRepository: ProductRepository
) : CartRepository {

    override fun getCartItems(): Flow<List<CartItem>> {
        return cartLocalDataSource.getCartItemsWithProducts().map { list ->
            list.map { cartItemWithProduct ->
                val product = cartItemWithProduct.product.toDomain()
                CartItem(
                    id = cartItemWithProduct.cartItem.id,
                    productId = product.id,
                    name = product.name,
                    imageUrl = product.imageUrl,
                    unitPrice = product.price,
                    quantity = cartItemWithProduct.cartItem.quantity
                )
            }
        }
    }

    override suspend fun addProductToCart(product: Product, quantity: Int) {
        val existingCartItem = cartLocalDataSource.getCartItemByProductId(product.id)
        if (existingCartItem != null) {
            val newQuantity = existingCartItem.quantity + quantity
            cartLocalDataSource.updateCartItem(existingCartItem.copy(quantity = newQuantity))
        } else {
            val cartItemEntity = CartItemEntity(productId = product.id, quantity = quantity)
            cartLocalDataSource.insertCartItem(cartItemEntity)
        }
    }


    override suspend fun updateCartItemQuantity(productId: String, quantity: Int) {
        val existingCartItem = cartLocalDataSource.getCartItemByProductId(productId)
        if (existingCartItem != null) {
            if (quantity <= 0) {
                cartLocalDataSource.deleteCartItemByProductId(productId)
            } else {
                cartLocalDataSource.updateCartItem(existingCartItem.copy(quantity = quantity))
            }
        } else {
            if (quantity > 0) {
                val productToAdd =
                    productRepository.getProductById(productId)
                if (productToAdd != null) {
                    val cartItemEntity =
                        CartItemEntity(productId = productId, quantity = quantity)
                    cartLocalDataSource.insertCartItem(cartItemEntity)
                }
            }
        }
    }

    override suspend fun removeProductFromCart(productId: String) {
        cartLocalDataSource.deleteCartItemByProductId(productId)
    }

    override suspend fun clearCart() {
        cartLocalDataSource.clearCart()
    }
}