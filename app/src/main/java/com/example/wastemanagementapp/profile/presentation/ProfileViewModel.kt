package com.example.wastemanagementapp.profile.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wastemanagementapp.core.domain.FirebaseFireStoreError
import com.example.wastemanagementapp.core.presentation.UserinfoUiModel
import com.example.wastemanagementapp.core.util.Result
import com.example.wastemanagementapp.core.util.mapAll
import com.example.wastemanagementapp.home.domain.models.UserInfo
import com.example.wastemanagementapp.home.domain.use_cases.CalculateUserRankingsUseCase
import com.example.wastemanagementapp.home.domain.use_cases.GetUsersUseCase
import com.example.wastemanagementapp.home.presentation.mapper.UserInfoToUserInfoUiModelMapper
import com.example.wastemanagementapp.profile.domain.use_case.GetAllRankedUsersUseCase
import com.example.wastemanagementapp.profile.domain.use_case.GetCurrentUserRankUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUsersUseCase: GetUsersUseCase,
    private val userRankingsUseCase: CalculateUserRankingsUseCase,
    private val getAllRankedUsersUseCase: GetAllRankedUsersUseCase,
    private val getCurrentUserRankUseCase: GetCurrentUserRankUseCase,
    private val userInfoToUserInfoUiModelMapper: UserInfoToUserInfoUiModelMapper
) : ViewModel() {
    private val _currentUser = MutableStateFlow(UserinfoUiModel())
    val currentUser = _currentUser.asStateFlow()

    private val _allUsers = MutableStateFlow<List<UserinfoUiModel>>(emptyList())
    val allUsers = _allUsers
        .onStart { loadInitialData() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            emptyList()
        )

    private fun loadInitialData() {
        viewModelScope.launch {
            getUsersUseCase.invoke()
                .collectLatest { result ->
                    when (result) {
                        is Result.Failure<*, FirebaseFireStoreError> -> {
                            Log.e("userInfo", "error while getting user info")
                            return@collectLatest
                        }
                        is Result.Success<List<UserInfo>, *> -> {
                            val usersRanks = userRankingsUseCase.invoke(result.data)
                            val listOfUpdatedUser = getAllRankedUsersUseCase.invoke(result.data, usersRanks)

                            _allUsers.value = userInfoToUserInfoUiModelMapper.mapAll(listOfUpdatedUser)

                            val currentUser = getCurrentUserRankUseCase.invoke(listOfUpdatedUser)
                            _currentUser.value = userInfoToUserInfoUiModelMapper.map(currentUser)
                        }
                    }
                }
        }
    }
}