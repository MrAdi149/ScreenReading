plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.firebase.crashlytics)

    id ("org.jetbrains.kotlin.android") version "1.9.23"
    id("com.google.gms.google-services") version "4.4.2"
}

android {
    namespace = "com.example.screenarrator"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.screenarrator"
        minSdk = 24
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
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.crashlytics)
    implementation(libs.androidx.browser)
    implementation(libs.play.services.measurement.api)
    implementation(libs.androidx.core.animation)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Firebase
    implementation ("com.google.firebase:firebase-bom:33.1.0")
//    implementation ("com.google.firebase:firebase-analytics-ktx")
//    implementation ("com.google.firebase:firebase-crashlytics-ktx")

    // Recommended: Add the Firebase SDK for Google Analytics.
    implementation ("com.google.firebase:firebase-analytics:22.0.1")

    // Add the Firebase Crashlytics SDK.
    implementation ("com.google.firebase:firebase-crashlytics:19.0.1")

    implementation ("com.squareup.retrofit2:retrofit:2.11.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.11.0")

    //Google Play
    implementation("com.google.android.play:review:2.0.1")
    implementation("com.google.android.play:review-ktx:2.0.1")

    //Gestures
    implementation("it.sephiroth.android.library.uigestures:uigesture-recognizer-kotlin:1.2.7")

    //Adapter delegates
    implementation ("com.hannesdorfmann:adapterdelegates4-kotlin-dsl:4.3.2")
    implementation ("com.hannesdorfmann:adapterdelegates4-kotlin-dsl-layoutcontainer:4.3.2")
    implementation ("com.hannesdorfmann:adapterdelegates4-kotlin-dsl-viewbinding:4.3.0")
    //calligraphy
    implementation("io.github.inflationx:calligraphy3:3.1.1")
    implementation("io.github.inflationx:viewpump:2.0.3")

    //Material
    implementation("com.google.android.material:material:1.12.0")

    implementation("androidx.core:core-ktx:1.13.1")

    implementation ("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation ("androidx.navigation:navigation-ui-ktx:2.7.7")

}
