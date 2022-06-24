plugins {
    id("com.android.application") version "7.2.0" apply false
    id("com.android.library") version "7.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.6.21" apply false
    id("androidx.navigation.safeargs") version "2.4.2" apply false
    id("org.jetbrains.kotlin.jvm") version "1.6.10" apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}