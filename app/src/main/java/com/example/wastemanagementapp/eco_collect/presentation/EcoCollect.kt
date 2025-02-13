package com.example.wastemanagementapp.eco_collect.presentation

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.rememberAsyncImagePainter
import com.example.wastemanagementapp.R
import java.time.*
import java.time.format.*
import java.util.*


@Composable
fun EventTruckBookingScreenContainer(modifier: Modifier = Modifier) {
    EventTruckBookingScreen(
        context = LocalContext.current,
        scrollState = rememberScrollState()
    )
}


/*
fun GarbageTruckBookingScreen(navController: NavController, scrollState: ScrollState) {
    val context = LocalContext.current
    val scrollState = rememberScrollState()

    var name by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var zipcode by remember { mutableStateOf("") }
    var station by remember { mutableStateOf("") }
    var wasteType by remember { mutableStateOf("") }
    var deliveryShift by remember { mutableStateOf("") }
    var pickupDate by remember { mutableStateOf("") }
    var estimatedWeight by remember { mutableStateOf("") }
    var additionalInfo by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<String?>(null) }


    val calendar = Calendar.getInstance()
    val datePicker = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            pickupDate = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            imageUri = uri?.toString()
        }
    )


    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.truck_delivery_service),
            contentDescription = "Garbage Truck",
            modifier = Modifier
                .size(120.dp)
                .padding(top = 24.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))
        // Title
        Text(
            text = "Schedule Garbage Truck Service",
            color = Color.Black,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Name Field
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Enter Your Full Name") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF386641),
                unfocusedBorderColor = Color(0xFF386641),
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Address Field
        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Enter Your Address") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF386641),
                unfocusedBorderColor = Color(0xFF386641),
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Zipcode Field
        OutlinedTextField(
            value = zipcode,
            onValueChange = { zipcode = it },
            label = { Text("Enter Zipcode") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF386641),
                unfocusedBorderColor = Color(0xFF386641),
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Pickup Station Dropdown
        DropdownMenuField(
            label = "Pick up station",
            value = station,
            onValueChange = { station = it },
            items = listOf("Option1", "Option2", "Option3", "Option4")
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Waste Type Dropdown
        DropdownMenuField(
            label = "Select waste type",
            value = wasteType,
            onValueChange = { wasteType = it },
            items = listOf( "General Waste", "Glass Waste", "Organic Waste", "Paper Waste", "Plastic Waste", "Recyclable Waste", "Textile Waste" )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Delivery Shift Dropdown
        DropdownMenuField(
            label = "Delivery shift",
            value = deliveryShift,
            onValueChange = { deliveryShift = it },
            items = listOf("Morning", "Afternoon", "Evening", "Night")
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Pickup Date Field
        OutlinedTextField(
            value = pickupDate,
            onValueChange = {},
            label = { Text("Pick up date") },
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF386641),
                unfocusedBorderColor = Color(0xFF386641),
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black
            ),
            trailingIcon = {
                IconButton(onClick = { datePicker.show() }) {
                    Icon(androidx.compose.material.icons.Icons.Default.DateRange, contentDescription = "Select Date")
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Estimated Weight Dropdown
        DropdownMenuField(
            label = "Estimated weight",
            value = estimatedWeight,
            onValueChange = { estimatedWeight = it },
            items = listOf("< 5kg", "5-10kg", "10-20kg", "> 20kg")
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Additional Information Field
        OutlinedTextField(
            value = additionalInfo,
            onValueChange = { additionalInfo = it },
            label = { Text("Additional information (Optional)") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF386641),
                unfocusedBorderColor = Color(0xFF386641) ,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Upload File Button
        Button(
            onClick = { photoPickerLauncher.launch("image/*") },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF386641))
        ) {
            Text("Upload Additional File", color = Color.White)
        }

        imageUri?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = "Selected Image"
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Attach picture. File size of your documents should not exceed 1MB",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Submit Button
        Button(
            onClick = {
                if (name.isNotEmpty() && station.isNotEmpty() && wasteType.isNotEmpty() && deliveryShift.isNotEmpty() && pickupDate.isNotEmpty() && estimatedWeight.isNotEmpty()) {
                    Toast.makeText(context, "Booking Confirmed", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Please fill all fields", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF386641))
        ) {
            Text("Submit Booking", color = Color.White)
        }
    }
}*/


 */
