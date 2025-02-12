package com.example.wastemanagementapp.support.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wastemanagementapp.core.util.NavigationEvent
import com.example.wastemanagementapp.core.util.Screen
import com.example.wastemanagementapp.support.data.CallHandlerImpl.Companion.CUSTOMER_SERVICE_PHONE_NUMBER
import com.example.wastemanagementapp.support.data.EmailHandlerImpl.Companion.CUSTOMER_SERVICE_EMAIL
import com.example.wastemanagementapp.support.domain.CallHandler
import com.example.wastemanagementapp.support.domain.EmailHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SupportViewModel @Inject constructor(
    private val callHandler: CallHandler,
    private val emailHandler: EmailHandler
) : ViewModel() {

    private val phoneNumber = CUSTOMER_SERVICE_PHONE_NUMBER

    private val email = CUSTOMER_SERVICE_EMAIL

    private val _navigationEvent = Channel<NavigationEvent>()
    val navigationEvent = _navigationEvent.receiveAsFlow()

    fun onEvent(event: SupportScreenEvent) {
        when (event) {
            SupportScreenEvent.OnCallUsRequest -> {
                callHandler.makeCall(phoneNumber)
            }
            SupportScreenEvent.OnComplaintClick -> {
                sendEvent(NavigationEvent.Navigate(Screen.ComplaintScreen))
            }
            SupportScreenEvent.OnEmailUsRequest -> {
                emailHandler.sendEmail(email)
            }
            SupportScreenEvent.OnFaqClick -> {
                sendEvent(NavigationEvent.Navigate(Screen.FaqScreen))
            }
            SupportScreenEvent.OnFeedbackClick -> {
                sendEvent(NavigationEvent.Navigate(Screen.FeedbackScreen))
            }
        }
    }

    private fun sendEvent(event: NavigationEvent) {
        viewModelScope.launch {
            _navigationEvent.send(event)
        }
    }
}