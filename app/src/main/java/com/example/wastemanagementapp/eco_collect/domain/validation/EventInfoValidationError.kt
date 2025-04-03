package com.example.wastemanagementapp.eco_collect.domain.validation

import com.example.wastemanagementapp.core.util.Error

enum class EventInfoValidationError : Error {
    NAME_EMPTY,
    EVENT_SCHEDULE_EMPTY,
    INVALID_PHONE,
    IMAGE_EMPTY,
    ORGANIZER_NAME_EMPTY,
    PICKUP_DATE_EMPTY,
    DELIVERY_SHIFT_EMPTY,
    ESTIMATED_WEIGHT_EMPTY,
    TRUCK_COUNT_EMPTY
}