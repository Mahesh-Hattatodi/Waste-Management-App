package com.example.wastemanagementapp.eco_collect.data

import android.util.Log
import com.example.wastemanagementapp.core.data.remote.NominatimApi
import com.example.wastemanagementapp.core.domain.RemoteError
import com.example.wastemanagementapp.core.util.Result
import com.example.wastemanagementapp.eco_collect.domain.models.EventLocation
import com.example.wastemanagementapp.eco_collect.domain.models.SearchResponse
import com.example.wastemanagementapp.eco_collect.domain.repository.EcoCollectRepository
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okio.IOException
import retrofit2.HttpException
import java.net.SocketTimeoutException
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class EcoCollectRepoImpl @Inject constructor(
    private val api: NominatimApi
) : EcoCollectRepository {
    override suspend fun searchLocations(query: String): Flow<Result<List<SearchResponse>, RemoteError>> = flow {
        try {
            val response = api.searchLocations(query = query)

            if (response.isSuccessful) {
                response.body()?.let {
                    Log.d("place", "searchLocations: search is successful $it")
                    emit(Result.Success(it))
                } ?: emit(Result.Success(emptyList()))
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("place", "getAddress: getting address not successful $errorBody")
                emit(Result.Success(emptyList()))
            }
        } catch (e: HttpException) {
            e.printStackTrace()
            emit(Result.Failure(RemoteError.HTTP_EXCEPTION))
        } catch (e: IOException) {
            e.printStackTrace()
            emit(Result.Failure(RemoteError.IO_EXCEPTION))
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
            emit(Result.Failure(RemoteError.JSON_DATA_EXCEPTION))
        } catch (e: SocketTimeoutException) {
            e.printStackTrace()
            emit(Result.Failure(RemoteError.TIMEOUT))
        } catch (e: Exception) {
            coroutineContext.ensureActive()
            e.printStackTrace()
            emit(Result.Failure(RemoteError.UNKNOWN))
        }
    }

    override suspend fun getAddress(lat: Double, lon: Double): Result<EventLocation, RemoteError> {
        return try {
            val response = api.getAddress(lat = lat, lon = lon)

            if (response.isSuccessful) {
                Log.d("place", "getAddress: getting address successful ${response.body()}")
                Result.Success(response.body() ?: EventLocation())
            } else {
                val errorBody = response.errorBody()?.string()
                Log.e("place", "getAddress: getting address not successful $errorBody")
                Result.Success(EventLocation())
            }
        } catch (e: HttpException) {
            e.printStackTrace()
            Result.Failure(RemoteError.HTTP_EXCEPTION)
        } catch (e: IOException) {
            e.printStackTrace()
            Result.Failure(RemoteError.IO_EXCEPTION)
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
            Result.Failure(RemoteError.JSON_DATA_EXCEPTION)
        } catch (e: SocketTimeoutException) {
            e.printStackTrace()
            Result.Failure(RemoteError.TIMEOUT)
        } catch (e: Exception) {
            coroutineContext.ensureActive()
            e.printStackTrace()
            Result.Failure(RemoteError.UNKNOWN)
        }
    }
}