package com.example.wastemanagementapp.home.data

import android.util.Log
import com.example.wastemanagementapp.core.domain.FirebaseFireStoreError
import com.example.wastemanagementapp.core.util.Result
import com.example.wastemanagementapp.home.domain.models.UserInfo
import com.example.wastemanagementapp.home.domain.repository.HomeRepository
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(
    private val firebaseFireStore: FirebaseFirestore
) : HomeRepository {

    override fun getUsers(): Flow<Result<List<UserInfo>, FirebaseFireStoreError>> = callbackFlow {
        Log.d("HomeRepositoryImpl", "Starting Firestore listener...")

        val listenerRegistration: ListenerRegistration = firebaseFireStore.collection("user")
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    Log.e("HomeRepositoryImpl", "Firestore Error: ${error.message}", error)
                    trySend(Result.Failure(FirebaseFireStoreError.UNKNOWN))
                    close(error)
                    return@addSnapshotListener
                }

                if (snapshot != null) {
                    Log.d("HomeRepositoryImpl", "Snapshot received: ${snapshot.documents.size} documents")

                    if (!snapshot.isEmpty) {
                        val userInfo = snapshot.documents.mapNotNull { it.toObject(UserInfo::class.java) }
                        Log.d("HomeRepositoryImpl", "Parsed ${userInfo.size} users from Firestore")
                        trySend(Result.Success(userInfo))
                    } else {
                        Log.w("HomeRepositoryImpl", "Firestore returned empty list")
                        trySend(Result.Failure(FirebaseFireStoreError.NOT_FOUND))
                    }
                } else {
                    Log.e("HomeRepositoryImpl", "Snapshot is null")
                    trySend(Result.Failure(FirebaseFireStoreError.UNKNOWN))
                }
            }

        awaitClose {
            Log.d("HomeRepositoryImpl", "Removing Firestore listener...")
            listenerRegistration.remove()
        }
    }
}
