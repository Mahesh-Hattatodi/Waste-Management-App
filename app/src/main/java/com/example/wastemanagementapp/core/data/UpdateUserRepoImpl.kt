package com.example.wastemanagementapp.core.data

import android.util.Log
import com.example.wastemanagementapp.core.domain.FirebaseFireStoreError
import com.example.wastemanagementapp.core.domain.repository.UpdateUserRepository
import com.example.wastemanagementapp.core.util.Result
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class UpdateUserRepoImpl @Inject constructor(
    private val firebaseFireStore: FirebaseFirestore,
    auth: FirebaseAuth
) : UpdateUserRepository {
    private val userId = auth.currentUser?.uid

    override suspend fun updateUserPoints(
        updateField: String,
        incrementBy: Int
    ) : Result<Unit, FirebaseFireStoreError> {
        return try {
            val querySnapshot = firebaseFireStore.collection("user")
                .whereEqualTo("uuid", userId)
                .limit(1)
                .get()
                .await()

            val document = querySnapshot.documents.firstOrNull()

            return if (document == null) {
                Result.Failure(FirebaseFireStoreError.NOT_FOUND)
            } else {
                val documentId = document.id

                firebaseFireStore.runTransaction { transaction ->
                    val docRef = firebaseFireStore.collection("user").document(documentId)
                    val snapshot = transaction.get(docRef)

                    val currentValue = snapshot.getLong(updateField)
                    val newValue = currentValue?.let {
                        currentValue + incrementBy.toLong()
                    }

                    transaction.update(docRef, updateField, newValue)
                }.await()

                Result.Success(Unit)
            }
        } catch (e: FirebaseFirestoreException) {
            when (e.code) {
                FirebaseFirestoreException.Code.CANCELLED -> {
                    Result.Failure(FirebaseFireStoreError.CANCELLED)
                }
                FirebaseFirestoreException.Code.NOT_FOUND -> {
                    Log.e("updateUserPoint", "updateUserPoints: update document not found ${e.message}")
                    Result.Failure(FirebaseFireStoreError.NOT_FOUND)
                }
                FirebaseFirestoreException.Code.PERMISSION_DENIED -> {
                    Log.e("updateUserPoint", "updateUserPoints: Permission denied ${e.message}")
                    Result.Failure(FirebaseFireStoreError.PERMISSION_DENIED)
                }
                FirebaseFirestoreException.Code.ABORTED -> {
                    Log.e("updateUserPoint", "updateUserPoints: transaction failed ${e.message}")
                    Result.Failure(FirebaseFireStoreError.ABORTED)
                }
                FirebaseFirestoreException.Code.UNAVAILABLE -> {
                    Log.e("updateUserPoint", "updateUserPoints: server unavailable ${e.message}")
                    Result.Failure(FirebaseFireStoreError.SERVER_UNAVAILABLE)
                }
                else -> {
                    Log.e("updateUserPoint", "updateUserPoints: Unknown error ${e.message}")
                    Result.Failure(FirebaseFireStoreError.UNKNOWN)
                }
            }
        } catch (e: Exception) {
            coroutineContext.ensureActive()
            Log.e("updateUserPoint", "updateUserPoints: Unknown error ${e.message}")
            Result.Failure(FirebaseFireStoreError.UNKNOWN)
        }
    }
}