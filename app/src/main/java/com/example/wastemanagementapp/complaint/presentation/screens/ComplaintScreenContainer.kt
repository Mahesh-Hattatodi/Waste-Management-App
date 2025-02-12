package com.example.wastemanagementapp.complaint.presentation.screens

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import com.example.wastemanagementapp.R
import com.example.wastemanagementapp.complaint.presentation.events.ComplaintEvent
import com.example.wastemanagementapp.complaint.presentation.state.ComplaintScreenState
import com.example.wastemanagementapp.complaint.presentation.viewmodel.ComplaintViewModel

@Composable
fun ComplaintScreenContainer(
    modifier: Modifier = Modifier,
    viewModel: ComplaintViewModel = hiltViewModel()
) {

    val state by viewModel.state.collectAsStateWithLifecycle()

    ComplaintScreen(
        modifier = modifier,
        state = state,
        onEvent = viewModel::onEvent,
        onUpdateImageUri = { uri ->
            uri?.let { viewModel.updateImageUri(it) }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComplaintScreen(
    modifier: Modifier = Modifier,
    context: Context = LocalContext.current,
    state: ComplaintScreenState = ComplaintScreenState(),
    onEvent: (ComplaintEvent) -> Unit = {},
    onUpdateImageUri: (Uri?) -> Unit = {}
) {
    val complaintCategories =
        context.resources.getStringArray(R.array.list_of_complaint_category)

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            onUpdateImageUri(uri)
        }
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Spacer(modifier = Modifier.height(50.dp))

            Text(
                text = stringResource(R.string.raise_a_complaint),
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Dropdown for selecting complaint topic
            ExposedDropdownMenuBox(
                expanded = state.expanded,
                onExpandedChange = {
                    onEvent(ComplaintEvent.OnToggle(!state.expanded))
                }
            ) {
                OutlinedTextField(
                    value = state.selectedCategory,
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(),
                    label = { Text(
                        text = stringResource(R.string.select_topic),
                        color = Color.Black
                    ) },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = state.expanded)
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF386641),
                        unfocusedBorderColor = Color(0xFF386641),
                        focusedTextColor = Color.Black,
                        unfocusedTextColor = Color.Black
                    )
                )

                ExposedDropdownMenu(
                    expanded = state.expanded,
                    onDismissRequest = { onEvent(ComplaintEvent.OnDismiss) },
                    modifier = Modifier.background(Color(0xFFDCEFD9)) // Light green dropdown background
                ) {
                    complaintCategories.forEach { topic ->
                        DropdownMenuItem(
                            text = { Text(text = topic, color = Color.Black) },
                            onClick = {
                                onEvent(ComplaintEvent.OnSelectCategory(topic))
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Name Input
            OutlinedTextField(
                value = state.name,
                onValueChange = {
                    onEvent(ComplaintEvent.OnNameChange(it))
                },
                label = { Text(stringResource(R.string.complaint_enter_name), color = Color.Black) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF386641),
                    unfocusedBorderColor = Color(0xFF386641),
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Postal Address Input
            OutlinedTextField(
                value = state.address,
                onValueChange = {
                    onEvent(ComplaintEvent.OnAddressChange(it))
                },
                label = { Text(stringResource(R.string.enter_postal_address), color = Color.Black) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF386641),
                    unfocusedBorderColor = Color(0xFF386641),
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Complaint Details Input
            OutlinedTextField(
                value = state.complaintDetails,
                onValueChange = {
                    onEvent(ComplaintEvent.OnComplaintDetailsChange(it))
                },
                label = { Text(stringResource(R.string.enter_complaint_details), color = Color.Black) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF386641),
                    unfocusedBorderColor = Color(0xFF386641),
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Photo Button
            Button(
                onClick = { photoPickerLauncher.launch("image/*") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF386641))
            ) {
                Text(stringResource(R.string.add_photo), color = Color.White)
            }

            state.imageUri?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Image(
                    painter = rememberAsyncImagePainter(it),
                    contentDescription = stringResource(R.string.selected_image)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Submit Button
            Button(
                onClick = {
                    Toast.makeText(
                        context,
                        context.getString(R.string.complaint_submitted), Toast.LENGTH_SHORT
                    ).show()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF386641))
            ) {
                Text(stringResource(R.string.submit_complaint), color = Color.White)
            }
        }
    }
}

@Preview
@Composable
fun ComplaintScreenPreview() {
    ComplaintScreen()
}
