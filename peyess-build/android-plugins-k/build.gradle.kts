plugins {
    `kotlin-dsl`
}

dependencies {
    api(project(":android-plugins"))
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.0")
    implementation("com.android.tools.build:gradle:7.2.1")
}