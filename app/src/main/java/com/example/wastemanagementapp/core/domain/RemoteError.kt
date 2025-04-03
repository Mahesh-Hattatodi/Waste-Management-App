package com.example.wastemanagementapp.core.domain

import com.example.wastemanagementapp.core.util.Error

enum class RemoteError : Error {
    HTTP_EXCEPTION,
    IO_EXCEPTION,
    JSON_DATA_EXCEPTION,
    TIMEOUT,
    UNKNOWN
}
