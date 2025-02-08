package com.example.wastemanagementapp.auth.domain

import com.example.wastemanagementapp.core.domain.UserProfile
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.StateFlow

interface LoginRepository {
    val authState : StateFlow<FirebaseUser?>
    suspend fun saveUserProfile(userProfile: UserProfile)

    suspend fun emailVerifiedOrNot(email: String) : Boolean?

    suspend fun loginUserWithEmailAndPassword(email: String, password: String)
}