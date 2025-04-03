package com.example.wastemanagementapp.eco_collect.domain.models

data class EventInfoModel(
    val eventName: String = "",
    val eventLocation: String = "",
    val lat: String = "",
    val lon: String = "",
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
    val imageUri: String = ""
)
