plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.kim.gunwoo.tacotaco"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.kim.gunwoo.tacotaco"
        minSdk = 34
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        viewBinding = true
    }
}

dependencies {

    implementation("androidx.work:work-runtime-ktx:2.8.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")


    implementation(libs.androidx.room.runtime.v250)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.material3.android)
    implementation(libs.identity.android.legacy)

    implementation(libs.logging.interceptor)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    annotationProcessor(libs.androidx.room.compiler)
    implementation(libs.retrofit)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}