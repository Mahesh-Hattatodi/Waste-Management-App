package com.example.wastemanagementapp.feedback.domain.use_case

import com.example.wastemanagementapp.core.util.Result
import com.example.wastemanagementapp.feedback.domain.DataError
import com.example.wastemanagementapp.feedback.domain.models.UserFeedback
import com.example.wastemanagementapp.feedback.domain.repository.FeedbackRepository
import com.example.wastemanagementapp.feedback.presentation.model.SubmitFeedback
import javax.inject.Inject

class SubmitUserFeedbackUseCase @Inject constructor(
    private val repository: FeedbackRepository,
    private val getRatingFromEmojiUseCase: GetRatingFromEmojiUseCase
) {
    suspend operator fun invoke(submitFeedback: SubmitFeedback) : Result<Unit, DataError> {
        val rating = getRatingFromEmojiUseCase.invoke(emoji = submitFeedback.emoji)

        return repository.submitFeedback(
            userFeedback = UserFeedback(
                rating = rating,
                topic = submitFeedback.topic,
                feedback = submitFeedback.feedback
            )
        )
    }
}