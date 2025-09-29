import java.util.Properties
import org.gradle.api.JavaVersion

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

// Lee FIXER_API_KEY de: local.properties -> gradle.properties -> ENV
val fixerApiKey: String = run {
    val p = Properties().apply {
        val f = rootProject.file("local.properties")
        if (f.exists()) f.inputStream().use { load(it) }
    }
    val fromLocal = p.getProperty("FIXER_API_KEY")?.trim().orEmpty()
    val fromGradleProp = (project.findProperty("FIXER_API_KEY") as String?)?.trim().orEmpty()
    val fromEnv = System.getenv("FIXER_API_KEY")?.trim().orEmpty()
    when {
        fromLocal.isNotBlank() -> fromLocal
        fromGradleProp.isNotBlank() -> fromGradleProp
        else -> fromEnv
    }
}

// Diagnóstico inofensivo en build (no imprime la key)
println("FIXER_API_KEY length at build time = ${fixerApiKey.length}")

android {
    namespace = "sv.udb.fixer"
    compileSdk = 34

    defaultConfig {
        applicationId = "sv.udb.fixer"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        // Exponer a BuildConfig
        buildConfigField("String", "FIXER_API_KEY", "\"$fixerApiKey\"")
        // Plan FREE requiere HTTP:
        buildConfigField("String", "FIXER_BASE_URL", "\"http://data.fixer.io/api/\"")
        //buildConfigField("String", "FIXER_API_KEY", "\"bb7df65da7967fcfa00d8f84d2598348\"")
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

    //Imagenes de banderas
    implementation("com.github.bumptech.glide:glide:4.16.0")
}
