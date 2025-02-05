package com.example.wastemanagementapp.core.util

sealed class NavigationEvent {

    data object PopBackStack : NavigationEvent()

    data class Navigate(val screen : Screen) : NavigationEvent()
}