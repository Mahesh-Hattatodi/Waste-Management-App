package com.example.wastemanagementapp.feedback.domain.repository

import com.example.wastemanagementapp.core.util.Result
import com.example.wastemanagementapp.feedback.domain.DataError
import com.example.wastemanagementapp.feedback.domain.models.UserFeedback

interface FeedbackRepository {
    suspend fun submitFeedback(userFeedback: UserFeedback) : Result<Unit, DataError>
}