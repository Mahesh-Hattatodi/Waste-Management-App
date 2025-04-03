package com.example.wastemanagementapp.feedback.presentation

data class FeedbackScreenState(
    val emoji: String = "",
    val selectedTopic: String = "Select topic",
    val feedback: String = "",
    val isExpanded: Boolean = false,
    val isSubmitting: Boolean = false
)
