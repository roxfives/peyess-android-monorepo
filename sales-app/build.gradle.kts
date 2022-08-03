buildscript {
    extra.apply {
        set("compose_version", "1.1.1")
        set("compose_nav_version", "2.4.0-rc01")
        set("compose_compiler_version", "1.3.0-rc01")
        set("compose_accompanist_version", "0.23.1")

        set ("koin_version", "3.2.0")
        set ("hilt_version", "2.43.1")
        set ("mavericks_version", "2.7.0")
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
    }
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}
