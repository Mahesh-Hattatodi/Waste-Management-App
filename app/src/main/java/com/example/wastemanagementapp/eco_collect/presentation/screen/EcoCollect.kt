package com.example.wastemanagementapp.eco_collect.presentation.screen

import android.content.Context
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import com.example.wastemanagementapp.R
import com.example.wastemanagementapp.core.util.NavigationEvent
import com.example.wastemanagementapp.core.util.ObserveAsEvents
import com.example.wastemanagementapp.eco_collect.domain.models.EventLocation
import com.example.wastemanagementapp.eco_collect.presentation.components.DropdownMenuField
import com.example.wastemanagementapp.eco_collect.presentation.components.EventDatePicker
import com.example.wastemanagementapp.eco_collect.presentation.components.EventTimePicker
import com.example.wastemanagementapp.eco_collect.presentation.components.TextFieldComponent
import com.example.wastemanagementapp.eco_collect.presentation.event.TruckBookingEvent
import com.example.wastemanagementapp.eco_collect.presentation.state.EventInfoFieldValue
import com.example.wastemanagementapp.eco_collect.presentation.state.EventInfoState
import com.example.wastemanagementapp.eco_collect.presentation.state.TextFieldEventValue
import com.example.wastemanagementapp.eco_collect.presentation.viewmodel.EventInfoViewModel

val textFieldModifier = Modifier
    .fillMaxWidth()

val spacerModifier = Modifier
    .height(8.dp)

