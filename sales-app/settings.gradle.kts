pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    includeBuild("../peyess-build")
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    repositories {
        google()
        mavenCentral()

        maven(url = "https://jitpack.io")
    }
}

include("app")
//monoInclude("logging")
//monoInclude("authentication")

fun monoInclude(name: String) {
    include(":$name")
    project(":$name").projectDir = File("../peyess-libs/$name")
}
