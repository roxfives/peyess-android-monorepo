plugins {
    id("sales-app")
    id("kotlin-kapt") // Hilt dependency (migrate to KSP as soon as it's supported)
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
    id("kotlinx-serialization")

    id("org.jetbrains.kotlin.plugin.serialization")

    id("com.google.firebase.crashlytics")
}

hilt {
    enableAggregatingTask = true
}

dependencies {

    val composeVersion = rootProject.extra["compose_version"]
    val navVersion = rootProject.extra["compose_nav_version"]
    val accompanistVersion = rootProject.extra["compose_accompanist_version"]
    val hiltVersion = rootProject.extra["hilt_version"]
    val mavericksVersion = rootProject.extra["mavericks_version"]
    val lottieVersion = rootProject.extra["lottie_version"]
    val firebaseBOM = rootProject.extra["firebase_bom"]
    val coilVersion = rootProject.ext["coil_version"]
    val dataStoreVersion = rootProject.ext["data_store"]
    val androidCrypto = rootProject.ext["android_crypto"]
    val roomVersion = rootProject.ext["room_version"]
    val workVersion = rootProject.ext["work_version"]
    val cameraxVersion = rootProject.ext["camerax_version"]
    val zoomLayoutVersion = rootProject.ext["zoom_layout_version"]

    val materialDialogs = rootProject.ext["material_dialogs"]

    val implementation by configurations.implementation
    val testImplementation by configurations.testImplementation
    val androidTestImplementation by configurations.androidTestImplementation
    val debugImplementation by configurations.debugImplementation
    val kapt by configurations.kapt
    val kaptTest by configurations.kaptTest
    val kaptAndroidTest by configurations.kaptAndroidTest
    val annotationProcessor by configurations.annotationProcessor
//    val testAnnotationProcessor by configurations.testAnnotationProcessor
//    val androidTestAnnotationProcessor by configurations.androidTestAnnotationProcessor

    implementation(project(":logging"))
    implementation(project(":authentication"))

    implementation("com.google.android.material:material:1.6.1")
    implementation("androidx.paging:paging-common-ktx:3.1.1")

    implementation("androidx.appcompat:appcompat:1.5.0")
    implementation("androidx.core:core-ktx:1.8.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")

    implementation("com.jakewharton.timber:timber:5.0.1")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.6.4")

    implementation("androidx.activity:activity-compose:1.5.1")
    implementation("androidx.compose.runtime:runtime:$composeVersion")
    implementation("androidx.compose.ui:ui:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling:$composeVersion")
    implementation("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    implementation("androidx.compose.material:material:$composeVersion")
    implementation("androidx.compose.material:material-icons-extended:$composeVersion")
    implementation("androidx.navigation:navigation-compose:$navVersion")
    implementation("com.google.accompanist:accompanist-navigation-animation:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-systemuicontroller:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-permissions:$accompanistVersion")
    implementation("com.google.accompanist:accompanist-placeholder-material:$accompanistVersion")

    // Paging for compose
    implementation("androidx.paging:paging-compose:1.0.0-alpha16")

    implementation("androidx.security:security-crypto:$androidCrypto")

    implementation("androidx.datastore:datastore-preferences:$dataStoreVersion")

    implementation("androidx.room:room-runtime:$roomVersion")
    annotationProcessor("androidx.room:room-compiler:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    implementation("androidx.room:room-paging:$roomVersion")
    // To use Kotlin Symbol Processing (KSP)
    //    ksp("androidx.room:room-compiler:$room_version")

    implementation("com.airbnb.android:lottie-compose:$lottieVersion")

    implementation("io.coil-kt:coil:$coilVersion")
    implementation("io.coil-kt:coil-compose:$coilVersion")
    implementation("io.coil-kt:coil-svg:$coilVersion")

    // Compose UI tooling
    debugImplementation("androidx.compose.ui:ui-tooling:$composeVersion")

    // Mavericks
    implementation("com.airbnb.android:mavericks:$mavericksVersion")
    implementation("com.airbnb.android:mavericks-hilt:$mavericksVersion")
    implementation("com.airbnb.android:mavericks-compose:$mavericksVersion")

    // Work Manager
    implementation("androidx.work:work-runtime-ktx:$workVersion")
    androidTestImplementation("androidx.work:work-testing:$workVersion")

    // Hilt
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-compiler:$hiltVersion")
    implementation("androidx.hilt:hilt-work:1.0.0")
    kapt("androidx.hilt:hilt-compiler:1.0.0")
    // Hilt instrumentation tests
    androidTestImplementation("com.google.dagger:hilt-android-testing:$hiltVersion")
    kaptAndroidTest("com.google.dagger:hilt-compiler:$hiltVersion")
    // Hilt local unit tests
    testImplementation("com.google.dagger:hilt-android-testing:$hiltVersion")
    kaptTest("com.google.dagger:hilt-compiler:$hiltVersion")

    // CameraX
    implementation("androidx.camera:camera-core:${cameraxVersion}")
    implementation("androidx.camera:camera-camera2:${cameraxVersion}")
    // If you want to additionally use the CameraX Lifecycle library
    implementation("androidx.camera:camera-lifecycle:${cameraxVersion}")
    // If you want to additionally use the CameraX View class
    implementation("androidx.camera:camera-view:${cameraxVersion}")
    // If you want to additionally use the CameraX Extensions library
    implementation("androidx.camera:camera-extensions:${cameraxVersion}")

    // ML Kit
    implementation("com.google.android.gms:play-services-mlkit-face-detection:16.2.0")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:$firebaseBOM"))
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-storage-ktx")
    implementation ("com.google.firebase:firebase-crashlytics-ktx")
    implementation ("com.google.firebase:firebase-analytics-ktx")

    // Ktor
    implementation("io.ktor:ktor-client-android:1.5.0")
    implementation("io.ktor:ktor-client-serialization:1.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")
    implementation("io.ktor:ktor-client-logging-jvm:1.5.0")

    // Misc libs ----------------------------------------------------------------------------------

    // Zoom layout
    implementation("com.otaliastudios:zoomlayout:$zoomLayoutVersion")

    // Decent dialogs
    implementation("io.github.vanpra.compose-material-dialogs:datetime:$materialDialogs")

    // Chard library (thanks Google)
    // TODO: add version to project gradle
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // Nano-id
    implementation("com.aventrix.jnanoid:jnanoid:2.0.0")
}