package com.example.myecommerceapp.data.remote

import com.google.gson.annotations.SerializedName

data class LoginRequestDto(
    val email: String,
    @SerializedName("encryptedPassword")
    val password: String
)