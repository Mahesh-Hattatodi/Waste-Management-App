package com.example.wastemanagementapp.complaint.domain.models

data class ComplaintInfo(
    val uid: String = "",
    val name: String = "",
    val address: String = "",
    val category: String = "",
    val complaintDetails: String = "",
    val image: String
)
