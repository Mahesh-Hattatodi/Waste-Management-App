package com.example.wastemanagementapp.core.domain

import android.net.Uri
import androidx.compose.runtime.Stable

@Stable
data class UserProfile(
    val uuid: String = "",
    val displayName: String? = "Loading",
    val email: String? = "Loading",
    val photoUrl: String? = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQ4YreOWfDX3kK-QLAbAL4ufCPc84ol2MA8Xg&s"
)
