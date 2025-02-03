package com.example.wastemanagementapp.auth.presentation

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import com.example.wastemanagementapp.BuildConfig
import com.example.wastemanagementapp.core.domain.UserProfile
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import java.security.MessageDigest
import java.util.UUID

class GoogleSignInClient(
    private val context: Context
) {
    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    suspend fun googleSignIn(): Result<AuthResult> {
        val credentialManager = CredentialManager.create(context)
        try {
            Log.i("googleSignIn", "Sign in started")
            // Initialize the credential manager


            // Generate a nonce (a random number generated once)
            val nonce = UUID.randomUUID().toString()
            val hashedNonce = hashNonce(nonce)

            Log.i("googleSignIn", "Nonce created")

            // Set up google ID option
            val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(BuildConfig.GOOGLE_SIGN_IN_WEB_API_KEY)
                .setNonce(hashedNonce)
                .build()

            Log.i("googleSignIn", "Google option is created")

            // Request credentials
            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            Log.i("googleSignIn", "Request is created")

            // Get the credential result
            Log.i("googleSignIn", "Attempting to get credential...")
            val result = credentialManager.getCredential(context, request)
            val credential = result.credential
            Log.i("googleSignIn", "Credential retrieved: ${result.credential}")

            Log.i("googleSignIn", "Credential is created")

            // Check if the received credential is a valid Google ID Token.
            if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential =
                    GoogleIdTokenCredential.createFrom(credential.data)
                val authCredential =
                    GoogleAuthProvider.getCredential(googleIdTokenCredential.idToken, null)

                // Sign in to Firebase with Google Credentials
                val authResult = firebaseAuth.signInWithCredential(authCredential).await()

                // Send result
                return Result.success(authResult)
            } else {
                Log.i("googleSignIn", "Received an invalid credential type")
                throw RuntimeException("Received an invalid credential type")
            }
        } catch (e: GetCredentialCancellationException) {
            Log.i("googleSignIn", "Sign in was cancelled")
            return Result.failure(Exception("Sign in was cancelled please try again"))
        } catch (e: Exception) {
            Log.i("googleSignIn", "Error during sign in ${e.message}")
            return Result.failure(e)
        }
    }

    // Helper function to hash the nonce
    private fun hashNonce(nonce: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(nonce.toByteArray())
        return digest.fold("") { str, it -> str + "%02x".format(it) }
    }
}