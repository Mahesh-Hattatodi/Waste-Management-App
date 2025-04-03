package com.example.wastemanagementapp.home.domain.use_cases

import com.example.wastemanagementapp.home.domain.models.UserInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CalculateUserRankingsUseCase @Inject constructor(

) {

    suspend operator fun invoke(users: List<UserInfo>) : Map<String, String> {
        return withContext(Dispatchers.Default) {
            users.sortedByDescending { it.points }
                .mapIndexed { index, userInfo -> userInfo.uuid to (index + 1).toString() }
                .toMap()
        }
    }
}