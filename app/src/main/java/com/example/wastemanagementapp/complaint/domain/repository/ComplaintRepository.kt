package com.example.wastemanagementapp.complaint.domain.repository

import android.net.Uri
import com.example.wastemanagementapp.complaint.domain.ComplaintDataError
import com.example.wastemanagementapp.complaint.domain.models.ComplaintInfo
import com.example.wastemanagementapp.core.util.Result


interface ComplaintRepository {

    suspend fun uploadImageToSupBase(uri: Uri, bucketName: String) : Result<String, ComplaintDataError>

    suspend fun saveComplaint(complaintInfo: ComplaintInfo) : Result<Unit, ComplaintDataError>
}