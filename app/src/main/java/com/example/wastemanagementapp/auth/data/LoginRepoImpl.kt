package com.example.wastemanagementapp.auth.data

import android.util.Log
import com.example.wastemanagementapp.R
import com.example.wastemanagementapp.auth.domain.LoginRepository
import com.example.wastemanagementapp.core.domain.model.UserProfile
import com.example.wastemanagementapp.core.util.UiText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await

class LoginRepoImpl(
    private val firebaseFireStore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth
) : LoginRepository {

    private val _authState = MutableStateFlow<FirebaseUser?>(null)
    override val authState: StateFlow<FirebaseUser?>
        get() = _authState

    init {
        firebaseAuth.addAuthStateListener { auth ->
            _authState.value = auth.currentUser
        }
    }

    override suspend fun saveUserProfile(userProfile: UserProfile) {
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

    override suspend fun emailVerifiedOrNot(email: String): Boolean? {
        Log.i("verify", "emailVerifiedOrNot: ${firebaseAuth.currentUser?.isEmailVerified}")
        return firebaseAuth.currentUser?.isEmailVerified
    }

    override suspend fun loginUserWithEmailAndPassword(email: String, password: String) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                Log.i("login", "loginUserWithEmailAndPassword: login successful")
            }
            .addOnFailureListener {
                Log.i("login", "loginUserWithEmailAndPassword: login unsuccessful $it")
            }
    }

    override suspend fun loginAndVerify(email: String, password: String): VerificationResult {
        return try {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val user = result.user ?: return VerificationResult(
                success = false, message = UiText.StringResource(
                    resId = R.string.user_data_is_null
                )
            )

            Log.i("verify", "loginAndVerify: email verification ${user.isEmailVerified}")

            if (user.isEmailVerified) {
                VerificationResult(
                    success = true,
                    message = UiText.StringResource(
                        resId = R.string.login_successful
                    )
                )
            } else {
                VerificationResult(
                    success = false, message = UiText.StringResource(
                        resId = R.string.email_is_not_verified
                    )
                )
            }
        } catch (e: Exception) {
            Log.e("login", "Login failed", e)
            VerificationResult(
                success = false, message = UiText.StringResource(
                    resId = R.string.login_unsuccessful
                )
            )
        }
    }
}