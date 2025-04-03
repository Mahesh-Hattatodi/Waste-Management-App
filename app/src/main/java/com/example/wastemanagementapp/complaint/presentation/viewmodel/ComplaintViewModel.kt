package com.example.wastemanagementapp.complaint.presentation.viewmodel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wastemanagementapp.R
import com.example.wastemanagementapp.complaint.domain.ComplaintDataError
import com.example.wastemanagementapp.complaint.domain.models.ComplaintInfo
import com.example.wastemanagementapp.complaint.domain.use_case.GetPublicUrlFromImageUseCase
import com.example.wastemanagementapp.complaint.domain.use_case.SubmitComplaintUseCase
import com.example.wastemanagementapp.complaint.presentation.events.ComplaintEvent
import com.example.wastemanagementapp.complaint.presentation.state.ComplaintScreenState
import com.example.wastemanagementapp.core.domain.use_case.UpdateUserPointsUseCase
import com.example.wastemanagementapp.core.util.NavigationEvent
import com.example.wastemanagementapp.core.util.Result
import com.example.wastemanagementapp.core.util.SnackBarController
import com.example.wastemanagementapp.core.util.SnackBarEvent
import com.example.wastemanagementapp.core.util.UiText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ComplaintViewModel @Inject constructor(
    private val submitComplaintUseCase: SubmitComplaintUseCase,
    private val getPublicUrlFromImageUseCase: GetPublicUrlFromImageUseCase,
    private val updateUserPointsUseCase: UpdateUserPointsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(ComplaintScreenState())
    val state = _state.asStateFlow()

    private val _navigationEvent = Channel<NavigationEvent>()
    val navigationEvent = _navigationEvent.receiveAsFlow()

    fun onEvent(event: ComplaintEvent) {
        when (event) {
            is ComplaintEvent.OnAddressChange -> {
                _state.update {
                    it.copy(
                        address = event.address
                    )
                }
            }

            ComplaintEvent.OnDismiss -> {
                _state.update {
                    it.copy(
                        expanded = false
                    )
                }
            }

            is ComplaintEvent.OnToggle -> {
                _state.update {
                    it.copy(
                        expanded = event.toggle
                    )
                }
            }

            is ComplaintEvent.OnComplaintDetailsChange -> {
                _state.update {
                    it.copy(
                        complaintDetails = event.complaintDetails
                    )
                }
            }

            is ComplaintEvent.OnNameChange -> {
                _state.update {
                    it.copy(
                        name = event.name
                    )
                }
            }

            is ComplaintEvent.OnSelectCategory -> {
                _state.update {
                    it.copy(
                        selectedCategory = event.category,
                        expanded = false
                    )
                }
            }

            ComplaintEvent.SubmitComplaint -> {
                viewModelScope.launch {
                    _state.update {
                        it.copy(
                            isSubmitting = true
                        )
                    }
                    if (
                        _state.value.imageUri != null &&
                        _state.value.name.isNotEmpty() &&
                        _state.value.complaintDetails.isNotEmpty() &&
                        _state.value.selectedCategory.isNotEmpty()
                    ) {
                        _state.value.imageUri?.let { uri ->
                            val uploadResult = getPublicUrlFromImageUseCase.invoke(
                                uri = uri,
                                bucketName = "complaints"
                            )

                            when (uploadResult) {
                                is Result.Failure -> {
                                    _state.update {
                                        it.copy(
                                            isSubmitting = false
                                        )
                                    }
                                    Log.i(
                                        "complaint",
                                        "onEvent: image not uploaded ${uploadResult.error}"
                                    )
                                    return@launch
                                }

                                is Result.Success -> {
                                    val result = submitComplaintUseCase.invoke(
                                        ComplaintInfo(
                                            name = _state.value.name,
                                            address = _state.value.address,
                                            category = _state.value.selectedCategory,
                                            complaintDetails = _state.value.complaintDetails,
                                            image = uploadResult.data
                                        )
                                    )

                                    when (result) {
                                        is Result.Failure -> {
                                            _state.update {
                                                it.copy(
                                                    isSubmitting = false
                                                )
                                            }
                                            when (result.error) {
                                                ComplaintDataError.FirebaseFireStoreError.SERVER_UNAVAILABLE -> {
                                                    SnackBarController.sendEvent(
                                                        event = SnackBarEvent(
                                                            message = UiText.StringResource(R.string.server_is_down)
                                                        )
                                                    )
                                                }

                                                ComplaintDataError.FirebaseFireStoreError.NOT_FOUND -> {
                                                    Log.e("complaint", "onEvent: Document not found")
                                                }

                                                ComplaintDataError.FirebaseFireStoreError.CANCELLED -> {
                                                    SnackBarController.sendEvent(
                                                        event = SnackBarEvent(
                                                            message = UiText.StringResource(R.string.complaint_submission_cancelled)
                                                        )
                                                    )
                                                }

                                                ComplaintDataError.FirebaseFireStoreError.ERROR_INVALID_DATA -> {
                                                    Log.e(
                                                        "complaint",
                                                        "onEvent: Invalid data ${state.value}"
                                                    )
                                                }

                                                ComplaintDataError.FirebaseStorageError.ERROR_QUOTA_EXCEEDED -> {
                                                    Log.e(
                                                        "complaint",
                                                        "onEvent: free tier constraints exceeded"
                                                    )
                                                }

                                                ComplaintDataError.FirebaseStorageError.ERROR_OBJECT_NOT_FOUND -> {
                                                    Log.e(
                                                        "complaint",
                                                        "onEvent: file not found at the given path"
                                                    )
                                                }

                                                ComplaintDataError.UnknownError -> {
                                                    Log.e("complaint", "onEvent: unknown error")
                                                }
                                            }
                                        }

                                        is Result.Success -> {
                                            _state.update {
                                                it.copy(
                                                    isSubmitting = false
                                                )
                                            }
                                            SnackBarController.sendEvent(
                                                event = SnackBarEvent(
                                                    message = UiText.StringResource(R.string.complaint_submitted)
                                                )
                                            )

                                            launch {
                                                updateUserPointsUseCase.invoke("points", 10)
                                            }

                                            sendEvent(NavigationEvent.PopBackStack)
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        _state.update {
                            it.copy(
                                isSubmitting = false
                            )
                        }
                        SnackBarController.sendEvent(
                            SnackBarEvent(
                                UiText.StringResource(R.string.fill_all_the_fields)
                            )
                        )
                    }
                }
            }
        }
    }

    fun updateImageUri(uri: Uri) {
        _state.update {
            it.copy(
                imageUri = uri
            )
        }
    }

    private fun sendEvent(event: NavigationEvent) {
        viewModelScope.launch {
            _navigationEvent.send(event)
        }
    }
}
