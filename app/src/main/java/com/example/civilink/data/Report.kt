package com.example.civilink.data

data class Report(
    val id: String = "",
    val userId: String = "",
    val title: String = "",
    val category: String = "",
    val location: String = "",
    val description: String = "",
    val status: String = "Pending",
    val timestamp: Long = 0L,
    val imageUrl: String? = null,
    // Set whenever the report is created, edited or has its status
    // changed – lets notifications order by "latest activity".
    val lastUpdatedAt: Long = 0L
)
