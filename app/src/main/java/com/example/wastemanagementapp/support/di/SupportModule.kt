package com.example.wastemanagementapp.support.di

import android.content.Context
import com.example.wastemanagementapp.support.data.CallHandlerImpl
import com.example.wastemanagementapp.support.data.EmailHandlerImpl
import com.example.wastemanagementapp.support.domain.CallHandler
import com.example.wastemanagementapp.support.domain.EmailHandler
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SupportModule {

    @Provides
    @Singleton
    fun providesCallHandler(
        @ApplicationContext context: Context
    ) : CallHandler {
        return CallHandlerImpl(context)
    }

    @Provides
    @Singleton
    fun providesEmailHandler(
        @ApplicationContext context: Context
    ) : EmailHandler {
        return EmailHandlerImpl(context)
    }
}