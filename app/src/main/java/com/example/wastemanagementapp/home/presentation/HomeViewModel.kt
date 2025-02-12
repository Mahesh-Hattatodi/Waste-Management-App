package com.example.wastemanagementapp.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wastemanagementapp.core.util.NavigationEvent
import com.example.wastemanagementapp.core.util.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(

) : ViewModel(){

    private val _navigationEvent = Channel<NavigationEvent>()
    val navigationEvent = _navigationEvent.receiveAsFlow()

    fun onEvent(event : HomeEvent) {
        when (event) {
            HomeEvent.OnComplaintClick -> {
                sendEvent(NavigationEvent.Navigate(Screen.ComplaintScreen))
            }
            HomeEvent.OnEcoCollectClick -> {
                sendEvent(NavigationEvent.Navigate(Screen.EcoCollectScreen))
            }
            HomeEvent.OnFeedbackClick -> {
                sendEvent(NavigationEvent.Navigate(Screen.FeedbackScreen))
            }
            HomeEvent.OnNotPickedClick -> TODO()
            HomeEvent.OnPickedClick -> TODO()
            HomeEvent.OnProfileClick -> TODO()
        }
    }

    private fun sendEvent(event: NavigationEvent) {
        viewModelScope.launch {
            _navigationEvent.send(event)
        }
    }
}