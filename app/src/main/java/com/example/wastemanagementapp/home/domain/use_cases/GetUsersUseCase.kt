package com.example.wastemanagementapp.home.domain.use_cases

import com.example.wastemanagementapp.core.domain.FirebaseFireStoreError
import com.example.wastemanagementapp.core.util.Result
import com.example.wastemanagementapp.home.domain.models.UserInfo
import com.example.wastemanagementapp.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val homeRepository: HomeRepository
) {

    operator fun invoke(): Flow<Result<List<UserInfo>, FirebaseFireStoreError>> {
        return homeRepository.getUsers()
    }
}