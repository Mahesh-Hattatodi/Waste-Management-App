package com.example.wastemanagementapp.home.domain.use_cases

import android.util.Log
import com.example.wastemanagementapp.core.domain.FirebaseFireStoreError
import com.example.wastemanagementapp.core.domain.repository.AuthRepository
import com.example.wastemanagementapp.core.util.Result
import com.example.wastemanagementapp.home.domain.models.UserInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val getUsersUseCase: GetUsersUseCase,
    private val calculateUserRankingsUseCase: CalculateUserRankingsUseCase
) {
    operator fun invoke(): Flow<Result<UserInfo, FirebaseFireStoreError>> = flow<Result<UserInfo, FirebaseFireStoreError>> {
        Log.d("GetUserInfoUseCase", "Fetching current user ID...")
        val currentUserId = authRepository.getCurrentUserId()

        if (currentUserId == null) {
            Log.e("GetUserInfoUseCase", "User not logged in")
            emit(Result.Failure(FirebaseFireStoreError.NOT_FOUND))
            return@flow
        }

        Log.d("GetUserInfoUseCase", "Current User ID: $currentUserId")

        getUsersUseCase.invoke().collect { result ->
            when (result) {
                is Result.Failure<*, FirebaseFireStoreError> -> {
                    Log.e("GetUserInfoUseCase", "Failed to fetch users: ${result.error}")
                    emit(Result.Failure(result.error))
                }
                is Result.Success<List<UserInfo>, *> -> {
                    val users = result.data
                    Log.d("GetUserInfoUseCase", "Fetched ${users.size} users from Firestore.")

                    val rankedUsers = calculateUserRankingsUseCase.invoke(users)
                    Log.d("GetUserInfoUseCase", "Calculated rankings: $rankedUsers")

                    val currentUser = users.find { it.uuid == currentUserId }
                    if (currentUser == null) {
                        Log.e("GetUserInfoUseCase", "Current user not found in Firestore.")
                        emit(Result.Failure(FirebaseFireStoreError.NOT_FOUND))
                        return@collect
                    }

                    val updatedUser = currentUser.copy(ranking = rankedUsers[currentUser.uuid] ?: "N/A")
                    Log.d("GetUserInfoUseCase", "Updated UserInfo: $updatedUser")

                    emit(Result.Success(updatedUser))
                }
            }
        }
    }.flowOn(Dispatchers.IO)
}
