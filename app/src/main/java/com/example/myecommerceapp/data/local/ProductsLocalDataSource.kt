package com.example.myecommerceapp.data.local

import com.example.myecommerceapp.data.local.entities.ProductEntity
import kotlinx.coroutines.flow.Flow

interface ProductsLocalDataSource {
    fun getProducts(): Flow<List<ProductEntity>>
    suspend fun insertProducts(products: List<ProductEntity>)
    suspend fun clearProducts()
    suspend fun initializeProductsIfEmpty()
    suspend fun getProductCount(): Int
    suspend fun getProductById(productId: String): ProductEntity?
}