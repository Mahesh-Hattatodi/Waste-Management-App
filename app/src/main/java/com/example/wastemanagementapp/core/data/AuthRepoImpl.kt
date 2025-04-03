package com.example.wastemanagementapp.core.data

import com.example.wastemanagementapp.core.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class AuthRepoImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {
    override suspend fun getCurrentUserId(): String? {
        return firebaseAuth.currentUser?.uid
    }
}