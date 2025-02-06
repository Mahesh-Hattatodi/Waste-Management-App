package com.example.wastemanagementapp.auth.presentation.events

sealed class SignUpEvent {

    data class OnNameChange(val name: String) : SignUpEvent()

    data class OnEmailChange(val email: String) : SignUpEvent()

    data class OnPasswordChange(val password: String) : SignUpEvent()

    data class OnConfirmPasswordChange(val confirmPassword: String) : SignUpEvent()

    data object OnSignUpClick : SignUpEvent()
}
