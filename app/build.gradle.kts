import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.composeCompiler)
    // Kotlin serialization plugin for type safe routes and navigation arguments
    kotlin("plugin.serialization") version "2.0.21"
    // Kapt annotation processor
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.wastemanagementapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.wastemanagementapp"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        // Load the local.properties file
        val localProperties = Properties().apply {
            load(FileInputStream(rootProject.file("local.properties")))
        }

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        // Expose the API key to BuildConfig
        buildConfigField("String", "GOOGLE_SIGN_IN_WEB_API_KEY", "\"${localProperties["GOOGLE_SIGN_IN_WEB_API_KEY"]}\"")

        buildConfigField("String", "SUPABASE_API_KEY", "\"${localProperties["SUPABASE_API_KEY"]}\"")

        buildConfigField("String", "MAPS_API_KEY", "\"${localProperties["MAPS_API_KEY"]}\"")

        manifestPlaceholders["MAPS_API_KEY"] = localProperties.getProperty("MAPS_API_KEY", "")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.13"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.auth)
    implementation(libs.googleid)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(libs.kotlin.stdlib)

    // Retrofit for api call
    implementation (libs.retrofit)
    implementation (libs.converter.gson)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // JSON serialization library, for working with routes
    implementation(libs.kotlinx.serialization.json)

    // Coil image loading library
    implementation(libs.coil.compose)
    implementation(libs.coil.gif)
    implementation(libs.coil.network.okhttp)

    // Dagger and hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    implementation (libs.androidx.hilt.navigation.compose)

    // Viewmodel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Kotlin coroutines
    implementation(libs.kotlinx.coroutines.android)

    // Material three extended
    implementation ("androidx.compose.material3:material3:1.2.0")
    implementation ("androidx.compose.material3:material3-window-size-class:1.2.0")
    implementation ("androidx.compose.material:material-icons-extended:1.7.7")

    // Splash screen
    implementation(libs.androidx.core.splashscreen)

    // Supabase storage
    implementation(platform("io.github.jan-tennert.supabase:bom:3.1.1"))
    implementation ("io.github.jan-tennert.supabase:storage-kt:3.1.1")
    implementation("io.ktor:ktor-client-android:3.1.1")

    // Maps
    implementation(libs.maps.compose)

    // Places
    implementation (libs.places)

    implementation (libs.kotlinx.coroutines.core)

    implementation(libs.androidx.lifecycle.runtime.compose)
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}