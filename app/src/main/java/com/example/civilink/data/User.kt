package com.example.civilink.data

data class User(
    val fullName: String = "",
    val email: String = "",
    val phone: String = "",
    val role: String = "user",
    val createdAt: Long = 0L
)
