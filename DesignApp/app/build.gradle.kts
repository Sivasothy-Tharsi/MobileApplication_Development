import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {


    namespace = "com.example.designapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.designapp"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

}

dependencies {
    implementation ("androidx.activity:activity-compose:1.3.1")
    implementation ("androidx.compose.foundation:foundation:1.0.5")
    implementation ("androidx.compose.material:material:1.0.5")
    implementation ("androidx.navigation:navigation-compose:2.4.0-alpha10")
//    implementation ("androidx.compose.runtime:runtime-livedata:2.4.0-alpha10")
    implementation("androidx.core:core-ktx:1.9.0")
    implementation ("androidx.compose.runtime:runtime-livedata:1.2.0")
//    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.4.0-alpha10")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.activity:activity-compose:1.8.1")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")

//    implementation("com.google.firebase:firebase-inappmessaging-ktx:20.4.0")
//    implementation("com.google.firebase:firebase-database:20.3.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-auth:22.3.0")
    implementation("com.google.firebase:firebase-database:20.3.0")
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("com.google.firebase:firebase-storage:20.3.0")
    implementation("com.google.firebase:firebase-firestore:24.10.0")
    implementation("androidx.camera:camera-lifecycle:1.3.1")
//    implementation("com.google.firebase:firebase-auth:22.3.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    //Splash Api
    implementation ("androidx.core:core-splashscreen:1.0.1")
// Dagger - Hilt
    // Dagger - Hilt
    implementation ("com.google.dagger:hilt-android:2.44")
    kapt ("com.google.dagger:hilt-android-compiler:2.44")
    kapt ("androidx.hilt:hilt-compiler:1.0.0")
    implementation ("androidx.hilt:hilt-navigation-compose:1.1.0-alpha01")
    implementation ("com.google.accompanist:accompanist-systemuicontroller:0.17.0")
    implementation ("androidx.activity:activity-compose:1.4.0")


    // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))

    // Add the dependency for the Realtime Database library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-database-ktx")
    implementation("io.github.sceneview:arsceneview:0.10.0")
  //  implementation ('com.google.firebase:firebase-database:20.3.0')



    implementation ("androidx.activity:activity-compose:1.3.0")
    implementation ("io.coil-kt:coil-compose:1.4.0")
//    implementation ("com.squareup.okhttp3:okhttp:4.9.0" )
    implementation ("com.squareup.okhttp3:okhttp:4.2.2")
//    implementation ("com.google.accompanist:accompanist-pager:0.18.0")



}