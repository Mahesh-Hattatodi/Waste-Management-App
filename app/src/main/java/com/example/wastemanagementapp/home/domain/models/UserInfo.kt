package com.example.wastemanagementapp.home.domain.models

data class UserInfo(
    val displayName: String = "",
    val email: String = "",
    val photoUrl: String = "",
    val points: Int = 0,
    val uuid: String = "",
    val ranking: String = ""
)
