package com.example.wastemanagementapp.auth.data

import android.util.Log
import com.example.wastemanagementapp.auth.domain.SignUpRepository
import com.example.wastemanagementapp.core.domain.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import javax.inject.Inject

class SignUpRepoImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val firebaseFireStore: FirebaseFirestore
) : SignUpRepository {
    override suspend fun saveUserAndSendEmailVerification(
        email: String,
        password: String,
        userProfile: UserProfile
    ) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful)   {
                    firebaseAuth.currentUser?.sendEmailVerification()
                        ?.addOnSuccessListener {
                            Log.e("verify", "saveUserAndSendEmailVerification: verification email sent")
                            saveUserProfile(userProfile)
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