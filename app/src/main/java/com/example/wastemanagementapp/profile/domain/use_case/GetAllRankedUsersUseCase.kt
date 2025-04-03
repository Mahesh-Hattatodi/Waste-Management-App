package com.example.wastemanagementapp.profile.domain.use_case

import com.example.wastemanagementapp.home.domain.models.UserInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetAllRankedUsersUseCase @Inject constructor() {
    suspend operator fun invoke(allUsers: List<UserInfo>, userRanks: Map<String, String>) : List<UserInfo> {
        return withContext(Dispatchers.Default) {
            val listOfUpdatedUser : MutableList<UserInfo> = emptyList<UserInfo>().toMutableList()
            allUsers.forEach { user ->
                val updatedUser = userRanks[user.uuid]?.let {
                    user.copy(
                        ranking = it
                    )
                }

                if (updatedUser != null) {
                    listOfUpdatedUser.add(updatedUser)
                }
            }

            listOfUpdatedUser.sortByDescending { it.points }
            listOfUpdatedUser.toList()
        }
    }
}