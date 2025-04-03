package com.example.wastemanagementapp.auth.di

import com.example.wastemanagementapp.auth.data.LoginRepoImpl
import com.example.wastemanagementapp.auth.data.SignUpRepoImpl
import com.example.wastemanagementapp.auth.domain.LoginRepository
import com.example.wastemanagementapp.auth.domain.SignUpRepository
import com.example.wastemanagementapp.auth.domain.use_cases.ValidateConfirmPassword
import com.example.wastemanagementapp.auth.domain.use_cases.ValidateEmail
import com.example.wastemanagementapp.auth.domain.use_cases.ValidatePassword
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideLoginRepository(
        firebaseFireStore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth
    ) : LoginRepository {
        return LoginRepoImpl(firebaseFireStore, firebaseAuth)
    }

    @Provides
    @Singleton
    fun provideSignUpRepository(
        firebaseAuth: FirebaseAuth,
        firebaseFireStore: FirebaseFirestore
    ) : SignUpRepository {
        return SignUpRepoImpl(firebaseAuth, firebaseFireStore)
    }

    @Provides
    @Singleton
    fun provideValidateEmailUseCase() : ValidateEmail {
        return ValidateEmail()
    }

    @Provides
    @Singleton
    fun provideValidatePasswordUseCase() : ValidatePassword {
        return ValidatePassword()
    }

    @Provides
    @Singleton
    fun provideValidateConfirmPasswordUseCase() : ValidateConfirmPassword {
        return ValidateConfirmPassword()
    }
}