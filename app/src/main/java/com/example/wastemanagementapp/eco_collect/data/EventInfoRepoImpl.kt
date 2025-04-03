package com.example.wastemanagementapp.eco_collect.data

import android.content.Context
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import com.example.wastemanagementapp.core.util.Result
import com.example.wastemanagementapp.eco_collect.domain.EventDataError
import com.example.wastemanagementapp.eco_collect.domain.models.EventInfoModel
import com.example.wastemanagementapp.eco_collect.domain.repository.EventInfoRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class EventInfoRepoImpl @Inject constructor(
    private val firebaseFireStore: FirebaseFirestore,
    private val storage: Storage,
    private val context: Context
) : EventInfoRepository {

    override suspend fun uploadImageToSupBase(
        uri: Uri,
        bucketName: String
    ): Result<String, EventDataError> {
        return withContext(Dispatchers.IO) {
            try {
                val bucket = storage.from(bucketName)

                val fileExtension = getFileExtension(uri) ?: "jpg"
                val fileName = "event/${System.currentTimeMillis()}.$fileExtension"

                bucket.upload(fileName, readImageFromUri(uri))
                val imageUrl = bucket.publicUrl(fileName)

                Result.Success(imageUrl)
            } catch (e: HttpException) {
                when (e.code()) {
                    400 -> {
                        Log.e("event", "uploadImageToSupBase: Bad request ${e.message()}")
                        Result.Failure(EventDataError.StorageError.BAD_REQUEST)
                    }
                    401 -> {
                        Log.e("event", "uploadImageToSupBase: Not authorized ${e.message()}")
                        Result.Failure(EventDataError.StorageError.UNAUTHORIZED)
                    }
                    403 -> {
                        Log.e("event", "uploadImageToSupBase: Insufficient permission ${e.message()}")
                        Result.Failure(EventDataError.StorageError.FORBIDDEN)
                    }
                    404 -> {
                        Log.e("event", "uploadImageToSupBase: File or bucket does not exist ${e.message()}")
                        Result.Failure(EventDataError.StorageError.NOT_FOUND)
                    }
                    409 -> {
                        Log.e("event", "uploadImageToSupBase: Trying to create the bucket or file that already exist ${e.message()}")
                        Result.Failure(EventDataError.StorageError.CONFLICT)
                    }
                    413 -> {
                        Result.Failure(EventDataError.StorageError.PAYLOAD_TOO_LARGE)
                    }
                    429 -> {
                        Result.Failure(EventDataError.StorageError.TOO_MANY_REQUEST)
                    }
                    500 -> {
                        Log.e("event", "uploadImageToSupBase: Something went wrong ${e.message()}")
                        Result.Failure(EventDataError.StorageError.INTERNAL_SERVER_ERROR)
                    }
                    else -> {
                        Log.e("event", "uploadImageToSupBase: Unknown http code ${e.message()}")
                        Result.Failure(EventDataError.UnknownError)
                    }
                }
            } catch (e: Exception) {
                coroutineContext.ensureActive()
                Log.i("event", "uploadImageToSupBase: ${e.message}")
                Result.Failure(EventDataError.UnknownError)
            }
        }
    }

    override suspend fun saveEvent(eventInfoModel: EventInfoModel): Result<Unit, EventDataError> {
        return try {
            firebaseFireStore.collection("events").add(eventInfoModel).await()
            Result.Success(Unit)
        } catch (e: FirebaseFirestoreException) {
            when (e.code) {
                FirebaseFirestoreException.Code.CANCELLED -> {
                    Log.e("event", "saveEvent: Request cancelled ${e.message}")
                    Result.Failure(EventDataError.FirebaseFireStoreError.CANCELLED)
                }
                FirebaseFirestoreException.Code.INTERNAL -> {
                    Log.e("event", "saveEvent: Internal server error ${e.message}")
                    Result.Failure(EventDataError.FirebaseFireStoreError.SERVER_UNAVAILABLE)
                }
                FirebaseFirestoreException.Code.INVALID_ARGUMENT -> {
                    Log.e("event", "saveEvent: Invalid data ${e.message}")
                    Result.Failure(EventDataError.FirebaseFireStoreError.ERROR_INVALID_DATA)
                }
                else -> {
                    Log.e("event", "saveEvent: Unknown ${e.message}")
                    Result.Failure(EventDataError.UnknownError)
                }
            }
        } catch (e: Exception) {
            coroutineContext.ensureActive()
            Log.e("event", "saveEvent: Unknown error")
            Result.Failure(EventDataError.UnknownError)
        }
    }

    private suspend fun readImageFromUri(uri: Uri) : ByteArray {
        return context.contentResolver.openInputStream(uri)?.use { inputStream ->
            ByteArrayOutputStream().use { outputStream ->
                var byte = inputStream.read()
                while (byte != -1) {
                    outputStream.write(byte)
                    byte = inputStream.read()
                    coroutineContext.ensureActive()
                }
                outputStream.toByteArray()
            }
        } ?: byteArrayOf()
    }

    private fun getFileExtension(uri: Uri) : String? {
        val contentResolver = context.contentResolver
        val mimeType = MimeTypeMap.getSingleton()
        return mimeType.getExtensionFromMimeType(contentResolver.getType(uri))
    }
}