package com.example.wastemanagementapp.complaint.data

import android.content.Context
import android.net.Uri
import android.util.Log
import android.webkit.MimeTypeMap
import com.example.wastemanagementapp.complaint.domain.ComplaintDataError
import com.example.wastemanagementapp.complaint.domain.models.ComplaintInfo
import com.example.wastemanagementapp.complaint.domain.repository.ComplaintRepository
import com.example.wastemanagementapp.core.util.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.github.jan.supabase.storage.Storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class ComplaintRepoImpl @Inject constructor(
    private val firebaseFireStore: FirebaseFirestore,
    private val storage: Storage,
    private val context: Context,
    firebaseAuth: FirebaseAuth
) : ComplaintRepository {

    private val uid = firebaseAuth.currentUser?.uid

    override suspend fun uploadImageToSupBase(
        uri: Uri,
        bucketName: String
    ): Result<String, ComplaintDataError> {
        return withContext(Dispatchers.IO) {
            try {
                val bucket = storage.from(bucketName)

                val fileExtension = getFileExtension(uri) ?: "jpg"
                val fileName = "complaints/${System.currentTimeMillis()}.$fileExtension"

                bucket.upload(fileName, readImageFromUri(uri))
                val imageUrl = bucket.publicUrl(fileName)

                Result.Success(imageUrl)
            } catch (e: Exception) {
                coroutineContext.ensureActive()
                Log.i("complaint", "uploadImageToSupBase: ${e.message}")
                Result.Failure(ComplaintDataError.UnknownError)
            }
        }
    }

    override suspend fun saveComplaint(complaintInfo: ComplaintInfo): Result<Unit, ComplaintDataError> {
        val submitComplaint = uid?.let {
            complaintInfo.copy(
                uid = it,
                name = complaintInfo.name,
                address = complaintInfo.address,
                category = complaintInfo.category,
                complaintDetails = complaintInfo.complaintDetails,
                image = complaintInfo.image
            )
        }

        return if (submitComplaint != null) {
            firebaseFireStore.collection("complaint").add(submitComplaint).await()
            Result.Success(Unit)
        } else {
            Result.Failure(ComplaintDataError.FirebaseFireStoreError.ERROR_INVALID_DATA)
        }
    }


    private fun getFileExtension(uri: Uri) : String? {
        val contentResolver = context.contentResolver // Used for getting the mime type of the file e.g. image/png
        val mimeType = MimeTypeMap.getSingleton() //Used for mapping mime type to file extensions e.g. image/png -> png
        return mimeType.getExtensionFromMimeType(contentResolver.getType(uri))

        /*
        contentResolver.getType(uri) : Gets the mime type from the uri
        getExtensionFromMimeType() : This function maps the mime type to file extension
         */
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
}
