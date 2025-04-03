package com.example.wastemanagementapp.home.domain.repository

import com.example.wastemanagementapp.core.domain.FirebaseFireStoreError
import com.example.wastemanagementapp.core.util.Result
import com.example.wastemanagementapp.home.domain.models.UserInfo
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getUsers() : Flow<Result<List<UserInfo>, FirebaseFireStoreError>>
}
