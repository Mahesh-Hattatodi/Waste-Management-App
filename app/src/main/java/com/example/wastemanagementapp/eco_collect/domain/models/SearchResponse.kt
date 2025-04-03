package com.example.wastemanagementapp.eco_collect.domain.models

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    val lat: String,
    val lon: String,
    @SerializedName("display_name") val displayName: String
)
