package com.example.wastemanagementapp.feedback.domain.models

data class UserFeedback(
    val uuid: String = "",
    val rating: Int,
    val topic: String,
    val feedback: String
)
