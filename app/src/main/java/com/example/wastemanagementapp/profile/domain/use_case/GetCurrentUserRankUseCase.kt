package com.example.wastemanagementapp.profile.domain.use_case

import com.example.wastemanagementapp.core.domain.use_case.GetUserIdUseCase
import com.example.wastemanagementapp.home.domain.models.UserInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetCurrentUserRankUseCase @Inject constructor(
    private val userIdUseCase: GetUserIdUseCase
) {

    suspend operator fun invoke(allUsers: List<UserInfo>) : UserInfo {
        val currentUserId = userIdUseCase.invoke()
        return withContext(Dispatchers.Default) {
            allUsers.find { it.uuid == currentUserId } ?: UserInfo()
        }
    }
}
