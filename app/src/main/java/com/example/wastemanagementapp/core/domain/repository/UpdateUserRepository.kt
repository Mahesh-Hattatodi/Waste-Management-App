package com.example.wastemanagementapp.core.domain.repository

import com.example.wastemanagementapp.core.domain.FirebaseFireStoreError
import com.example.wastemanagementapp.core.util.Result

interface UpdateUserRepository {
    suspend fun updateUserPoints(
        updateField: String,
        incrementBy: Int
    ) : Result<Unit, FirebaseFireStoreError>
}