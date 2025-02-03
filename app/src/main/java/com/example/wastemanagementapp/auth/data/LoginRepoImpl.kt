package com.example.wastemanagementapp.auth.data

import android.util.Log
import com.example.wastemanagementapp.auth.domain.LoginRepository
import com.example.wastemanagementapp.core.domain.UserProfile
import com.google.firebase.firestore.FirebaseFirestore

class LoginRepoImpl(
    private val firebaseFireStore: FirebaseFirestore
) : LoginRepository {
    override suspend fun saveGoogleUserProfile(userProfile: UserProfile) {
        val userRef = firebaseFireStore.collection("user").document(userProfile.uuid)

        userRef.set(userProfile)
            .addOnSuccessListener {
                Log.i(
                    "save",
                    "User data successfully written"
                )
            }
            .addOnFailureListener { exception ->
                Log.i(
                    "save",
                    "Error writing to document: $exception"
                )
            }
    }
}