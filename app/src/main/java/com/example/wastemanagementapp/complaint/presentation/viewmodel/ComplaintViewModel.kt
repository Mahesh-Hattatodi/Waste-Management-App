package com.example.wastemanagementapp.complaint.presentation.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.wastemanagementapp.complaint.presentation.events.ComplaintEvent
import com.example.wastemanagementapp.complaint.presentation.state.ComplaintScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ComplaintViewModel @Inject constructor(

) : ViewModel() {

    private val _state = MutableStateFlow(ComplaintScreenState())
    val state = _state.asStateFlow()

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
                // TODO: send complaint information to the backend
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
}