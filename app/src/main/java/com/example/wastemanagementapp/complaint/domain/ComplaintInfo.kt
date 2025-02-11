package com.example.wastemanagementapp.complaint.domain

import android.net.Uri

data class ComplaintInfo(
    val name: String,
    val address: String,
    val complaintDetails: String,
    val image: Uri
)
