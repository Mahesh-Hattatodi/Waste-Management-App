package com.example.wastemanagementapp.feedback.presentation

sealed interface FeedbackEvent {
    data class OnToggle(val toggle: Boolean) : FeedbackEvent

    data class OnSelectFeedbackTopic(val topic: String) : FeedbackEvent

    data object OnDismiss : FeedbackEvent

    data class OnEmojiChange(val emoji: String) : FeedbackEvent

    data class OnFeedbackChange(val feedback: String) : FeedbackEvent

    data object OnSubmitFeedback : FeedbackEvent
}