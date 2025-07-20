package com.example.myecommerceapp.data.repository

import com.example.myecommerceapp.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    fun getAllProducts(): Flow<List<Product>>
    suspend fun getProductById(productId: String): Product?
}
