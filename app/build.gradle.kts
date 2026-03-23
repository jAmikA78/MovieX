plugins {
    alias(libs.plugins.android.application)
<<<<<<< HEAD
    alias(libs.plugins.kotlin.android)
=======
>>>>>>> 2b8c97a (Initial commit)
    alias(libs.plugins.kotlin.compose)
}

android {
<<<<<<< HEAD
    namespace = "com.depi.moviex"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.depi.moviex"
=======
    namespace = "com.example.moviex"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.example.moviex"
>>>>>>> 2b8c97a (Initial commit)
        minSdk = 24
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
<<<<<<< HEAD
    kotlinOptions {
        jvmTarget = "11"
    }
=======
>>>>>>> 2b8c97a (Initial commit)
    buildFeatures {
        compose = true
    }
}

dependencies {
<<<<<<< HEAD

=======
>>>>>>> 2b8c97a (Initial commit)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
<<<<<<< HEAD
    testImplementation(libs.junit)
    implementation("androidx.navigation:navigation-compose:2.8.0")
=======
    implementation("androidx.navigation:navigation-compose:2.8.0")
    testImplementation(libs.junit)
>>>>>>> 2b8c97a (Initial commit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
<<<<<<< HEAD
}
=======
}
>>>>>>> 2b8c97a (Initial commit)
