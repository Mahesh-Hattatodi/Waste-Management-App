package com.example.wastemanagementapp.auth.domain

import com.example.wastemanagementapp.core.domain.UserProfile

interface LoginRepository {
    suspend fun saveGoogleUserProfile(userProfile: UserProfile)
}