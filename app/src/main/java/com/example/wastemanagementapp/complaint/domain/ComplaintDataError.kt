package com.example.wastemanagementapp.complaint.domain

import com.example.wastemanagementapp.core.util.Error

sealed interface ComplaintDataError : Error {
    enum class FirebaseStorageError : ComplaintDataError {
        ERROR_QUOTA_EXCEEDED,
        ERROR_OBJECT_NOT_FOUND,
    }

    enum class FirebaseFireStoreError : ComplaintDataError {
        SERVER_UNAVAILABLE,
        NOT_FOUND,
        CANCELLED,
        ERROR_INVALID_DATA
    }

    data object UnknownError : ComplaintDataError
}