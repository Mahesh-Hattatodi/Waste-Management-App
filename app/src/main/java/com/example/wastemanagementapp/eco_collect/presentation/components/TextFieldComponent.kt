package com.example.wastemanagementapp.eco_collect.presentation.components

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.wastemanagementapp.eco_collect.presentation.state.EventInfoFieldValue

@Composable
fun TextFieldComponent(
    modifier: Modifier = Modifier,
    valueId: EventInfoFieldValue,
    value: String,
    onValueChange: (EventInfoFieldValue, String) -> Unit,
    label: String,
    icon: ImageVector? = null,
    contentDescription: String? = null,
    onClickIcon: (EventInfoFieldValue) -> Unit = {},
    singleLine: Boolean = false,
    readOnly: Boolean = false,
    maxLine: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(valueId, it) },
        label = {
            Text(
                text = label,
                color = Color.Black
            )
        },
        modifier = modifier,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF386641),
            unfocusedBorderColor = Color(0xFF386641),
            focusedLabelColor = Color.Black,
            unfocusedLabelColor = Color.Black,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black
        ),
        singleLine = singleLine,
        readOnly = readOnly,
        trailingIcon = {
            icon?.let {
                IconButton(onClick = {
                    onClickIcon(valueId)
                }) {
                    Icon(imageVector = it, contentDescription = contentDescription)
                }
            }
        },
        maxLines = maxLine
    )
}