@Composable
fun EventTruckBookingScreen(context: Context, scrollState: ScrollState) {

    var eventName by remember { mutableStateOf("") }
    var eventLocation by remember { mutableStateOf("") }
    var eventCity by remember { mutableStateOf("") }
    var eventStartDate by remember { mutableStateOf("") }
    var eventEndDate by remember { mutableStateOf("") }
    var eventStartTime by remember { mutableStateOf("") }
    var eventEndTime by remember { mutableStateOf("") }
    var organizerName by remember { mutableStateOf("") }
    var organizerPhone by remember { mutableStateOf("") }
    var zipcode by remember { mutableStateOf("") }
    var estimatedWeight by remember { mutableStateOf("") }
    var deliveryShift by remember { mutableStateOf("") }
    var eventType by remember { mutableStateOf("") }
    var expectedAttendees by remember { mutableStateOf("") }
    var wasteType by remember { mutableStateOf("") }
    var truckCount by remember { mutableStateOf("") }
    var pickupDate by remember { mutableStateOf("") }
    var additionalInfo by remember { mutableStateOf("") }
    var imageUri by remember { mutableStateOf<String?>(null) }

    val calendar = Calendar.getInstance()
    val datePicker = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            pickupDate = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            imageUri = uri?.toString()
        }
    )

    val eventStartDatePicker = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            eventStartDate = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val eventEndDatePicker = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            eventEndDate = "$dayOfMonth/${month + 1}/$year"
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    val eventStartTimePicker = TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            eventStartTime = String.format("%02d:%02d", hourOfDay, minute)
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        false
    )

    val eventEndTimePicker = TimePickerDialog(
        context,
        { _, hourOfDay, minute ->
            eventEndTime = String.format("%02d:%02d", hourOfDay, minute)
        },
        calendar.get(Calendar.HOUR_OF_DAY),
        calendar.get(Calendar.MINUTE),
        false
    )



    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.truck_delivery_service),
            contentDescription = "Garbage Truck",
            modifier = Modifier
                .size(180.dp)
                .padding(top = 20.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Schedule Garbage Trucks for Your Event",
            color = Color.Black,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = eventName,
            onValueChange = { eventName = it },
            label = { Text("Event Name") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF386641),
                unfocusedBorderColor = Color(0xFF386641) ,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = eventLocation,
            onValueChange = { eventLocation = it },
            label = { Text("Event Location") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF386641),
                unfocusedBorderColor = Color(0xFF386641) ,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = eventCity,
            onValueChange = { eventCity = it },
            label = { Text("Event City") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF386641),
                unfocusedBorderColor = Color(0xFF386641) ,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black
            )
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Zipcode Field
        OutlinedTextField(
            value = zipcode,
            onValueChange = { zipcode = it },
            label = { Text("Event Zipcode") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF386641),
                unfocusedBorderColor = Color(0xFF386641),
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = eventStartDate,
            onValueChange = {},
            label = { Text("Event Start Date") },
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF386641),
                unfocusedBorderColor = Color(0xFF386641),
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black
            ),
            trailingIcon = {
                IconButton(onClick = { eventStartDatePicker.show() }) {
                    Icon(Icons.Default.DateRange, contentDescription = "Select Start Date")
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // End Date
        OutlinedTextField(
            value = eventEndDate,
            onValueChange = {},
            label = { Text("Event End Date") },
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF386641),
                unfocusedBorderColor = Color(0xFF386641),
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black
            ),
            trailingIcon = {
                IconButton(onClick = { eventEndDatePicker.show() }) {
                    Icon(Icons.Default.DateRange, contentDescription = "Select End Date")
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Start Time
        OutlinedTextField(
            value = eventStartTime,
            onValueChange = {},
            label = { Text("Event Start Time") },
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF386641),
                unfocusedBorderColor = Color(0xFF386641),
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black
            ),
            trailingIcon = {
                IconButton(onClick = { eventStartTimePicker.show() }) {
                    Icon(Icons.Default.Schedule, contentDescription = "Select Start Time")
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // End Time
        OutlinedTextField(
            value = eventEndTime,
            onValueChange = {},
            label = { Text("Event End Time") },
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF386641),
                unfocusedBorderColor = Color(0xFF386641),
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black
            ),
            trailingIcon = {
                IconButton(onClick = { eventEndTimePicker.show() }) {
                    Icon(Icons.Default.Schedule, contentDescription = "Select End Time")
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        DropdownMenuField(
            label = "Event Type",
            value = eventType,
            onValueChange = { eventType = it },
            items = listOf("Concert", "Festival", "Sports Event", "Conference", "Wedding", "Other")
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = expectedAttendees,
            onValueChange = { expectedAttendees = it },
            label = { Text("Expected Number of Attendees") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF386641),
                unfocusedBorderColor = Color(0xFF386641) ,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        DropdownMenuField(
            label = "Waste Type",
            value = wasteType,
            onValueChange = { wasteType = it },
            items = listOf("General Waste", "Food Waste", "Recyclable Waste", "Plastic Waste", "Other")
        )

        Spacer(modifier = Modifier.height(8.dp))

        DropdownMenuField(
            label = "Number of Trucks Needed",
            value = truckCount,
            onValueChange = { truckCount = it },
            items = listOf("1", "2", "3", "4", "5+"))

        Spacer(modifier = Modifier.height(8.dp))

        // Delivery Shift Dropdown
        DropdownMenuField(
            label = "Delivery Shift",
            value = deliveryShift,
            onValueChange = { deliveryShift = it },
            items = listOf("Morning", "Afternoon", "Evening", "Night")
        )

        Spacer(modifier = Modifier.height(8.dp))


        DropdownMenuField(
            label = "Estimated weight",
            value = estimatedWeight,
            onValueChange = { estimatedWeight = it },
            items = listOf("< 5kg", "5-10kg", "10-20kg", "> 20kg")
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = pickupDate,
            onValueChange = {},
            label = { Text("Pick-up Date") },
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF386641),
                unfocusedBorderColor = Color(0xFF386641) ,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black
            ),
            trailingIcon = {
                IconButton(onClick = { datePicker.show() }) {
                    Icon(Icons.Default.DateRange, contentDescription = "Select Date")
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = organizerName,
            onValueChange = { organizerName = it },
            label = { Text("Organizer Name") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF386641),
                unfocusedBorderColor = Color(0xFF386641) ,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = organizerPhone,
            onValueChange = { organizerPhone = it },
            label = { Text("Organizer Phone Number") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF386641),
                unfocusedBorderColor = Color(0xFF386641) ,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = additionalInfo,
            onValueChange = { additionalInfo = it },
            label = { Text("Additional Information (Optional)") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF386641),
                unfocusedBorderColor = Color(0xFF386641) ,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { photoPickerLauncher.launch("image/*") },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF386641))
        ) {
            Text("Upload Permit", color = Color.White)
        }

        imageUri?.let {
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = "Uploaded Image"
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            onClick = {
                if (eventName.isNotEmpty() && eventLocation.isNotEmpty() && eventType.isNotEmpty() && eventCity.isNotEmpty() && zipcode.isNotEmpty() &&  eventStartDate.isNotEmpty() && eventEndDate.isNotEmpty() && eventStartTime.isNotEmpty() && eventEndTime.isNotEmpty() &&
                    expectedAttendees.isNotEmpty() && wasteType.isNotEmpty() && truckCount.isNotEmpty() && pickupDate.isNotEmpty() && organizerName.isNotEmpty() && organizerPhone.isNotEmpty()
                    && additionalInfo.isNotEmpty() && imageUri != null) {
                    Toast.makeText(context, "Event Truck Booking Confirmed", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Please fill all required fields", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF386641))
        ) {
            Text("Submit Booking", color = Color.White)
        }
    }
}



@Composable
fun DropdownMenuField(label: String, value: String, onValueChange: (String) -> Unit, items: List<String> = listOf("Option 1", "Option 2", "Option 3")) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = value,
            onValueChange = {},
            label = { Text(label) },
            readOnly = true,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF386641),
                unfocusedBorderColor = Color(0xFF386641),
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black
            ),
            trailingIcon = {
                IconButton(onClick = { expanded = !expanded }) {
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "Dropdown")
                }
            }
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color(0xFFDCEFD9))
        ) {
            items.forEach { item ->
                DropdownMenuItem(text = { Text(item) }, onClick = {
                    onValueChange(item)
                    expanded = false
                })
            }
        }
    }
}




@Preview(showBackground = true)
@Composable
fun GarbageTruckBookingScreenPreview() {
    val scrollState = rememberScrollState()
    EventTruckBookingScreen(LocalContext.current, scrollState)
}