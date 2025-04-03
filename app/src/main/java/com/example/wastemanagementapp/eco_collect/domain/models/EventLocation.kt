package com.example.wastemanagementapp.eco_collect.domain.models

import com.google.gson.annotations.SerializedName

data class EventLocation(
    val lat: String = "12.9141",
    val lon: String = "74.8560",
    @SerializedName("display_name") val displayName: String = ""
)
