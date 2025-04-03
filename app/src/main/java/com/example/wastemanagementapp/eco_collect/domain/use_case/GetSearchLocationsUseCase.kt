package com.example.wastemanagementapp.eco_collect.domain.use_case

import com.example.wastemanagementapp.core.domain.RemoteError
import com.example.wastemanagementapp.core.util.Result
import com.example.wastemanagementapp.eco_collect.domain.models.SearchResponse
import com.example.wastemanagementapp.eco_collect.domain.repository.EcoCollectRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSearchLocationsUseCase @Inject constructor(
    private val repository: EcoCollectRepository
) {
    suspend operator fun invoke(query: String) : Flow<Result<List<SearchResponse>, RemoteError>> {
        return repository.searchLocations(query)
    }
}
