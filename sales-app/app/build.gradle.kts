plugins {
    id("sales-app")
    id("kotlin-kapt") // Hilt dependency (migrate to KSP as soon as it's supported)
    id("com.google.dagger.hilt.android")
}

hilt {
    enableAggregatingTask = true
}


dependencies {
    val composeVersion = rootProject.extra["compose_version"]
    val navVersion = rootProject.extra["compose_nav_version"]
    val accompanistVersion = rootProject.extra["compose_accompanist_version"]
    val koinVersion = rootProject.extra["koin_version"]
    val hiltVersion = rootProject.extra["hilt_version"]
    val mavericksVersion = rootProject.extra["mavericks_version"]

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
}