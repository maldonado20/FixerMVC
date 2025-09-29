import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

import org.gradle.api.JavaVersion

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

// Lee FIXER_API_KEY con prioridad:
// 1) <raiz>/local.properties  2) gradle.properties  3) env var
val fixerApiKey: String = run {
    val fromLocal = gradleLocalProperties(rootDir, providers)
        .getProperty("FIXER_API_KEY")?.trim().orEmpty()

    val fromGradleProp = providers.gradleProperty("FIXER_API_KEY")
        .orNull?.trim().orEmpty()

    val fromEnv = providers.environmentVariable("FIXER_API_KEY")
        .orNull?.trim().orEmpty()

    when {
        fromLocal.isNotBlank() -> fromLocal
        fromGradleProp.isNotBlank() -> fromGradleProp
        else -> fromEnv
    }
}

android {
    namespace = "sv.udb.fixer"
    compileSdk = 34

    defaultConfig {
        applicationId = "sv.udb.fixer"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        buildConfigField("String", "FIXER_BASE_URL", "\"http://data.fixer.io/api/\"")
        buildConfigField("String", "FIXER_API_KEY", "\"$fixerApiKey\"")
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            buildConfigField("boolean", "LOG_HTTP", "true")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("boolean", "LOG_HTTP", "false")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }

    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

kotlin { jvmToolchain(17) }

dependencies {
    // AndroidX
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Retrofit 2 + Gson
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")

    // OkHttp
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
}
