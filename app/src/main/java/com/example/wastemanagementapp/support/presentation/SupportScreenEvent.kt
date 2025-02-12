package com.example.wastemanagementapp.support.presentation

sealed interface SupportScreenEvent {

    data object OnFaqClick : SupportScreenEvent

    data object OnComplaintClick : SupportScreenEvent

    data object OnFeedbackClick : SupportScreenEvent

    data object OnCallUsRequest : SupportScreenEvent

    data object OnEmailUsRequest : SupportScreenEvent
}