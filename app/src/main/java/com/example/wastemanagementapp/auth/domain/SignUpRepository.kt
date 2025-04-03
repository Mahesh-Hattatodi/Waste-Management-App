package com.example.wastemanagementapp.auth.domain

interface SignUpRepository {
    suspend fun saveUserAndSendEmailVerification(
        email: String,
        password: String,
        displayName: String,
        ward: String
    )
}