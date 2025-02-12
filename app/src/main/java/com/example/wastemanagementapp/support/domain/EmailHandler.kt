package com.example.wastemanagementapp.support.domain

interface EmailHandler {
    fun sendEmail(
        emailRecipient: String,
        subject: String = "",
        body: String = ""
    )
}