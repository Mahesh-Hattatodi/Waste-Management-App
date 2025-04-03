package com.example.wastemanagementapp.eco_collect.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wastemanagementapp.R
import com.example.wastemanagementapp.core.domain.RemoteError
import com.example.wastemanagementapp.core.util.Result
import com.example.wastemanagementapp.core.util.Screen
import com.example.wastemanagementapp.core.util.SnackBarController
import com.example.wastemanagementapp.core.util.SnackBarEvent
import com.example.wastemanagementapp.core.util.UiText
import com.example.wastemanagementapp.eco_collect.domain.models.EventLocation
import com.example.wastemanagementapp.eco_collect.domain.models.SearchResponse
import com.example.wastemanagementapp.eco_collect.domain.use_case.GetEventAddressUseCase
import com.example.wastemanagementapp.eco_collect.domain.use_case.GetSearchLocationsUseCase
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class)
@HiltViewModel
class EventPlaceSelectViewModel @Inject constructor(
    private val getSearchLocationsUseCase: GetSearchLocationsUseCase,
    private val getEventAddressUseCase: GetEventAddressUseCase
) : ViewModel() {

    private val _query = MutableStateFlow("")
    val query = _query.asStateFlow()

    private val _isSearching = MutableStateFlow(false)
    val isSearching = _isSearching.asStateFlow()

    private val _searchLocations = MutableStateFlow<List<SearchResponse>>(emptyList())
    val searchLocations = _searchLocations.asStateFlow()

    private val _searchAddress = MutableStateFlow(EventLocation())
    val searchAddress = _searchAddress.asStateFlow()

    private var shouldSearch = true

    var place by mutableStateOf<LatLng?>(null)
        private set

    init {
        viewModelScope.launch {
            query
                .debounce(700L)
                .onEach {
                    _isSearching.update { true }
                }
                .collectLatest { searchText ->
                    if (shouldSearch && searchText.isNotBlank()) {
                        searchLocations(searchText)
                    } else {
                        clearSearchSuggestions()
                        _isSearching.value = false
                    }
                }
        }
    }

    fun searchLocations(query: String) {
        Log.i("place", "getAddress: search locations function called")
        viewModelScope.launch {
            if (query.isBlank()) {
                SnackBarController.sendEvent(
                    event = SnackBarEvent(
                        message = UiText.StringResource(
                            R.string.search_field_is_empty
                        )
                    )
                )
                return@launch
            }

            _isSearching.value = true
            delay(1100)
            getSearchLocationsUseCase.invoke(query).collect { result ->
                when (result) {
                    is Result.Failure -> {
                        _searchLocations.value = emptyList()
                        when (result.error) {
                            RemoteError.HTTP_EXCEPTION -> {
                                Log.e("place", "searchLocations: http exception")
                            }

                            RemoteError.IO_EXCEPTION -> {
                                SnackBarController.sendEvent(
                                    event = SnackBarEvent(
                                        message = UiText.StringResource(
                                            R.string.check_your_internet_connection
                                        )
                                    )
                                )
                            }

                            RemoteError.JSON_DATA_EXCEPTION -> {
                                Log.i("place", "searchLocations: json parsing error")
                            }

                            RemoteError.TIMEOUT -> {
                                SnackBarController.sendEvent(
                                    event = SnackBarEvent(
                                        message = UiText.StringResource(
                                            R.string.request_time_out
                                        )
                                    )
                                )
                            }

                            RemoteError.UNKNOWN -> {
                                Log.i("place", "searchLocations: Unknown error")
                            }
                        }
                    }

                    is Result.Success -> {
                        _searchLocations.value = result.data
                    }
                }

                _isSearching.value = false
            }
        }
    }

    fun getAddress() {
        viewModelScope.launch {
            Log.i("place", "getAddress: get address function called")
            _isSearching.value = true
            delay(1100)
            place?.let { latLon ->
                val result = getEventAddressUseCase.invoke(latLon.latitude, latLon.longitude)
                when (result) {
                    is Result.Failure -> {
                        _searchLocations.value = emptyList()
                        when (result.error) {
                            RemoteError.HTTP_EXCEPTION -> {
                                Log.e("place", "searchLocations: http exception")
                            }

                            RemoteError.IO_EXCEPTION -> {
                                SnackBarController.sendEvent(
                                    event = SnackBarEvent(
                                        message = UiText.StringResource(
                                            R.string.check_your_internet_connection
                                        )
                                    )
                                )
                            }

                            RemoteError.JSON_DATA_EXCEPTION -> {
                                Log.e("place", "searchLocations: json parsing error")
                            }

                            RemoteError.TIMEOUT -> {
                                SnackBarController.sendEvent(
                                    event = SnackBarEvent(
                                        message = UiText.StringResource(
                                            R.string.request_time_out
                                        )
                                    )
                                )
                            }

                            RemoteError.UNKNOWN -> {
                                Log.i("place", "searchLocations: Unknown error")
                            }
                        }
                    }

                    is Result.Success -> {
                        _searchAddress.value = result.data
                    }
                }
                _isSearching.value = false
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        shouldSearch = true
        _query.value = query
    }

    fun onItemSelected(selectedLocation: SearchResponse) {
        shouldSearch = false
        _query.value = selectedLocation.displayName
        clearSearchSuggestions()
    }

    private fun clearSearchSuggestions() {
        _searchLocations.value = emptyList()
    }

    fun clearSearchField() {
        _query.value = ""
    }

    fun onChangePlace(latLon: LatLng) {
        place = latLon
    }

    fun setEventLocation(eventLocation: Screen.EventPlaceSelectScreen) {
        place = LatLng(
            eventLocation.lat,
            eventLocation.lon
        )

        _searchAddress.update {
            it.copy(
                displayName = eventLocation.displayName
            )
        }
    }
}