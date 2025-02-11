package com.example.wastemanagementapp.complaint.presentation.events

sealed interface ComplaintEvent {

    data class OnToggle(val toggle: Boolean) : ComplaintEvent

    data class OnSelectCategory(val category: String) : ComplaintEvent

    data object OnDismiss : ComplaintEvent

    data object SubmitComplaint : ComplaintEvent

    data class OnNameChange(val name: String) : ComplaintEvent

    data class OnAddressChange(val address: String) : ComplaintEvent

    data class OnComplaintDetailsChange(val complaintDetails: String) : ComplaintEvent
}