@Composable
fun EventTruckBookingScreenContainer(
    viewModel: EventInfoViewModel = hiltViewModel(),
    onNavigate: (EventLocation) -> Unit = {},
    onPopBackStack: () -> Unit
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    val location by viewModel.eventLocation.collectAsStateWithLifecycle()

    ObserveAsEvents(
        flow = viewModel.navigationEvent
    ) { event ->
        when (event) {
            is NavigationEvent.Navigate -> Unit
            NavigationEvent.PopBackStack -> onPopBackStack()
        }
    }

    EventTruckBookingScreen(
        state = state,
        location = location.displayName,
        onEvent = viewModel::onEvent,
        onNavigate = {
            onNavigate(
                EventLocation(
                    lat = location.lat,
                    lon = location.lon,
                    displayName = location.displayName
                )
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventTruckBookingScreen(
    context: Context = LocalContext.current,
    state: EventInfoState = EventInfoState(),
    location: String = "",
    onEvent: (TruckBookingEvent) -> Unit = {},
    onNavigate: () -> Unit = {}
) {

    val textFieldEventValues by remember(state) {
        mutableStateOf(listOf(
            TextFieldEventValue(
                valueId = EventInfoFieldValue.EVENT_NAME,
                value = state.eventName,
                label = "Event Name",
                singleLine = true
            ),
            TextFieldEventValue(
                valueId = EventInfoFieldValue.EVENT_PLACE,
                value = location,
                label = "Event Location",
                maxLine = 5,
                icon = Icons.Default.AddLocation,
                contentDescription = "Event location",
                readOnly = true
            ),
            TextFieldEventValue(
                valueId = EventInfoFieldValue.EVENT_START_DATE,
                value = state.eventStartDate,
                label = "Event start date",
                icon = Icons.Default.DateRange,
                contentDescription = "Event start date",
                singleLine = true,
                readOnly = true
            ),
            TextFieldEventValue(
                valueId = EventInfoFieldValue.EVENT_END_DATE,
                value = state.eventEndDate,
                label = "Event end date",
                icon = Icons.Default.DateRange,
                contentDescription = "Event end date",
                singleLine = true,
                readOnly = true
            ),
            TextFieldEventValue(
                valueId = EventInfoFieldValue.EVENT_START_TIME,
                value = state.eventStartTime,
                label = "Event start time",
                icon = Icons.Default.Schedule,
                contentDescription = "Event start time",
                singleLine = true,
                readOnly = true
            ),
            TextFieldEventValue(
                valueId = EventInfoFieldValue.EVENT_END_TIME,
                value = state.eventEndTime,
                label = "Event end time",
                icon = Icons.Default.Schedule,
                contentDescription = "Event end time",
                singleLine = true,
                readOnly = true
            ),
            TextFieldEventValue(
                valueId = EventInfoFieldValue.PICKUP_DATE,
                value = state.pickUpDate,
                label = "Pickup Date",
                icon = Icons.Default.DateRange,
                contentDescription = "Pickup date",
                singleLine = true,
                readOnly = true
            ),
            TextFieldEventValue(
                valueId = EventInfoFieldValue.EVENT_ORGANIZER_NAME,
                value = state.organizerName,
                label = "Organizer Name",
                singleLine = true
            ),
            TextFieldEventValue(
                valueId = EventInfoFieldValue.EVENT_ORGANIZER_PHONE,
                value = state.organizerPhone,
                label = "Organizer Phone",
                singleLine = true
            )
        ))
    }

    val wasteTypes = remember { context.resources.getStringArray(R.array.list_of_waste_type) }
    val truckCounts = remember { context.resources.getStringArray(R.array.list_of_truck_count) }
    val deliveryShifts = remember { context.resources.getStringArray(R.array.list_of_delivery_shift) }
    val estimatedWeights = remember { context.resources.getStringArray(R.array.list_of_estimated_weight) }

    var isStartDateOpen by remember { mutableStateOf(false) }
    var isEndDateOpen by remember { mutableStateOf(false) }
    var isStartTimeOpen by remember { mutableStateOf(false) }
    var isEndTimeOpen by remember { mutableStateOf(false) }
    var isPickupDateOpen by remember { mutableStateOf(false) }

    if (isStartDateOpen) {
        EventDatePicker(
            onDateSelected = { date ->
                onEvent(TruckBookingEvent.OnEventStartDateChanged(date))
            },
            onDismiss = { isStartDateOpen = !isStartDateOpen }
        )
    }

    if (isEndDateOpen) {
        EventDatePicker(
            onDateSelected = { date ->
                onEvent(TruckBookingEvent.OnEventEndDateChanged(date))
            },
            onDismiss = { isEndDateOpen = !isEndDateOpen }
        )
    }

    if (isPickupDateOpen) {
        EventDatePicker(
            onDateSelected = { date ->
                onEvent(TruckBookingEvent.OnPickupDateChanged(date))
            },
            onDismiss = { isPickupDateOpen = !isPickupDateOpen }
        )
    }

    if (isStartTimeOpen) {
        EventTimePicker(
            onConfirm = { timePickerState ->
                onEvent(TruckBookingEvent.OnEventStartTimeChanged(
                    minute = timePickerState.minute,
                    hour = timePickerState.hour
                ))
                isStartTimeOpen = !isStartTimeOpen
            },
            onDismiss = { isStartTimeOpen = !isStartTimeOpen }
        )
    }

    if (isEndTimeOpen) {
        EventTimePicker(
            onConfirm = { timePickerState ->
                onEvent(TruckBookingEvent.OnEventEndTimeChanged(
                    minute = timePickerState.minute,
                    hour = timePickerState.hour
                ))
                isEndTimeOpen = !isEndTimeOpen
            },
            onDismiss = { isEndTimeOpen = !isEndTimeOpen }
        )
    }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            onEvent(TruckBookingEvent.OnImageUriChanged(uri))
        }
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        item {
            Image(
                painter = painterResource(id = R.drawable.truck_delivery_service),
                contentDescription = stringResource(R.string.garbage_truck),
                modifier = Modifier
                    .size(180.dp)
                    .padding(top = 20.dp)
            )

            Spacer(modifier = spacerModifier.height(8.dp))

            Text(
                text = stringResource(R.string.schedule_garbage_trucks_for_your_event),
                color = Color.Black,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }

        items(
            textFieldEventValues,
            key = { value -> value.valueId }
        ) { item ->
            TextFieldComponent(
                modifier = textFieldModifier,
                valueId = item.valueId,
                value = item.value,
                onValueChange = { eventInfoFieldValue, infoValue ->
                    when (eventInfoFieldValue) {
                        EventInfoFieldValue.EVENT_NAME -> {
                            onEvent(TruckBookingEvent.OnEventNameChange(infoValue))
                        }
                        EventInfoFieldValue.EVENT_ORGANIZER_NAME -> {
                            onEvent(TruckBookingEvent.OnOrganizerNameChanged(infoValue))
                        }
                        EventInfoFieldValue.EVENT_ORGANIZER_PHONE -> {
                            onEvent(TruckBookingEvent.OnOrganizerPhoneChanged(infoValue))
                        }
                        else -> Unit
                    }
                },
                label = item.label,
                icon = item.icon,
                contentDescription = item.contentDescription,
                onClickIcon = { eventInfoFieldValue ->
                    when (eventInfoFieldValue) {
                        EventInfoFieldValue.EVENT_START_DATE -> {
                            isStartDateOpen = true
                        }
                        EventInfoFieldValue.EVENT_END_DATE -> {
                            isEndDateOpen = true
                        }
                        EventInfoFieldValue.EVENT_START_TIME -> {
                            isStartTimeOpen = true
                        }
                        EventInfoFieldValue.EVENT_END_TIME -> {
                            isEndTimeOpen = true
                        }
                        EventInfoFieldValue.PICKUP_DATE -> {
                            isPickupDateOpen = true
                        }
                        EventInfoFieldValue.EVENT_PLACE -> {
                            onNavigate()
                        }
                        else -> {
                            Unit
                        }
                    }
                },
                singleLine = item.singleLine,
                readOnly = item.readOnly,
                maxLine = item.maxLine
            )

            Spacer(modifier = spacerModifier)
        }

        item {
            DropdownMenuField(
                label = stringResource(R.string.waste_type),
                value = state.wasteType,
                onValueChange = { onEvent(TruckBookingEvent.OnWasteTypeChanged(it)) },
                items = wasteTypes.toList()
            )

            Spacer(modifier = spacerModifier)

            DropdownMenuField(
                label = stringResource(R.string.number_of_trucks_needed),
                value = state.truckCount,
                onValueChange = { onEvent(TruckBookingEvent.OnTruckCountChange(it)) },
                items = truckCounts.toList()
            )

            Spacer(modifier = spacerModifier)

            // Delivery Shift Dropdown
            DropdownMenuField(
                label = stringResource(R.string.delivery_shift),
                value = state.deliveryShift,
                onValueChange = { onEvent(TruckBookingEvent.OnDeliveryShiftChange(it)) },
                items = deliveryShifts.toList()
            )

            Spacer(modifier = spacerModifier)


            DropdownMenuField(
                label = stringResource(R.string.estimated_weight),
                value = state.estimatedWeight,
                onValueChange = { onEvent(TruckBookingEvent.OnEstimatedWeightChange(it)) },
                items = estimatedWeights.toList()
            )

            Spacer(modifier = spacerModifier)
        }

        item {
            Button(
                onClick = { photoPickerLauncher.launch("image/*") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF386641))
            ) {
                Text(text = stringResource(R.string.upload_permit), color = Color.White)
            }

            state.imageUri?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = stringResource(R.string.uploaded_image)
                )
            }

            Spacer(modifier = spacerModifier.height(12.dp))
        }

        item {
            Button(
                onClick = {
                    onEvent(TruckBookingEvent.OnSubmit)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF386641),
                    disabledContainerColor = Color(0xFFD3D3D3)
                ),
                enabled = !state.isSubmitting
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = if (state.isSubmitting) Arrangement.SpaceBetween else Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = stringResource(R.string.submit_booking), color = Color.White)

                    if (state.isSubmitting) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .size(20.dp)
                                .padding(end = 8.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GarbageTruckBookingScreenPreview() {
    EventTruckBookingScreen()
}