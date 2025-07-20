package com.example.myecommerceapp.data.repository

import com.example.myecommerceapp.data.local.ProductsLocalDataSource
import com.example.myecommerceapp.data.local.entities.ProductEntity
import com.example.myecommerceapp.domain.model.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepositoryImpl @Inject constructor(
    private val localDataSource: ProductsLocalDataSource
) : ProductRepository {

    init {
        CoroutineScope(Dispatchers.IO).launch {
            localDataSource.initializeProductsIfEmpty()
        }
    }

    override fun getAllProducts(): Flow<List<Product>> {
        return localDataSource.getProducts().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getProductById(productId: String): Product? {
        val productEntity = localDataSource.getProductById(productId)
        if (productEntity != null) {
            return productEntity.toDomain()
        } else {
            return null
        }
    }

    private fun ProductEntity.toDomain(): Product {
        return Product(
            id = this.id,
            name = this.name,
            description = this.description,
            imageUrl = this.imageUrl,
            price = this.price,
            hasDrink = this.hasDrink,
            category = this.category
        )
    }
}
