package com.example.wastemanagementapp.home.presentation.models

import androidx.compose.ui.graphics.painter.Painter
import com.example.wastemanagementapp.home.presentation.util.FeatureId

data class FeatureSelection(
    val icon: Painter,
    val text: String,
    val id: FeatureId
)
