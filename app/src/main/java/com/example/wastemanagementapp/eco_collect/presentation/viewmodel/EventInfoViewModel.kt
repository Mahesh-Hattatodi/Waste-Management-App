package com.example.wastemanagementapp.eco_collect.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wastemanagementapp.R
import com.example.wastemanagementapp.core.util.NavigationEvent
import com.example.wastemanagementapp.core.util.Result
import com.example.wastemanagementapp.core.util.SnackBarController
import com.example.wastemanagementapp.core.util.SnackBarEvent
import com.example.wastemanagementapp.core.util.UiText
import com.example.wastemanagementapp.eco_collect.domain.EventDataError
import com.example.wastemanagementapp.eco_collect.domain.models.EventInfoModel
import com.example.wastemanagementapp.eco_collect.domain.validation.EventInfoValidationError
import com.example.wastemanagementapp.eco_collect.domain.models.EventLocation
import com.example.wastemanagementapp.eco_collect.domain.use_case.GetPublicUrlFromPermitImageUseCase
import com.example.wastemanagementapp.eco_collect.domain.use_case.SubmitEventInfoUseCase
import com.example.wastemanagementapp.eco_collect.domain.use_case.ValidateEventInfoUseCase
import com.example.wastemanagementapp.eco_collect.presentation.event.TruckBookingEvent
import com.example.wastemanagementapp.eco_collect.presentation.state.EventInfoState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class EventInfoViewModel @Inject constructor(
    private val eventInfoUseCase: ValidateEventInfoUseCase,
    private val getPublicUrlFromPermitImageUseCase: GetPublicUrlFromPermitImageUseCase,
    private val submitEventInfoUseCase: SubmitEventInfoUseCase
) : ViewModel() {

    private val _eventLocation = MutableStateFlow(EventLocation())
    val eventLocation = _eventLocation.asStateFlow()

    private val _state = MutableStateFlow(EventInfoState())
    val state = _state.asStateFlow()

    private val _navigationEvent = Channel<NavigationEvent>()
    val navigationEvent = _navigationEvent.receiveAsFlow()
    
    fun onEvent(event: TruckBookingEvent) {
        when (event) {
            is TruckBookingEvent.OnDeliveryShiftChange -> {
                _state.update {
                    it.copy(
                        deliveryShift = event.deliveryShift
                    )
                }
            }
            is TruckBookingEvent.OnEstimatedWeightChange -> {
                _state.update {
                    it.copy(
                        estimatedWeight = event.estimatedWeight
                    )
                }
            }
            is TruckBookingEvent.OnEventEndDateChanged -> {
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                event.eventEndDate?.let { date ->
                    val endDate = sdf.format(Date(date))

                    _state.update {
                        it.copy(
                            eventEndDate = endDate
                        )
                    }
                }
            }
            is TruckBookingEvent.OnEventEndTimeChanged -> {
                val time = String.format(Locale.getDefault(),"%02d:%02d", event.hour, event.minute)
                _state.update {
                    it.copy(
                        eventEndTime = time
                    )
                }
            }
            is TruckBookingEvent.OnEventNameChange -> {
                _state.update {
                    it.copy(
                        eventName = event.eventName
                    )
                }
            }
            is TruckBookingEvent.OnEventStartDateChanged -> {
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                event.eventStartDate?.let { date ->
                    val startDate = sdf.format(Date(date))

                    _state.update {
                        it.copy(
                            eventStartDate = startDate
                        )
                    }
                }
            }
            is TruckBookingEvent.OnEventStartTimeChanged -> {
                val time = String.format(Locale.getDefault(),"%02d:%02d", event.hour, event.minute)
                _state.update {
                    it.copy(
                        eventStartTime = time
                    )
                }
            }
            is TruckBookingEvent.OnImageUriChanged -> {
                _state.update {
                    it.copy(
                        imageUri = event.imageUri
                    )
                }
            }
            is TruckBookingEvent.OnOrganizerNameChanged -> {
                _state.update {
                    it.copy(
                        organizerName = event.organizerName
                    )
                }
            }
            is TruckBookingEvent.OnOrganizerPhoneChanged -> {
                _state.update {
                    it.copy(
                        organizerPhone = event.organizerPhone
                    )
                }
            }
            is TruckBookingEvent.OnPickupDateChanged -> {
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                event.pickupDate?.let { date ->
                    val pickupDate = sdf.format(Date(date))

                    _state.update {
                        it.copy(
                            pickUpDate = pickupDate
                        )
                    }
                }
            }
            TruckBookingEvent.OnSubmit -> {
                viewModelScope.launch {
                    when (eventInfoUseCase.invoke(_state.value)) {
                        EventInfoValidationError.NAME_EMPTY -> {
                            SnackBarController.sendEvent(
                                event = SnackBarEvent(
                                    message = UiText.StringResource(
                                        R.string.event_name_is_empty
                                    )
                                )
                            )
                            return@launch
                        }
                        EventInfoValidationError.EVENT_SCHEDULE_EMPTY -> {
                            SnackBarController.sendEvent(
                                event = SnackBarEvent(
                                    message = UiText.StringResource(
                                        R.string.all_schedules_need_to_be_specified
                                    )
                                )
                            )
                            return@launch
                        }
                        EventInfoValidationError.INVALID_PHONE -> {
                            SnackBarController.sendEvent(
                                event = SnackBarEvent(
                                    message = UiText.StringResource(
                                        R.string.invalid_phone_number
                                    )
                                )
                            )
                            return@launch
                        }
                        EventInfoValidationError.IMAGE_EMPTY -> {
                            SnackBarController.sendEvent(
                                event = SnackBarEvent(
                                    message = UiText.StringResource(
                                        R.string.permit_is_empty
                                    )
                                )
                            )
                            return@launch
                        }
                        EventInfoValidationError.ORGANIZER_NAME_EMPTY -> {
                            SnackBarController.sendEvent(
                                event = SnackBarEvent(
                                    message = UiText.StringResource(
                                        R.string.organizer_name_is_empty
                                    )
                                )
                            )
                            return@launch
                        }
                        EventInfoValidationError.PICKUP_DATE_EMPTY -> {
                            SnackBarController.sendEvent(
                                event = SnackBarEvent(
                                    message = UiText.StringResource(
                                        R.string.pickup_date_is_not_specified
                                    )
                                )
                            )
                            return@launch
                        }
                        EventInfoValidationError.DELIVERY_SHIFT_EMPTY -> {
                            SnackBarController.sendEvent(
                                event = SnackBarEvent(
                                    message = UiText.StringResource(
                                        R.string.delivery_shift_is_not_specified
                                    )
                                )
                            )
                            return@launch
                        }
                        EventInfoValidationError.ESTIMATED_WEIGHT_EMPTY -> {
                            SnackBarController.sendEvent(
                                event = SnackBarEvent(
                                    message = UiText.StringResource(
                                        R.string.estimated_weight_is_empty
                                    )
                                )
                            )
                            return@launch
                        }
                        EventInfoValidationError.TRUCK_COUNT_EMPTY -> {
                            SnackBarController.sendEvent(
                                event = SnackBarEvent(
                                    message = UiText.StringResource(
                                        R.string.truck_count_is_not_specified
                                    )
                                )
                            )
                            return@launch
                        }
                        null -> {
                            updateSubmitState(true)
                            if (_eventLocation.value.displayName.isEmpty()) {
                                SnackBarController.sendEvent(
                                    event = SnackBarEvent(
                                        message = UiText.StringResource(
                                            R.string.event_location_is_not_specified
                                        )
                                    )
                                )
                                updateSubmitState(false)
                                return@launch
                            }
                            _state.value.imageUri?.let {
                                val uploadResultDeferred = async {
                                    getPublicUrlFromPermitImageUseCase.invoke(
                                        uri = it,
                                        bucketName = "complaints")
                                }

                                when (val uploadResult = uploadResultDeferred.await()) {
                                    is Result.Failure -> {
                                        updateSubmitState(false)
                                        when (uploadResult.error) {
                                            EventDataError.StorageError.PAYLOAD_TOO_LARGE -> {
                                                SnackBarController.sendEvent(
                                                    event = SnackBarEvent(
                                                        message = UiText.StringResource(
                                                            R.string.image_file_too_large
                                                        )
                                                    )
                                                )
                                                return@launch
                                            }
                                            EventDataError.StorageError.TOO_MANY_REQUEST -> {
                                                SnackBarController.sendEvent(
                                                    event = SnackBarEvent(
                                                        message = UiText.StringResource(
                                                            R.string.too_many_requests_try_again_after_some_time
                                                        )
                                                    )
                                                )
                                                return@launch
                                            }
                                            else -> {
                                                SnackBarController.sendEvent(
                                                    event = SnackBarEvent(
                                                        message = UiText.StringResource(
                                                            R.string.something_went_wrong
                                                        )
                                                    )
                                                )
                                                return@launch
                                            }
                                        }
                                    }
                                    is Result.Success -> {
                                        val result = submitEventInfoUseCase.invoke(
                                            eventInfoModel = EventInfoModel(
                                                eventName = _state.value.eventName,
                                                eventLocation = _eventLocation.value.displayName,
                                                lat = _eventLocation.value.lat,
                                                lon = _eventLocation.value.lon,
                                                eventStartDate = _state.value.eventStartDate,
                                                eventEndDate = _state.value.eventEndDate,
                                                eventStartTime = _state.value.eventStartTime,
                                                eventEndTime = _state.value.eventEndTime,
                                                organizerName = _state.value.organizerName,
                                                organizerPhone = _state.value.organizerPhone,
                                                estimatedWeight = _state.value.estimatedWeight,
                                                deliveryShift = _state.value.deliveryShift,
                                                wasteType = _state.value.wasteType,
                                                truckCount = _state.value.truckCount,
                                                pickUpDate = _state.value.pickUpDate,
                                                imageUri = uploadResult.data
                                            )
                                        )

                                        when (result) {
                                            is Result.Failure -> {
                                                updateSubmitState(false)
                                                when (result.error) {
                                                    EventDataError.FirebaseFireStoreError.CANCELLED -> {
                                                        SnackBarController.sendEvent(
                                                            event = SnackBarEvent(
                                                                message = UiText.StringResource(
                                                                    R.string.event_information_submission_cancelled
                                                                )
                                                            )
                                                        )
                                                        return@launch
                                                    }
                                                    else -> {
                                                        SnackBarController.sendEvent(
                                                            event = SnackBarEvent(
                                                                message = UiText.StringResource(
                                                                    R.string.something_went_wrong
                                                                )
                                                            )
                                                        )
                                                        return@launch
                                                    }
                                                }
                                            }
                                            is Result.Success -> {
                                                SnackBarController.sendEvent(
                                                    event = SnackBarEvent(
                                                        message = UiText.StringResource(
                                                            R.string.event_information_submitted
                                                        )
                                                    )
                                                )
                                                updateSubmitState(false)
                                                sendEvent(NavigationEvent.PopBackStack)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            is TruckBookingEvent.OnTruckCountChange -> {
                _state.update {
                    it.copy(
                        truckCount = event.truckCount
                    )
                }
            }
            is TruckBookingEvent.OnWasteTypeChanged -> {
                _state.update {
                    it.copy(
                        wasteType = event.wasteType
                    )
                }
            }
        }
    }

    fun setEventLocation(eventLocation: EventLocation) {
        _eventLocation.value = eventLocation
    }

    private fun sendEvent(event: NavigationEvent) {
        viewModelScope.launch {
            _navigationEvent.send(event)
        }
    }

    private fun updateSubmitState(state: Boolean) {
        _state.update {
            it.copy(
                isSubmitting = state
            )
        }
    }
}
