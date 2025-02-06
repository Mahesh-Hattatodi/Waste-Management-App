package com.example.wastemanagementapp.auth.domain

import com.example.wastemanagementapp.core.domain.UserProfile

interface SignUpRepository {
    suspend fun saveUserAndSendEmailVerification(email: String, password: String, userProfile: UserProfile)
}