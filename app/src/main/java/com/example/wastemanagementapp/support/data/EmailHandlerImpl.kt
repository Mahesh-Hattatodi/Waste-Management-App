package com.example.wastemanagementapp.support.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.wastemanagementapp.support.domain.EmailHandler
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class EmailHandlerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : EmailHandler {
    override fun sendEmail(
        emailRecipient: String,
        subject: String,
        body: String
    ) {
        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:$emailRecipient")
            putExtra(Intent.EXTRA_EMAIL, emailRecipient)
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, body)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        }
    }

    companion object {
        const val CUSTOMER_SERVICE_EMAIL = "hmahesh554@gmail.com"
    }
}