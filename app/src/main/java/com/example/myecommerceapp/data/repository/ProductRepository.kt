package com.example.myecommerceapp.data.repository

import com.example.myecommerceapp.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun getAllProducts(refreshData: Boolean): List<Product>

    fun getProductsFlow(): Flow<List<Product>>

    suspend fun getProductById(productId: String): Product?
}
