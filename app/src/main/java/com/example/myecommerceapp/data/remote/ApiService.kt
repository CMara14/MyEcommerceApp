package com.example.myecommerceapp.data.remote

import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiService {
    @GET("foods")
    suspend fun getProducts(): List<ProductDto>

//    @GET("foods/{id}")
//
//    @PUT("foods/{id}")
//
//    @DELETE("foods/{id}")
//
//    @POST("orders")

//    Request body
//    {
//        "orderId": "string",
//        "productIds": [
//        {
//            "name": "string",
//            "description": "string",
//            "imageUrl": "string",
//            "price": 0,
//            "hasDrink": true,
//            "quantity": 0
//        }
//        ],
//        "total": 0,
//        "timestamp": 0
//    }

//    @GET("orders")
//    suspend fun getOrders():
//
//    @POST("users/register")
//    suspend fun createUser():

//    Request body
//    {
//        "email": "string",
//        "fullName": "string",
//        "encryptedPassword": "string"
//    }

//    @POST("users/login")
//    suspend fun loginUser():
//    Request body
//    {
//        "email": "string",
//        "encryptedPassword": "string"
//    }

//            responses
//          200
//  Login exitoso
//
//    401
//    Credenciales inválidas
//
//
//    404
//    Usuario no encontrado
//
//    500
//    Error en el servidor

//    @GET("users/{email}")
//    suspend fun getUserByEmail():
            //email: String


}