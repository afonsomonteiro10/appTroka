plugins {
    alias(libs.plugins.kotlin.android)
    id("com.android.application")
    id("com.google.gms.google-services")
    id("kotlin-kapt")
    id ("kotlin-android")
    id ("com.android.application")
    apply("com.google.gms.google-services")

}

android {
    namespace = "pt.ipsantarem.apptroka"
    compileSdk = 35

    defaultConfig {
        applicationId = "pt.ipsantarem.apptroka"
        minSdk = 28
        targetSdk = 35
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
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.support.annotations)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.legacy.support.v4)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation ("com.yuyakaido.android:card-stack-view:2.3.4")
    implementation("com.github.bumptech.glide:glide:4.15.1")
    kapt ("com.github.bumptech.glide:compiler:4.15.1")


//    implementation(libs.firebase.bom)
//    implementation(libs.flexbox)
}

private fun DependencyHandlerScope.kapt(string: kotlin.String) {}
