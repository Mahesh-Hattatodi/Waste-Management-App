package com.example.wastemanagementapp.complaint.di

import android.content.Context
import com.example.wastemanagementapp.complaint.data.ComplaintRepoImpl
import com.example.wastemanagementapp.complaint.domain.repository.ComplaintRepository
import com.example.wastemanagementapp.complaint.domain.use_case.GetPublicUrlFromImageUseCase
import com.example.wastemanagementapp.complaint.domain.use_case.SubmitComplaintUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.storage.Storage
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ComplaintModel {

    @Provides
    @Singleton
    fun provideComplaintRepository(
        firebaseFireStore: FirebaseFirestore,
        storage: Storage,
        firebaseAuth: FirebaseAuth,
        @ApplicationContext context: Context
    ) : ComplaintRepository {
        return ComplaintRepoImpl(
            firebaseFireStore = firebaseFireStore,
            storage = storage,
            context = context,
            firebaseAuth = firebaseAuth
        )
    }

    @Provides
    @Singleton
    fun provideSubmitComplaintUseCase(complaintRepository: ComplaintRepository) : SubmitComplaintUseCase {
        return SubmitComplaintUseCase(complaintRepository)
    }

    @Provides
    @Singleton
    fun provideGetPublicUrlFromImageUseCase(complaintRepository: ComplaintRepository) : GetPublicUrlFromImageUseCase {
        return GetPublicUrlFromImageUseCase(complaintRepository)
    }
}
