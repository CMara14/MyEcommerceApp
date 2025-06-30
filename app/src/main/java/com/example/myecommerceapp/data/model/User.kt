package com.example.myecommerceapp.data.model

data class User(
    val email: String,
    val password: String,
    val name: String = "",
    val lastName: String = "",
    val nationality: String = "",
    val profileImage: String? = null
)