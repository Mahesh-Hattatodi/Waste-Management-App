package com.example.wastemanagementapp.eco_collect.domain.repository

import android.net.Uri
import com.example.wastemanagementapp.core.util.Result
import com.example.wastemanagementapp.eco_collect.domain.EventDataError
import com.example.wastemanagementapp.eco_collect.domain.models.EventInfoModel

interface EventInfoRepository {
    suspend fun uploadImageToSupBase(uri: Uri, bucketName: String) : Result<String, EventDataError>

    suspend fun saveEvent(eventInfoModel: EventInfoModel) : Result<Unit, EventDataError>
}
