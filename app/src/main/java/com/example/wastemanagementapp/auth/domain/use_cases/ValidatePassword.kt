package com.example.wastemanagementapp.auth.domain.use_cases

import com.example.wastemanagementapp.R
import com.example.wastemanagementapp.core.util.UiText

class ValidatePassword {

    fun execute(password: String) : ValidationResult {
        if (password.isBlank()) {
            return ValidationResult(
                success = false,
                errorMessage = UiText.StringResource(
                    resId = R.string.password_cannot_be_empty
                )
            )
        }
        if (password.length < 8) {
            return ValidationResult(
                success = false,
                errorMessage = UiText.StringResource(
                    resId = R.string.the_password_needs_to_consist_of_eight_length,
                    args = arrayOf(8)
                )
            )
        }
        val containsLettersAndDigits =
            password.any { it.isDigit() } && password.any { it.isLetter() }

        if (!containsLettersAndDigits) {
            return ValidationResult(
                success = false,
                errorMessage = UiText.StringResource(
                    resId = R.string.the_password_should_contain_atleast_one_letter_and_one_digit
                )
            )
        }

        return ValidationResult(
            success = true
        )
    }
}