package com.example.wastemanagementapp.core.domain.use_case

import com.example.wastemanagementapp.core.domain.repository.AuthRepository
import javax.inject.Inject

class GetUserIdUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() : String? {
        return authRepository.getCurrentUserId()
    }
}