package com.example.wastemanagementapp.complaint.di

import com.example.wastemanagementapp.complaint.data.ComplaintRepoImpl
import com.example.wastemanagementapp.complaint.domain.ComplaintRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ComplaintModel {

    @Provides
    @Singleton
    fun provideComplaintRepository() : ComplaintRepository {
        return ComplaintRepoImpl()
    }
}
