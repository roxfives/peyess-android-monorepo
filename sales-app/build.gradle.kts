buildscript {
    extra.apply {
        set("compose_version", "1.1.1")
        set("compose_nav_version", "2.4.0-rc01")
        set("compose_compiler_version", "1.3.0-rc01")
        set("compose_accompanist_version", "0.23.1")
        set("lottie_version", "5.2.0")
        set("firebase_bom", "30.3.1")
        set("coil_version", "2.1.0")

        set("koin_version", "3.2.0")
        set("hilt_version", "2.43.1")
        set("mavericks_version", "2.7.0")

        set("data_store", "1.0.0")
        set("android_crypto", "1.0.0")
        set("room_version", "2.4.3")
        set("work_version", "2.7.1")

        set("material_dialogs", "0.7.1")
    }

    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        val hiltVersion = rootProject.extra["hilt_version"]

        classpath("com.android.tools.build:gradle:7.2.1")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.10")
        classpath("com.google.dagger:hilt-android-gradle-plugin:$hiltVersion")
        classpath("com.google.gms:google-services:4.3.13")
    }
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}
