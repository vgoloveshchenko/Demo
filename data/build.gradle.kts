plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()

        val githubServerUrl: String by project
        buildConfigField("String", "GITHUB_SERVER_URL", githubServerUrl)
        val countriesServerUrl: String by project
        buildConfigField("String", "COUNTRIES_SERVER_URL", countriesServerUrl)

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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

    // Core
    implementation(libs.androidx.core)
    implementation(libs.androidx.appcompat)
    implementation(libs.google.material)

    // Room
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.runtime)

    // Network
    implementation(libs.bundles.retrofit)
    implementation(libs.okHttpInterceptor)

    // Koin
    implementation(libs.koin)

    // Google Location
    implementation(libs.google.location)

    // Test
    testImplementation(libs.test.junit)
    testImplementation(libs.test.androidx.core)
    testImplementation(libs.test.robolectric)
    testImplementation(libs.test.kotlin.coroutines)
    testImplementation(libs.test.mockk)
}