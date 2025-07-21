package com.example.myecommerceapp.data.remote

import javax.inject.Inject

class ProductsRemoteDataSourceImpl @Inject constructor(
    private val apiService: ApiService
) : ProductsRemoteDataSource {
    override suspend fun getProducts(): List<ProductDto> = apiService.getProducts()
}