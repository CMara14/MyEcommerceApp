package com.example.myecommerceapp.data.remote

import retrofit2.http.GET

interface ApiService {
    @GET("foods")
    suspend fun getProducts(): List<ProductDto>
}