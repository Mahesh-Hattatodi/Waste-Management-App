package com.example.wastemanagementapp.core.di

import android.util.Log
import com.example.wastemanagementapp.BuildConfig
import com.example.wastemanagementapp.core.data.AuthRepoImpl
import com.example.wastemanagementapp.core.data.UpdateUserRepoImpl
import com.example.wastemanagementapp.core.data.remote.NetworkConstants.BASE_URL
import com.example.wastemanagementapp.core.data.remote.NominatimApi
import com.example.wastemanagementapp.core.domain.repository.AuthRepository
import com.example.wastemanagementapp.core.domain.repository.UpdateUserRepository
import com.example.wastemanagementapp.core.domain.use_case.GetUserIdUseCase
import com.example.wastemanagementapp.core.domain.use_case.UpdateUserPointsUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.storage.Storage
import io.github.jan.supabase.storage.storage
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MainModule {

    @Provides
    @Singleton
    fun providesFirebaseFireStore() = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun providesFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideSupabaseClient() : SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = "https://jplwsyvopvfupsbumpfh.supabase.co",
            supabaseKey = BuildConfig.SUPABASE_API_KEY
        ) {
            install(Storage)
        }
    }

    @Provides
    @Singleton
    fun provideSupabaseStorage(client: SupabaseClient): Storage {
        return client.storage
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .header("User-Agent", "WasteManagementApp/1.0 maheshhattatodi@gmail.com") // Replace with real email
                    .build()

                Log.d("OkHttp", "Request Headers: ${request.headers}")
                chain.proceed(request)
            }
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // Use the custom OkHttpClient
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideNominatimApi(retrofit: Retrofit) : NominatimApi {
        return retrofit.create(NominatimApi::class.java)
    }

    @Provides
    @Singleton
    fun provideUpdateUserRepository(
        firebaseFireStore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth
    ) : UpdateUserRepository {
        return UpdateUserRepoImpl(
            firebaseFireStore = firebaseFireStore,
            auth = firebaseAuth
        )
    }

    @Provides
    @Singleton
    fun provideUpdateUserPointsUseCase(repository: UpdateUserRepository) : UpdateUserPointsUseCase {
        return UpdateUserPointsUseCase(repository)
    }

    @Provides
    @Singleton
    fun providesAuthRepository(firebaseAuth: FirebaseAuth) : AuthRepository {
        return AuthRepoImpl(firebaseAuth)
    }

    @Provides
    @Singleton
    fun providesGetUserIdUseCase(authRepository: AuthRepository) : GetUserIdUseCase {
        return GetUserIdUseCase(authRepository)
    }
}
