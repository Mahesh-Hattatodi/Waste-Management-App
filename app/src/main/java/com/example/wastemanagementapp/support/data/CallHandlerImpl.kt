package com.example.wastemanagementapp.support.data

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.wastemanagementapp.support.domain.CallHandler
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class CallHandlerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : CallHandler {
    override fun makeCall(phoneNumber: String) {
        val intent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }

        if (intent.resolveActivity(context.packageManager) != null) {
            context.startActivity(intent)
        }
    }

    companion object {
        const val CUSTOMER_SERVICE_PHONE_NUMBER = "9353857575"
    }
}