package com.example.wastemanagementapp.auth.data

import android.util.Log
import com.example.wastemanagementapp.auth.domain.SignUpRepository
import com.example.wastemanagementapp.core.domain.model.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class SignUpRepoImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFireStore: FirebaseFirestore
) : SignUpRepository {
    override suspend fun saveUserAndSendEmailVerification(
        email: String,
        password: String,
        displayName: String,
        ward: String
    ) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { authResult ->
                if (authResult.isSuccessful) {
                    firebaseAuth.currentUser?.sendEmailVerification()
                        ?.addOnSuccessListener {
                            Log.i(
                                "verify",
                                "saveUserAndSendEmailVerification: verification email sent"
                            )
                            val user = authResult.result.user
                            val profileUpdates = UserProfileChangeRequest.Builder()
                                .setDisplayName(displayName)
                                .build()

                            user?.updateProfile(profileUpdates)
                                ?.addOnSuccessListener {
                                    saveUserProfile(
                                        UserProfile(
                                            uuid = user.uid,
                                            displayName = displayName,
                                            email = email,
                                            ward = ward
                                        )
                                    )
                                }
                                ?.addOnFailureListener {
                                    Log.e(
                                        "verify",
                                        "saveUserAndSendEmailVerification: error saving user info $it"
                                    )
                                }
                        }
                        ?.addOnFailureListener {
                            Log.e(
                                "verify",
                                "saveUserAndSendEmailVerification: verification email not sent $it"
                            )
                        }
                }
            }
    }

    private fun saveUserProfile(userProfile: UserProfile) {
        val userRef = firebaseFireStore.collection("user")

        userRef.add(userProfile)
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