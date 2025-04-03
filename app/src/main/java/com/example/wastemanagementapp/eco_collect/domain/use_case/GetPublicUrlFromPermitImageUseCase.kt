package com.example.wastemanagementapp.eco_collect.domain.use_case

import android.net.Uri
import com.example.wastemanagementapp.core.util.Result
import com.example.wastemanagementapp.eco_collect.domain.EventDataError
import com.example.wastemanagementapp.eco_collect.domain.repository.EventInfoRepository
import javax.inject.Inject

class GetPublicUrlFromPermitImageUseCase @Inject constructor(
    private val repository: EventInfoRepository
) {

    suspend operator fun invoke(uri: Uri, bucketName: String) : Result<String, EventDataError> {
        return repository.uploadImageToSupBase(
            uri, bucketName
        )
    }
}