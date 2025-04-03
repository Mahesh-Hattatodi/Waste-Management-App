package com.example.wastemanagementapp.feedback.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wastemanagementapp.R
import com.example.wastemanagementapp.core.domain.use_case.UpdateUserPointsUseCase
import com.example.wastemanagementapp.core.util.NavigationEvent
import com.example.wastemanagementapp.core.util.Result
import com.example.wastemanagementapp.core.util.SnackBarController
import com.example.wastemanagementapp.core.util.SnackBarEvent
import com.example.wastemanagementapp.core.util.UiText
import com.example.wastemanagementapp.feedback.domain.use_case.SubmitUserFeedbackUseCase
import com.example.wastemanagementapp.feedback.presentation.model.SubmitFeedback
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FeedbackViewModel @Inject constructor(
    private val submitUserFeedbackUseCase: SubmitUserFeedbackUseCase,
    private val updateUserPointsUseCase: UpdateUserPointsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(FeedbackScreenState())
    val state = _state.asStateFlow()

    private val _navEvent = Channel<NavigationEvent>()
    val navEvent = _navEvent.receiveAsFlow()

    fun onEvent(event: FeedbackEvent) {
        when (event) {
            FeedbackEvent.OnDismiss -> {
                _state.update {
                    it.copy(
                        isExpanded = false
                    )
                }
            }

            is FeedbackEvent.OnEmojiChange -> {
                _state.update {
                    it.copy(
                        emoji = event.emoji
                    )
                }
            }

            is FeedbackEvent.OnFeedbackChange -> {
                _state.update {
                    it.copy(
                        feedback = event.feedback
                    )
                }
            }

            is FeedbackEvent.OnSelectFeedbackTopic -> {
                _state.update {
                    it.copy(
                        selectedTopic = event.topic,
                        isExpanded = false
                    )
                }
            }

            FeedbackEvent.OnSubmitFeedback -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            isSubmitting = true
                        )
                    }
                    if (
                        _state.value.emoji.isNotEmpty()
                        && _state.value.selectedTopic.isNotEmpty()
                        && _state.value.feedback.isNotEmpty()
                    ) {
                        val result = submitUserFeedbackUseCase.invoke(
                            submitFeedback = SubmitFeedback(
                                emoji = _state.value.emoji,
                                topic = _state.value.selectedTopic,
                                feedback = _state.value.feedback
                            )
                        )

                        when (result) {
                            is Result.Failure -> {
                                _state.update {
                                    it.copy(
                                        isSubmitting = false
                                    )
                                }
                                SnackBarController.sendEvent(
                                    SnackBarEvent(
                                        UiText.StringResource(R.string.check_your_internet_connection)
                                    )
                                )
                            }
                            is Result.Success -> {
                                _state.update {
                                    it.copy(
                                        isSubmitting = false
                                    )
                                }
                                SnackBarController.sendEvent(
                                    SnackBarEvent(
                                        UiText.StringResource(R.string.feedback_submitted)
                                    )
                                )
                                launch {
                                    when (updateUserPointsUseCase.invoke("points", 5)) {
                                        is Result.Failure<*, *> -> {
                                            Log.e("ecoPoints", "onEvent: Error while updating eco points for user")
                                        }
                                        is Result.Success<*, *> -> {
                                            Log.i("ecoPoints", "onEvent: eco points added")
                                        }
                                    }
                                }
                                sendEvent(NavigationEvent.PopBackStack)
                            }
                        }
                    } else {
                        _state.update {
                            it.copy(
                                isSubmitting = false
                            )
                        }
                        SnackBarController.sendEvent(
                            SnackBarEvent(
                                UiText.StringResource(R.string.fill_all_the_fields)
                            )
                        )
                    }
                }
            }

            is FeedbackEvent.OnToggle -> {
                _state.update {
                    it.copy(
                        isExpanded = !event.toggle
                    )
                }
            }
        }
    }

    private fun sendEvent(event: NavigationEvent) {
        viewModelScope.launch {
            _navEvent.send(event)
        }
    }
}
