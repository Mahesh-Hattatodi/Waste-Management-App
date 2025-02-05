package com.example.wastemanagementapp.auth.data

import android.util.Log
import com.example.wastemanagementapp.auth.domain.LoginRepository
import com.example.wastemanagementapp.core.domain.UserProfile
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

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

    override suspend fun emailVerifiedOrNot(email: String): Boolean? {
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
}