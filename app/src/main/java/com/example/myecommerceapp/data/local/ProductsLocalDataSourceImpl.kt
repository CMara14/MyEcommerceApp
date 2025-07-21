package com.example.myecommerceapp.data.local

import com.example.myecommerceapp.data.local.dao.ProductDao
import com.example.myecommerceapp.data.local.entities.ProductEntity
import com.example.myecommerceapp.domain.model.Product
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductsLocalDataSourceImpl @Inject constructor(
    private val productDao: ProductDao
) : ProductsLocalDataSource {
    override fun getProducts(): Flow<List<ProductEntity>> {
        return productDao.getAllProducts()
    }

    override suspend fun insertProducts(products: List<ProductEntity>) {
        productDao.insertProducts(products)
    }

    override suspend fun clearProducts() {
        productDao.deleteAllProducts()
    }

    override suspend fun getProductById(productId: String): ProductEntity? {
        try {
            val entity = productDao.getProductById(productId)
            return entity
        } catch (e: Exception) {
            return null
        }
    }
}