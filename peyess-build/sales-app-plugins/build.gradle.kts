plugins {
    `kotlin-dsl`
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
    implementation("com.android.tools.build:gradle:7.2.1")
    implementation(project(":android-plugins-k"))
}