plugins {
    id("library-android-module")
}

dependencies {
    val composeVersion = rootProject.extra["compose_version"]

    val implementation by configurations
    val testImplementation by configurations

    implementation("androidx.core:core-ktx:1.8.0")
    implementation("androidx.compose.runtime:runtime:$composeVersion")

    testImplementation("junit:junit:latest.release")
}