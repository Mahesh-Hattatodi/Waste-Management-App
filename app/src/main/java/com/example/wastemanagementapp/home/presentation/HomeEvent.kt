package com.example.wastemanagementapp.home.presentation

sealed interface HomeEvent {

    data object OnProfileClick : HomeEvent

    data object OnEcoCollectClick : HomeEvent

    data object OnScheduleClick : HomeEvent

    data object OnPickedClick : HomeEvent

    data object OnNotPickedClick : HomeEvent

    data object OnTrackClick: HomeEvent
}
