pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "MovieApp"
include(":app")
include(":data")
include(":domain")
include(":presentation")
include(":presentation:screens")
include(":presentation:ui_kit")
include(":presentation:screens:detail")
include(":presentation:screens:favorites")
include(":presentation:screens:filters")
include(":presentation:screens:list")
include(":presentation:screens:profile")
