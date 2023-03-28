plugins {
    id("android-module")
    id("com.android.application")
    id("kotlin-android")
}

//val keystorePropertiesFile = rootProject.file("keystore.properties")
//val keystoreProperties = Properties()
//keystoreProperties.load(FileInputStream(keystorePropertiesFile))

android {
//    signingConfigs {
//        getByName("release") {
//            keyAlias = keystoreProperties["keyAlias"] as String
//            keyPassword = keystoreProperties["keyPassword"] as String
//            storeFile = file(keystoreProperties["storeFile"] as String)
//            storePassword = keystoreProperties["storePassword"] as String
//        }
//    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isDebuggable = false

            signingConfig = signingConfigs.getByName("debug")

            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }

        getByName("debug") {
            applicationIdSuffix = ".debug"

//            signingConfig = signingConfigs.getByName("debug")

            isDebuggable =  true
            isMinifyEnabled =  false
        }
    }

    defaultConfig {
        javaCompileOptions {
            annotationProcessorOptions {
                arguments["room.schemaLocation"] = "$projectDir/schemas"
            }
        }
    }
}