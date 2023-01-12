buildscript {
    extra.apply {
        set("compose_version", "1.2.1")
        set("compose_accompanist_version", "0.25.1")
        set("compose_nav_version", "2.4.0-rc01")
        set("compose_compiler_version", "1.3.2")
        set("material_dialogs", "0.8.1-rc")

        set("lottie_version", "5.2.0")

        set("koin_version", "3.2.0")
        set("hilt_version", "2.43.1")

        set("mavericks_version", "2.7.0")
        set("arrow_version", "1.1.3")

        set("firebase_bom", "31.1.1")
        set("coil_version", "2.2.0")
        set("data_store", "1.0.0")
        set("android_crypto", "1.0.0")
        set("room_version", "2.4.3")
        set("work_version", "2.7.1")
        set("camerax_version", "1.1.0-rc01")

        set("zoom_layout_version", "1.9.0")
    }

    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        val hiltVersion = rootProject.extra["hilt_version"]

        classpath("com.android.tools.build:gradle:7.3.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.20")
        classpath("com.google.dagger:hilt-android-gradle-plugin:$hiltVersion")
        classpath("com.google.gms:google-services:4.3.14")
        classpath("com.google.firebase:firebase-crashlytics-gradle:2.9.2")
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.7.20")
    }
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}