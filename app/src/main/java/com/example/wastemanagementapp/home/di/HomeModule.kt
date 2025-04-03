package com.example.wastemanagementapp.home.di

import com.example.wastemanagementapp.core.domain.repository.AuthRepository
import com.example.wastemanagementapp.core.util.Mapper
import com.example.wastemanagementapp.home.data.HomeRepositoryImpl
import com.example.wastemanagementapp.home.domain.models.UserInfo
import com.example.wastemanagementapp.home.domain.repository.HomeRepository
import com.example.wastemanagementapp.home.domain.use_cases.CalculateUserRankingsUseCase
import com.example.wastemanagementapp.home.domain.use_cases.GetUserInfoUseCase
import com.example.wastemanagementapp.home.domain.use_cases.GetUsersUseCase
import com.example.wastemanagementapp.home.presentation.mapper.UserInfoToUserInfoUiModelMapper
import com.example.wastemanagementapp.core.presentation.UserinfoUiModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HomeModule {

    @Provides
    @Singleton
    fun providesHomeRepository(
        firebaseFireStore: FirebaseFirestore
    ) : HomeRepository {
        return HomeRepositoryImpl(
            firebaseFireStore = firebaseFireStore
        )
    }

    @Provides
    @Singleton
    fun provideGetUserInfoUseCase(
        authRepository: AuthRepository,
        calculateUserRankingsUseCase: CalculateUserRankingsUseCase,
        getUsersUseCase: GetUsersUseCase
    ) : GetUserInfoUseCase {
        return GetUserInfoUseCase(
            authRepository = authRepository,
            getUsersUseCase = getUsersUseCase,
            calculateUserRankingsUseCase = calculateUserRankingsUseCase
        )
    }

    @Provides
    @Singleton
    fun provideGetUsersUseCase(repository: HomeRepository) : GetUsersUseCase {
        return GetUsersUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideCalculateUserRankingsUseCase() : CalculateUserRankingsUseCase {
        return CalculateUserRankingsUseCase()
    }

    @Provides
    @Singleton
    fun provideUserInfoToUserInfoUiModelMapper() : Mapper<UserInfo, UserinfoUiModel> {
        return UserInfoToUserInfoUiModelMapper()
    }
}