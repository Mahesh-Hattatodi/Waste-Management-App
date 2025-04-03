package com.example.wastemanagementapp.eco_collect.presentation.event

import android.net.Uri

sealed interface TruckBookingEvent {
    data class OnEventNameChange(val eventName: String) : TruckBookingEvent

    data class OnEventStartDateChanged(val eventStartDate: Long?) : TruckBookingEvent

    data class OnEventEndDateChanged(val eventEndDate: Long?) : TruckBookingEvent

    data class OnEventStartTimeChanged(val minute: Int, val hour: Int) : TruckBookingEvent

    data class OnEventEndTimeChanged(val minute: Int, val hour: Int) : TruckBookingEvent

    data class OnOrganizerNameChanged(val organizerName: String) : TruckBookingEvent

    data class OnOrganizerPhoneChanged(val organizerPhone: String) : TruckBookingEvent

    data class OnEstimatedWeightChange(val estimatedWeight: String) : TruckBookingEvent

    data class OnDeliveryShiftChange(val deliveryShift: String) : TruckBookingEvent

    data class OnWasteTypeChanged(val wasteType: String) : TruckBookingEvent

    data class OnTruckCountChange(val truckCount: String) : TruckBookingEvent

    data class OnPickupDateChanged(val pickupDate: Long?) : TruckBookingEvent

    data class OnImageUriChanged(val imageUri: Uri?) : TruckBookingEvent

    data object OnSubmit : TruckBookingEvent
}
