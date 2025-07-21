package com.example.myecommerceapp.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @GET("foods")
    suspend fun getProducts(): List<ProductDto>

    @POST("users/login")
    suspend fun loginUser(@Body request: LoginRequestDto): Unit
}