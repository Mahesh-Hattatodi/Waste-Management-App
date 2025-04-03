package com.example.wastemanagementapp.eco_collect.presentation.state

import android.net.Uri

data class EventInfoState(
    val eventName: String = "",
    val eventStartDate: String = "",
    val eventEndDate: String = "",
    val eventStartTime: String = "",
    val eventEndTime: String = "",
    val organizerName: String = "",
    val organizerPhone: String = "",
    val estimatedWeight: String = "",
    val deliveryShift: String = "",
    val wasteType: String = "",
    val truckCount: String = "",
    val pickUpDate: String = "",
    val imageUri: Uri? = null,
    val isSubmitting: Boolean = false
)
