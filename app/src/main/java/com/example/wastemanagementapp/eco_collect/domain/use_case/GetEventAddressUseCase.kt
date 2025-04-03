package com.example.wastemanagementapp.eco_collect.domain.use_case

import com.example.wastemanagementapp.core.domain.RemoteError
import com.example.wastemanagementapp.core.util.Result
import com.example.wastemanagementapp.eco_collect.domain.models.EventLocation
import com.example.wastemanagementapp.eco_collect.domain.repository.EcoCollectRepository
import javax.inject.Inject

class GetEventAddressUseCase @Inject constructor(
    private val repository: EcoCollectRepository
) {
    suspend operator fun invoke(lat: Double, lon: Double) : Result<EventLocation, RemoteError> {
        return repository.getAddress(lat, lon)
    }
}
