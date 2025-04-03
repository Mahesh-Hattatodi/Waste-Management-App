package com.example.wastemanagementapp.profile.di

import com.example.wastemanagementapp.core.domain.use_case.GetUserIdUseCase
import com.example.wastemanagementapp.profile.domain.use_case.GetAllRankedUsersUseCase
import com.example.wastemanagementapp.profile.domain.use_case.GetCurrentUserRankUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ProfileModule {

    @Provides
    @Singleton
    fun provideGetAllRankedUsersUseCase(): GetAllRankedUsersUseCase {
        return GetAllRankedUsersUseCase()
    }

    @Provides
    @Singleton
    fun provideGetCurrentUserRankUseCase(getUserIdUseCase: GetUserIdUseCase): GetCurrentUserRankUseCase {
        return GetCurrentUserRankUseCase(getUserIdUseCase)
    }
}
