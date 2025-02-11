package com.example.wastemanagementapp.complaint.presentation.state

import android.net.Uri

data class ComplaintScreenState(
    val expanded: Boolean = false,
    val name: String = "",
    val address: String = "",
    val selectedCategory: String = "Select Complaint Category",
    val complaintDetails: String = "",
    val imageUri: Uri? = null
)
