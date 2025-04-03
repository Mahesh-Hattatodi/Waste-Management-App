package com.example.wastemanagementapp.eco_collect.domain.validation

import android.net.Uri
import com.example.wastemanagementapp.core.util.Result
import com.example.wastemanagementapp.core.util.getFailureOrNull
import com.example.wastemanagementapp.eco_collect.presentation.state.EventInfoState

object EventInfoValidator {
    private fun validateEventName(name: String): Result<Unit, EventInfoValidationError> {
        return if (name.isBlank()) Result.Failure(EventInfoValidationError.NAME_EMPTY)
        else Result.Success(Unit)
    }

    private fun validateOrganizerName(name: String): Result<Unit, EventInfoValidationError> {
        return if (name.isBlank()) Result.Failure(EventInfoValidationError.ORGANIZER_NAME_EMPTY)
        else Result.Success(Unit)
    }

    private fun validatePickupDate(pickupDate: String): Result<Unit, EventInfoValidationError> {
        return if (pickupDate.isBlank()) Result.Failure(EventInfoValidationError.PICKUP_DATE_EMPTY)
        else Result.Success(Unit)
    }

    private fun validateDeliveryShift(deliveryShift: String): Result<Unit, EventInfoValidationError> {
        return if (deliveryShift.isBlank()) Result.Failure(EventInfoValidationError.DELIVERY_SHIFT_EMPTY)
        else Result.Success(Unit)
    }

    private fun validateEstimatedWeight(estimatedWeight: String): Result<Unit, EventInfoValidationError> {
        return if (estimatedWeight.isBlank()) Result.Failure(EventInfoValidationError.ESTIMATED_WEIGHT_EMPTY)
        else Result.Success(Unit)
    }

    private fun validateTruckCount(truckCount: String): Result<Unit, EventInfoValidationError> {
        return if (truckCount.isBlank()) Result.Failure(EventInfoValidationError.TRUCK_COUNT_EMPTY)
        else Result.Success(Unit)
    }

    private fun validateDate(date: String): Result<Unit, EventInfoValidationError> {
        return if (date.isBlank()) Result.Failure(EventInfoValidationError.EVENT_SCHEDULE_EMPTY)
        else Result.Success(Unit)
    }

    private fun validatePhone(phone: String): Result<Unit, EventInfoValidationError> {
        return if (phone.matches(Regex("^[0-9]{10}\$"))) Result.Success(Unit)
        else Result.Failure(EventInfoValidationError.INVALID_PHONE)
    }

    private fun validateImageUri(uri: Uri?): Result<Unit, EventInfoValidationError> {
        return if (uri == null) Result.Failure(EventInfoValidationError.IMAGE_EMPTY)
        else Result.Success(Unit)
    }

    fun validateAll(eventInfo: EventInfoState): List<EventInfoValidationError> {
        return listOfNotNull(
            validateEventName(eventInfo.eventName).getFailureOrNull(),
            validateDate(eventInfo.eventStartDate).getFailureOrNull(),
            validateDate(eventInfo.eventEndDate).getFailureOrNull(),
            validateDate(eventInfo.eventStartTime).getFailureOrNull(),
            validateDate(eventInfo.eventEndTime).getFailureOrNull(),
            validatePhone(eventInfo.organizerPhone).getFailureOrNull(),
            validateImageUri(eventInfo.imageUri).getFailureOrNull(),
            validateOrganizerName(eventInfo.organizerName).getFailureOrNull(),
            validatePickupDate(eventInfo.pickUpDate).getFailureOrNull(),
            validateDeliveryShift(eventInfo.deliveryShift).getFailureOrNull(),
            validateEstimatedWeight(eventInfo.estimatedWeight).getFailureOrNull(),
            validateTruckCount(eventInfo.truckCount).getFailureOrNull()
        )
    }
}

