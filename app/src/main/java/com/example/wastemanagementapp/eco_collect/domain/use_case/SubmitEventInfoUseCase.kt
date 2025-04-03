package com.example.wastemanagementapp.eco_collect.domain.use_case

import com.example.wastemanagementapp.core.util.Result
import com.example.wastemanagementapp.eco_collect.domain.EventDataError
import com.example.wastemanagementapp.eco_collect.domain.models.EventInfoModel
import com.example.wastemanagementapp.eco_collect.domain.repository.EventInfoRepository
import javax.inject.Inject

class SubmitEventInfoUseCase @Inject constructor(
    private val repository: EventInfoRepository
) {

    suspend operator fun invoke(eventInfoModel: EventInfoModel) : Result<Unit, EventDataError> {
        return repository.saveEvent(eventInfoModel)
    }
}