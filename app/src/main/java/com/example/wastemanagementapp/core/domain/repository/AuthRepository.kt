package com.example.wastemanagementapp.core.domain.repository

interface AuthRepository {
    suspend fun getCurrentUserId(): String?
}