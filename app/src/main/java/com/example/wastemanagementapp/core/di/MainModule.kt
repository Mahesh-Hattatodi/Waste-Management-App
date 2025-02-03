package com.example.wastemanagementapp.core.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class MainModule {

    @Provides
    @Singleton
    fun providesFirebaseFireStore() = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun providesFirebaseAuth() = FirebaseAuth.getInstance()
}