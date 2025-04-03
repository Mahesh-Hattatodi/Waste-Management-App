package com.example.wastemanagementapp.feedback.di

import com.example.wastemanagementapp.feedback.data.FeedbackRepoImpl
import com.example.wastemanagementapp.feedback.domain.repository.FeedbackRepository
import com.example.wastemanagementapp.feedback.domain.use_case.GetRatingFromEmojiUseCase
import com.example.wastemanagementapp.feedback.domain.use_case.SubmitUserFeedbackUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FeedbackModule {

    @Provides
    @Singleton
    fun providesFeedbackRepository(
        firebaseStore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth
    ) : FeedbackRepository {
        return FeedbackRepoImpl(firebaseStore, firebaseAuth)
    }

    @Provides
    @Singleton
    fun providesSubmitFeedbackUseCase(
        repository: FeedbackRepository,
        getRatingFromEmojiUseCase: GetRatingFromEmojiUseCase
    ) : SubmitUserFeedbackUseCase {
        return SubmitUserFeedbackUseCase(repository, getRatingFromEmojiUseCase)
    }

    @Provides
    @Singleton
    fun providesGetRatingFromEmojiUseCase() = GetRatingFromEmojiUseCase()
}