package com.example.wastemanagementapp.home.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wastemanagementapp.R
import com.example.wastemanagementapp.core.domain.FirebaseFireStoreError
import com.example.wastemanagementapp.core.domain.use_case.UpdateUserPointsUseCase
import com.example.wastemanagementapp.core.util.NavigationEvent
import com.example.wastemanagementapp.core.util.Result
import com.example.wastemanagementapp.core.util.Screen
import com.example.wastemanagementapp.core.util.SnackBarAction
import com.example.wastemanagementapp.core.util.SnackBarController
import com.example.wastemanagementapp.core.util.SnackBarEvent
import com.example.wastemanagementapp.core.util.UiText
import com.example.wastemanagementapp.home.domain.models.UserInfo
import com.example.wastemanagementapp.home.domain.use_cases.GetUserInfoUseCase
import com.example.wastemanagementapp.home.presentation.mapper.UserInfoToUserInfoUiModelMapper
import com.example.wastemanagementapp.core.presentation.UserinfoUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val updateUserPointsUseCase: UpdateUserPointsUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val userInfoMapper: UserInfoToUserInfoUiModelMapper
) : ViewModel() {

    private val _navigationEvent = Channel<NavigationEvent>()
    val navigationEvent = _navigationEvent.receiveAsFlow()

    private val _userInfoUiModel = MutableStateFlow(UserinfoUiModel())
    val userInfo: StateFlow<UserinfoUiModel> = _userInfoUiModel
        .onStart {
            viewModelScope.launch { // If suspend function getUserInfo() not launched using viewModelScope.launch then that suspend function will not be having a proper coroutine context and will block the stateFlow from emitting values.
                getUserInfo()
            }
        }
        .onEach { Log.d("userInfo", "StateFlow emitted: $it") }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            UserinfoUiModel()
        )

    fun onEvent(event : HomeEvent) {
        when (event) {
            HomeEvent.OnScheduleClick -> {
                sendEvent(NavigationEvent.Navigate(Screen.ScheduleScreen))
            }
            HomeEvent.OnEcoCollectClick -> {
                sendEvent(NavigationEvent.Navigate(Screen.EcoCollectScreen))
            }
            HomeEvent.OnNotPickedClick -> {}
            HomeEvent.OnPickedClick -> {
                viewModelScope.launch {
                    when (updateUserPoints("points", 2)) {
                        is Result.Failure -> {
                            SnackBarController.sendEvent(
                                SnackBarEvent(
                                    message = UiText.StringResource(
                                        R.string.error_while_updating_points
                                    ),
                                    action = SnackBarAction(
                                        name = "retry",
                                        action = {
                                            retryUpdateUserPoints("points", 2, 3)
                                        },
                                        maxRetries = 2
                                    )
                                )
                            )
                        }
                        is Result.Success -> {
                            Log.i("ecoPoints", "onEvent: 2 eco points added to the user profile")
                        }
                    }
                }
            }
            HomeEvent.OnProfileClick -> {}
            HomeEvent.OnTrackClick -> {
                sendEvent(NavigationEvent.Navigate(Screen.TrackScreen))
            }
        }
    }

    private fun sendEvent(event: NavigationEvent) {
        viewModelScope.launch {
            _navigationEvent.send(event)
        }
    }

    private suspend fun updateUserPoints(
        updateField: String,
        incrementPoints: Int
    ): Result<Unit, FirebaseFireStoreError> {
        return updateUserPointsUseCase.invoke(
            updateField = updateField,
            incrementBy = incrementPoints
        )
    }

    private suspend fun retryUpdateUserPoints(field: String, incrementBy: Int, maxRetries: Int) {
        var retryCount = 0
        while (retryCount < maxRetries) {
            try {
                val result = updateUserPoints(field, incrementBy)
                if (result is Result.Success) {
                    // Show success message and exit
                    SnackBarController.sendEvent(
                        SnackBarEvent(
                            message = UiText.StringResource(R.string._2_eco_points_added_to_your_profile)
                        )
                    )
                    return
                } else if (result is Result.Failure) {
                    retryCount++
                }
            } catch (e: Exception) {
                retryCount++
                if (retryCount < maxRetries) {
                    delay(1000L * retryCount) // Exponential backoff
                }
            }
        }

        // If all retries fail, show a "Retry Failed" message
        SnackBarController.sendEvent(
            SnackBarEvent(UiText.StringResource(R.string.retry_failed))
        )
    }

    private suspend fun getUserInfo() {
        getUserInfoUseCase.invoke()
            .collectLatest { result ->
                when (result) {
                    is Result.Failure<*, FirebaseFireStoreError> -> {
                        Log.e("userInfo", "getUserInfo: error while updating user points")
                        return@collectLatest
                    }
                    is Result.Success<UserInfo, *> -> {
                        Log.d("userInfo", "Before update: ${_userInfoUiModel.value}")

                        _userInfoUiModel.value = userInfoMapper.map(result.data)

                        Log.d("userInfo", "After update: ${_userInfoUiModel.value}")
                    }
                }
            }
    }
}

