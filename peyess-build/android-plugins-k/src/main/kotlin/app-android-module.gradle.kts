plugins {
    id("android-module")
    id("com.android.application")
    id("kotlin-android")
}

android {
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

        getByName("debug") {
            isDebuggable = true
            applicationIdSuffix = ".debug"
        }
    }
}