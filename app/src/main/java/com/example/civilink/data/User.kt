package com.example.civilink.data

data class User(
    val fullName: String = "",
    val email: String = "",
    val phone: String = "",
    val location: String = "",
    val role: String = "user",
    val profileImageUrl: String? = null,
    val createdAt: Long = 0L
)
