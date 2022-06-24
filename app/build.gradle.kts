plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs")
}

android {
    compileSdk = libs.versions.compileSdk.get().toInt()

    val googleMapApiKey: String by project

    defaultConfig {
        applicationId = "com.example.demo"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        resourceConfigurations.addAll(listOf("en", "ru"))

        manifestPlaceholders["googleMapApiKey"] = googleMapApiKey

        testInstrumentationRunner = "com.example.demo.KoinTestRunner"
    }

    buildFeatures {
        viewBinding = true
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
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":data"))

    // Core
    implementation(libs.androidx.core)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.fragment)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.contraintLayout)
    implementation(libs.androidx.swipeRefreshLayout)
    implementation(libs.androidx.splashScreen)
    implementation(libs.google.material)

    // Coroutines
    implementation(libs.kotlin.coroutines)

    // Navigation
    implementation(libs.bundles.androidx.navigation)

    // Koin
    implementation(libs.koin)

    // Coil
    implementation(libs.coil)

    // Google Services
    implementation(libs.google.map)
    implementation(libs.google.mapUtils)

    testImplementation(libs.test.junit)
    testImplementation(libs.test.androidx.core)
    testImplementation(libs.test.robolectric)
    testImplementation(libs.test.kotlin.coroutines)
    testImplementation(libs.test.mockk)

    androidTestImplementation(libs.test.mockk.android)
    androidTestImplementation(libs.test.koin)
    androidTestImplementation(libs.test.koin.junit4)
    androidTestImplementation(libs.test.kaspresso)
    androidTestImplementation(libs.test.androidx.navigation)
    debugImplementation(libs.test.androidx.fragment)
}