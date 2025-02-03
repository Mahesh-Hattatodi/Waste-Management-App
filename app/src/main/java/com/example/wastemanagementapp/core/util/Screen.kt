package com.example.wastemanagementapp.core.util

import kotlinx.serialization.Serializable

sealed class Screen {

    @Serializable
    data object LoginScreen : Screen()

    @Serializable
    data object SignUpScreen : Screen()

    @Serializable
    data object ComplaintScreen : Screen()

    @Serializable
    data object FeedbackScreen : Screen()
}