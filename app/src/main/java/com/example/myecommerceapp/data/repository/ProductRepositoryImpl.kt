package com.example.myecommerceapp.data.repository

import com.example.myecommerceapp.data.local.ProductsLocalDataSource
import com.example.myecommerceapp.data.remote.ProductsRemoteDataSource
import com.example.myecommerceapp.domain.model.Product
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepositoryImpl @Inject constructor(
    private val localDataSource: ProductsLocalDataSource,
    private val remoteDataSource: ProductsRemoteDataSource
) : ProductRepository {

    override suspend fun getAllProducts(refreshData: Boolean): List<Product> {
        return if (refreshData) {
            val remoteProducts = remoteDataSource.getProducts()
            localDataSource.clearProducts()
            localDataSource.insertProducts(remoteProducts.map { it.toEntity() })
            remoteProducts.map { it.toDomain() }
        } else {
            val localProductsEntities = localDataSource.getProducts().first()
            val localProducts = localProductsEntities.map { it.toDomain() }

            if (localProducts.isNotEmpty()) {
                localProducts
            } else {
                val remoteProductsDto = remoteDataSource.getProducts()
                localDataSource.insertProducts(remoteProductsDto.map { it.toEntity() })
                val remoteProducts = remoteProductsDto.map { it.toDomain() }
                remoteProducts
            }
        }
    }

    override fun getProductsFlow(): Flow<List<Product>> {
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
}