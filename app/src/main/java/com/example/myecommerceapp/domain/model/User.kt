package com.example.myecommerceapp.domain.model

data class User(
    val email: String,
    val password: String,
    val name: String = "",
    val lastName: String = "",
    val nationality: String = "",
    val profileImage: String? = null
)