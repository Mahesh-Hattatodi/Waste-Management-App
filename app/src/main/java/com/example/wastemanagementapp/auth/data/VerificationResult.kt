package com.example.wastemanagementapp.auth.data

import com.example.wastemanagementapp.core.util.UiText

data class VerificationResult(
    val success: Boolean = false,
    val message: UiText.StringResource? = null
)
