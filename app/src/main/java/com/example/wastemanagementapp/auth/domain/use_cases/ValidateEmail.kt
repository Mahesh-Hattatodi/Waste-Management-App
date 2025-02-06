package com.example.wastemanagementapp.auth.domain.use_cases

import android.util.Patterns
import com.example.wastemanagementapp.R
import com.example.wastemanagementapp.core.util.UiText

class ValidateEmail {
    fun execute(email: String) : ValidationResult {
        if (email.isBlank()) {
            return ValidationResult(
                success = false,
                errorMessage = UiText.StringResource(
                    resId = R.string.the_email_cannot_be_blank
                )
            )
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return ValidationResult(
                success = false,
                errorMessage = UiText.StringResource(
                    resId = R.string.that_is_not_a_valid_email
                )
            )
        }

        return ValidationResult(
            success = true
        )
    }
}