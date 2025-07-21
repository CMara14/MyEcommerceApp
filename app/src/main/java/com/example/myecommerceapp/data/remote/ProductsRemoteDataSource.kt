package com.example.myecommerceapp.data.remote

interface ProductsRemoteDataSource {
    suspend fun getProducts(): List<ProductDto>
}