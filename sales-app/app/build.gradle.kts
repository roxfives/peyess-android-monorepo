plugins {
    id("sales-app")
    id("kotlin-kapt") // Hilt dependency (migrate to KSP as soon as it's supported)
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
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
    val kotlinVersion = rootProject.ext["coil_version"]
    val dataStoreVersion = rootProject.ext["data_store"]

    val implementation by configurations.implementation
    val testImplementation by configurations.testImplementation
    val androidTestImplementation by configurations.androidTestImplementation
    val debugImplementation by configurations.debugImplementation
    val kapt by configurations.kapt
    val kaptTest by configurations.kaptTest
    val kaptAndroidTest by configurations.kaptAndroidTest
//    val annotationProcessor by configurations.annotationProcessor
//    val testAnnotationProcessor by configurations.testAnnotationProcessor
//    val androidTestAnnotationProcessor by configurations.androidTestAnnotationProcessor

    implementation(project(":logging"))
    implementation(project(":authentication"))

    implementation("androidx.appcompat:appcompat:1.4.2")
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

    implementation("androidx.datastore:datastore-preferences:$dataStoreVersion")

    implementation("com.airbnb.android:lottie-compose:$lottieVersion")

    implementation("io.coil-kt:coil:$kotlinVersion")
    implementation("io.coil-kt:coil-compose:$kotlinVersion")



    // Compose UI tooling
    debugImplementation("androidx.compose.ui:ui-tooling:$composeVersion")

    // Mavericks
    implementation("com.airbnb.android:mavericks:$mavericksVersion")
    implementation("com.airbnb.android:mavericks-hilt:$mavericksVersion")
    implementation("com.airbnb.android:mavericks-compose:$mavericksVersion")

    // Hilt
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-compiler:$hiltVersion")

    // Hilt instrumentation tests
    androidTestImplementation("com.google.dagger:hilt-android-testing:$hiltVersion")
    kaptAndroidTest("com.google.dagger:hilt-compiler:$hiltVersion")

    // Hilt local unit tests
    testImplementation("com.google.dagger:hilt-android-testing:$hiltVersion")
    kaptTest("com.google.dagger:hilt-compiler:$hiltVersion")

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:$firebaseBOM"))
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-storage-ktx")
}