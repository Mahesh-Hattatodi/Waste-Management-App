package com.example.wastemanagementapp.eco_collect.domain

import com.example.wastemanagementapp.core.util.Error

interface EventDataError : Error {
    enum class StorageError : EventDataError {
        UNAUTHORIZED,
        FORBIDDEN,
        NOT_FOUND,
        CONFLICT,
        BAD_REQUEST,
        PAYLOAD_TOO_LARGE,
        INTERNAL_SERVER_ERROR,
        TOO_MANY_REQUEST
    }

    enum class FirebaseFireStoreError : EventDataError {
        SERVER_UNAVAILABLE,
        NOT_FOUND,
        CANCELLED,
        ERROR_INVALID_DATA
    }

    data object UnknownError : EventDataError
}