plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("org.jetbrains.kotlin.kapt") // Add this
    id("com.google.dagger.hilt.android") // Hilt plugin if containing ViewModels
}

android {
    namespace = "com.creators.core"
    compileSdk = 35

    defaultConfig {
        minSdk = 25

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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.hilt.common)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.databinding.adapters)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation (libs.androidx.lifecycle.viewmodel.compose)
    implementation("androidx.compose.material3:material3:1.3.1")
    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    // Hilt Navigation
    implementation(libs.androidx.hilt.navigation.compose.v100)
    // Hilt Testing
    androidTestImplementation(libs.hilt.android.testing)
    kaptAndroidTest(libs.hilt.android.compiler.v248)
    //room
    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)
}
kapt {
    correctErrorTypes = true
}
