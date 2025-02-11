package com.example.wastemanagementapp.feedback.presentation

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class FeedbackViewModel @Inject constructor(

) : ViewModel() {

    private val _state = MutableStateFlow(FeedbackScreenState())
    val state = _state.asStateFlow()

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
}