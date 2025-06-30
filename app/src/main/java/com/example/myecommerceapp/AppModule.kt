package com.example.myecommerceapp

import com.example.myecommerceapp.data.repository.AuthRepository
import com.example.myecommerceapp.data.repository.AuthRepositoryImpl
import com.example.myecommerceapp.data.repository.ProductRepository
import com.example.myecommerceapp.data.repository.ProductsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    @Singleton
    abstract fun bindProductRepository(
        productsRepositoryImpl: ProductsRepositoryImpl
    ): ProductRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository
}
