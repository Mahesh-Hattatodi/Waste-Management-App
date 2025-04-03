package com.example.wastemanagementapp.eco_collect.domain.use_case

import com.example.wastemanagementapp.eco_collect.domain.validation.EventInfoValidationError
import com.example.wastemanagementapp.eco_collect.domain.validation.EventInfoValidator
import com.example.wastemanagementapp.eco_collect.presentation.state.EventInfoState
import javax.inject.Inject

class ValidateEventInfoUseCase @Inject constructor(
    private val eventInfoValidator: EventInfoValidator
) {

    operator fun invoke(eventInfo: EventInfoState): EventInfoValidationError? {
        val validationResults = eventInfoValidator.validateAll(eventInfo)

        return if (validationResults.isNotEmpty()) {
            validationResults[0]
        } else {
            null
        }
    }
}
