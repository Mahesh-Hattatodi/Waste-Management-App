package com.example.wastemanagementapp.eco_collect.presentation.state

import androidx.compose.ui.graphics.vector.ImageVector

data class TextFieldEventValue(
    val valueId: EventInfoFieldValue,
    val value: String,
    val label: String,
    val icon: ImageVector? = null,
    val contentDescription: String? = null,
    val singleLine: Boolean = false,
    val readOnly: Boolean = false,
    val maxLine: Int = 1
)
