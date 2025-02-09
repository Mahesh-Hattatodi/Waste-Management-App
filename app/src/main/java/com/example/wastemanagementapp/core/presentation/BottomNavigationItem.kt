package com.example.wastemanagementapp.core.presentation

import androidx.compose.ui.graphics.painter.Painter
import com.example.wastemanagementapp.core.util.Screen

data class BottomNavigationItem(
    val iconSelected: Painter,
    val name: String,
    val route: Screen
)

