package com.example.wastemanagementapp.auth.di

import com.example.wastemanagementapp.auth.data.LoginRepoImpl
import com.example.wastemanagementapp.auth.domain.LoginRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AuthModule {

    @Provides
    @Singleton
    fun provideLoginRepository(
        firebaseFireStore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth
    ) : LoginRepository {
        return LoginRepoImpl(firebaseFireStore, firebaseAuth)
    }
}