package com.example.wastemanagementapp.home.presentation

sealed interface HomeEvent {

    data object OnProfileClick : HomeEvent

    data object OnEcoCollectClick : HomeEvent

    data object OnFeedbackClick : HomeEvent

    data object OnComplaintClick : HomeEvent

    data object OnPickedClick : HomeEvent

    data object OnNotPickedClick : HomeEvent
}