package com.example.wastemanagementapp.feedback.data

import android.util.Log
import com.example.wastemanagementapp.core.util.Result
import com.example.wastemanagementapp.feedback.domain.DataError
import com.example.wastemanagementapp.feedback.domain.models.UserFeedback
import com.example.wastemanagementapp.feedback.domain.repository.FeedbackRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import kotlin.coroutines.coroutineContext

class FeedbackRepoImpl @Inject constructor(
    private val firebaseFireStore: FirebaseFirestore,
    fireBaseAuth: FirebaseAuth
) : FeedbackRepository {

    private val userId = fireBaseAuth.currentUser?.uid

    override suspend fun submitFeedback(userFeedback: UserFeedback) : Result<Unit, DataError> {
        val submitFeedback = userId?.let { userId ->
            userFeedback.copy(
                uuid = userId,
                rating = userFeedback.rating,
                topic = userFeedback.topic,
                feedback = userFeedback.feedback
            )
        }

        try {
            submitFeedback?.let {
                firebaseFireStore.collection("feedback")
                    .add(it)
                    .await()
            }

            return Result.Success(Unit)
        } catch (e: Exception) {
            coroutineContext.ensureActive()

            Log.e("feedback", "submitFeedback: $e")
            return Result.Failure(DataError.FEEDBACK_SUBMISSION_FAILED)
        }
    }
}