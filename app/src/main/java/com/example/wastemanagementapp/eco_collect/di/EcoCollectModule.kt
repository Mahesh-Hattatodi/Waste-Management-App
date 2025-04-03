package com.example.wastemanagementapp.eco_collect.di

import android.content.Context
import com.example.wastemanagementapp.core.data.remote.NominatimApi
import com.example.wastemanagementapp.eco_collect.data.EcoCollectRepoImpl
import com.example.wastemanagementapp.eco_collect.data.EventInfoRepoImpl
import com.example.wastemanagementapp.eco_collect.domain.repository.EcoCollectRepository
import com.example.wastemanagementapp.eco_collect.domain.repository.EventInfoRepository
import com.example.wastemanagementapp.eco_collect.domain.use_case.GetEventAddressUseCase
import com.example.wastemanagementapp.eco_collect.domain.use_case.GetPublicUrlFromPermitImageUseCase
import com.example.wastemanagementapp.eco_collect.domain.use_case.GetSearchLocationsUseCase
import com.example.wastemanagementapp.eco_collect.domain.use_case.SubmitEventInfoUseCase
import com.example.wastemanagementapp.eco_collect.domain.use_case.ValidateEventInfoUseCase
import com.example.wastemanagementapp.eco_collect.domain.validation.EventInfoValidator
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
object EcoCollectModule {

    @Provides
    @Singleton
    fun provideEcoCollectRepository(api: NominatimApi) : EcoCollectRepository {
        return EcoCollectRepoImpl(api)
    }

    @Provides
    @Singleton
    fun provideGetSearchLocationsUseCase(repository: EcoCollectRepository) : GetSearchLocationsUseCase {
        return GetSearchLocationsUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetEventAddressUseCase(repository: EcoCollectRepository) : GetEventAddressUseCase {
        return GetEventAddressUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideEventInfoValidator() : EventInfoValidator {
        return EventInfoValidator
    }

    @Provides
    @Singleton
    fun provideValidateEventInfoUseCase(eventInfoValidator: EventInfoValidator) : ValidateEventInfoUseCase {
        return ValidateEventInfoUseCase(eventInfoValidator)
    }

    @Provides
    @Singleton
    fun provideEventInfoRepository(
        firebaseFireStore: FirebaseFirestore,
        storage: Storage,
        @ApplicationContext context: Context
    ) : EventInfoRepository {
        return EventInfoRepoImpl(
            firebaseFireStore = firebaseFireStore,
            storage = storage,
            context = context
        )
    }

    @Provides
    @Singleton
    fun provideGetPublicUrlFromPermitImageUseCase(
        repository: EventInfoRepository
    ) : GetPublicUrlFromPermitImageUseCase {
        return GetPublicUrlFromPermitImageUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideSubmitEventInfoUseCase(
        repository: EventInfoRepository
    ) : SubmitEventInfoUseCase {
        return SubmitEventInfoUseCase(repository)
    }
}
