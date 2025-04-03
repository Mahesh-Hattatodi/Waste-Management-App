package com.example.wastemanagementapp.eco_collect.domain.repository

import com.example.wastemanagementapp.core.domain.RemoteError
import com.example.wastemanagementapp.core.util.Result
import com.example.wastemanagementapp.eco_collect.domain.models.EventLocation
import com.example.wastemanagementapp.eco_collect.domain.models.SearchResponse
import kotlinx.coroutines.flow.Flow

interface EcoCollectRepository {

    suspend fun searchLocations(query: String) : Flow<Result<List<SearchResponse>, RemoteError>>

    suspend fun getAddress(lat: Double, lon: Double) : Result<EventLocation, RemoteError>
}
