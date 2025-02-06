package com.example.wastemanagementapp.auth.domain.use_cases

import com.example.wastemanagementapp.R
import com.example.wastemanagementapp.core.util.UiText

class ValidateConfirmPassword {
    fun execute(password: String, confirmPassword: String) : ValidationResult {
        if (password != confirmPassword) {
            return ValidationResult(
                success = false,
                errorMessage = UiText.StringResource(
                    resId = R.string.passwords_do_not_match
                )
            )
        }

        return ValidationResult(
            success = true
        )
    }
}