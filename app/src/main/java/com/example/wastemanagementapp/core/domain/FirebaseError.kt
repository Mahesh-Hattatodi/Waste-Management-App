package com.example.wastemanagementapp.core.domain

import com.example.wastemanagementapp.core.util.Error

enum class FirebaseFireStoreError : Error {
    SERVER_UNAVAILABLE,
    NOT_FOUND,
    CANCELLED,
    ABORTED,
    PERMISSION_DENIED,
    UNKNOWN
}
