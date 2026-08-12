package com.example.civilink.data

data class Report(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val category: String = "",
    val location: String = "",
    val description: String = "",
    val status: String = "Pending",
    val timestamp: Long = 0L
)