package com.example.wastemanagementapp.auth.presentation

import com.example.wastemanagementapp.core.util.UiText

data class RegistrationFormState(
    val name: String = "",
    val email: String = "",
    val emailError: UiText.StringResource? = null,
    val password: String = "",
    val passwordError: UiText.StringResource? = null,
    val confirmPassword: String = "",
    val confirmPasswordError: UiText.StringResource? = null
)
