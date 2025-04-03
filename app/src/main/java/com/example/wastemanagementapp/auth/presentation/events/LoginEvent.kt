package com.example.wastemanagementapp.auth.presentation.events

sealed interface LoginEvent {

    data object OnSignInClick : LoginEvent

    data object OnSignUpClick : LoginEvent

    data class OnEmailChange(val email: String) : LoginEvent

    data class OnWardChange(val ward: String) : LoginEvent

    data class OnPasswordChange(val password: String) : LoginEvent
}