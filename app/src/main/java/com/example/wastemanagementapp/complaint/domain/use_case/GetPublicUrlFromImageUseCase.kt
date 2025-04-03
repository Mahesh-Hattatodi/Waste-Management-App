package com.example.wastemanagementapp.complaint.domain.use_case

import android.net.Uri
import com.example.wastemanagementapp.complaint.domain.ComplaintDataError
import com.example.wastemanagementapp.complaint.domain.repository.ComplaintRepository
import com.example.wastemanagementapp.core.util.Result
import javax.inject.Inject

class GetPublicUrlFromImageUseCase @Inject constructor(
    private val repository: ComplaintRepository
) {

    suspend operator fun invoke(uri: Uri, bucketName: String) : Result<String, ComplaintDataError> {
        return repository.uploadImageToSupBase(uri, bucketName)
    }
}
