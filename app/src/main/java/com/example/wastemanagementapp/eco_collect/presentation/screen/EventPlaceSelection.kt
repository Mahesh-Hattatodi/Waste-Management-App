package com.example.wastemanagementapp.eco_collect.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.wastemanagementapp.R
import com.example.wastemanagementapp.eco_collect.domain.models.SearchResponse
import com.example.wastemanagementapp.eco_collect.presentation.components.LocationSuggestionItem
import com.example.wastemanagementapp.eco_collect.presentation.viewmodel.EventPlaceSelectViewModel
import com.example.wastemanagementapp.ui.theme.WasteManagementAppTheme
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

val searchSuggestionBackground =
    Modifier
        .fillMaxWidth()
        .padding(8.dp)

@Composable
fun EventPlaceSelectScreenContainer(
    viewModel: EventPlaceSelectViewModel,
    onPopBackStack: () -> Unit
) {

    val query by viewModel.query.collectAsStateWithLifecycle()
    val searchLocations by viewModel.searchLocations.collectAsStateWithLifecycle()
    val isSearching by viewModel.isSearching.collectAsStateWithLifecycle()
    val searchPlace by viewModel.searchAddress.collectAsStateWithLifecycle()

    var isSearchFieldClicked by remember { mutableStateOf(false) }

    EventPlaceSelectScreen(
        query = query,
        onSearchChange = {
            viewModel.onSearchQueryChange(it)
        },
        searchResults = searchLocations,
        onItemSelected = {
            viewModel.onItemSelected(it)
        },
        isSearching = isSearching,
        isSearchFieldClicked = isSearchFieldClicked,
        onSearchFieldClicked = { isSearchFieldClicked = !isSearchFieldClicked },
        onSearchFieldClear = { viewModel.clearSearchField() },
        onSearchClick = {
            viewModel.searchLocations(query = query)
        },
        onChangePlace = {
            viewModel.onChangePlace(it)
        },
        place = viewModel.place,
        placeAddress = searchPlace.displayName,
        onGetAddress = {
            viewModel.getAddress()
        },
        onPopBackStack = onPopBackStack
    )
}

@Composable
fun EventPlaceSelectScreen(
    query: String = "",
    onSearchChange: (String) -> Unit = {},
    searchResults: List<SearchResponse> = emptyList(),
    onItemSelected: (SearchResponse) -> Unit = {},
    isSearching: Boolean = false,
    isSearchFieldClicked: Boolean = false,
    onSearchFieldClicked: () -> Unit = {},
    onSearchFieldClear: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    place: LatLng? = LatLng(12.9141, 74.8560),
    onChangePlace: (LatLng) -> Unit = {},
    placeAddress: String = "",
    onGetAddress: () -> Unit = {},
    onPopBackStack: () -> Unit = {}
) {

    val interactionSource = remember { MutableInteractionSource() }

    val focusManager = LocalFocusManager.current

    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            if (interaction is FocusInteraction.Focus || interaction is FocusInteraction.Unfocus) {
                onSearchFieldClicked()
            }
        }
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(12.9141, 74.8560), 10f)
    }

    Column {
        OutlinedTextField(
            value = query,
            onValueChange = { onSearchChange(it) },
            label = { Text("Search Location") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            interactionSource = interactionSource,
            trailingIcon = {
                if (isSearchFieldClicked) {
                    IconButton(
                        onClick = onSearchFieldClear
                    ) {
                        Icon(Icons.Default.Clear, contentDescription = stringResource(R.string.clear_search))
                    }
                } else {
                    Icon(Icons.Default.Search, contentDescription = stringResource(R.string.search))
                }
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = {
                    focusManager.clearFocus()
                    onSearchClick()
                }
            ),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF386641),
                unfocusedBorderColor = Color(0xFF386641),
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            )
        )

        if (isSearching) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .size(50.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(searchResults) { result ->
                    LocationSuggestionItem(
                        modifier = searchSuggestionBackground,
                        result = result
                    ) { selectedLocation ->
                        onChangePlace(LatLng(
                            selectedLocation.lat.toDouble(),
                            selectedLocation.lon.toDouble()
                        ))
                        onGetAddress()
                        onItemSelected(selectedLocation)

                        focusManager.clearFocus()
                    }
                }
            }
        }

        GoogleMap(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.7f),
            cameraPositionState = cameraPositionState,
            onMapClick = { latLon ->
                onChangePlace(latLon)
                onGetAddress()
            }
        ) {
            place?.let { latLan ->
                Marker(state = MarkerState(position = latLan))
                cameraPositionState.move(CameraUpdateFactory.newLatLngZoom(latLan, 12f))
            }
        }

        Text(
            text = stringResource(R.string.selected_place, placeAddress),
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp, top = 16.dp, bottom = 8.dp)
        )

        Box(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        ) {
            Button(
                onClick = onPopBackStack
            ) {
                Text(
                    text = stringResource(R.string.confirm_location)
                )
            }
        }
    }
}

@Preview
@Composable
private fun EventPlaceSelectionScreenPreview() {
    WasteManagementAppTheme {
        EventPlaceSelectScreen()
    }
}
