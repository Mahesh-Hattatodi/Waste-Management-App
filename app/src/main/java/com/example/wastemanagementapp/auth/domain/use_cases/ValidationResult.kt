package com.example.wastemanagementapp.auth.domain.use_cases

import com.example.wastemanagementapp.core.util.UiText

data class ValidationResult(
    val success: Boolean,
    val errorMessage: UiText.StringResource? = null
)
