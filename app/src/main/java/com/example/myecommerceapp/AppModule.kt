package com.example.myecommerceapp

import com.example.myecommerceapp.data.local.CartLocalDataSource
import com.example.myecommerceapp.data.local.CartLocalDataSourceImpl
import com.example.myecommerceapp.data.local.OrderLocalDataSource
import com.example.myecommerceapp.data.local.OrderLocalDataSourceImpl
import com.example.myecommerceapp.data.local.ProductsLocalDataSource
import com.example.myecommerceapp.data.local.ProductsLocalDataSourceImpl
import com.example.myecommerceapp.data.local.dao.CartItemDao
import com.example.myecommerceapp.data.local.dao.OrderHistoryDao
import com.example.myecommerceapp.data.local.dao.OrderItemDao
import com.example.myecommerceapp.data.local.dao.ProductDao
import com.example.myecommerceapp.data.repository.AuthRepository
import com.example.myecommerceapp.data.repository.AuthRepositoryImpl
import com.example.myecommerceapp.data.repository.CartRepository
import com.example.myecommerceapp.data.repository.CartRepositoryImpl
import com.example.myecommerceapp.data.repository.OrderRepository
import com.example.myecommerceapp.data.repository.OrderRepositoryImpl
import com.example.myecommerceapp.data.repository.ProductRepository
import com.example.myecommerceapp.data.repository.ProductRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import javax.inject.Singleton
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    // Local Data Sources
    @Provides
    @Singleton
    fun provideProductsLocalDataSource(productDao: ProductDao): ProductsLocalDataSource {
        return ProductsLocalDataSourceImpl(productDao)
    }

    @Provides
    @Singleton
    fun provideCartLocalDataSource(cartItemDao: CartItemDao): CartLocalDataSource {
        return CartLocalDataSourceImpl(cartItemDao)
    }

    @Provides
    @Singleton
    fun provideOrderLocalDataSource(
        orderHistoryDao: OrderHistoryDao,
        orderItemDao: OrderItemDao
    ): OrderLocalDataSource {
        return OrderLocalDataSourceImpl(orderHistoryDao, orderItemDao)
    }

    // Repositories
    @Provides
    @Singleton
    fun provideAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository {
        return authRepositoryImpl
    }

    @Provides
    @Singleton
    fun provideProductRepository(
        localDataSource: ProductsLocalDataSource
    ): ProductRepository {
        return ProductRepositoryImpl(localDataSource)
    }

    @Provides
    @Singleton
    fun provideCartRepository(
        cartLocalDataSource: CartLocalDataSource,
        productRepository: ProductRepository
    ): CartRepository {
        return CartRepositoryImpl(cartLocalDataSource, productRepository)
    }

    @Provides
    @Singleton
    fun provideOrderRepository(orderLocalDataSource: OrderLocalDataSource): OrderRepository {
        return OrderRepositoryImpl(orderLocalDataSource)
    }
}