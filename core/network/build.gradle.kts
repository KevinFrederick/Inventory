import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.kevinfreyap.network"
    compileSdk {
        version = release(37) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val localProperties = Properties()
        val localPropertiesFile = rootProject.file("local.properties")
        if (localPropertiesFile.exists()){
            localProperties.load(FileInputStream(localPropertiesFile))
        }

        val localPropertyUrl = localProperties.getProperty("BASE_URL")
        val baseUrl = if (localPropertyUrl.isNullOrEmpty()) {
            "\"http://localhost:8080/\""
        } else {
            localPropertyUrl
        }

        buildConfigField("String", "BASE_URL", baseUrl)
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    buildFeatures {
        buildConfig = true
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_11)
    }
}

dependencies {
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    implementation(libs.ktor.client)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.resources)
    implementation(libs.ktor.client.websockets)
    implementation(libs.ktor.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.client.logging)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}