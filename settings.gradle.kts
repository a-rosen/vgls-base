pluginManagement {
    includeBuild("build-logic")

    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven {
            url = uri("https://oss.jfrog.org/artifactory/oss-snapshot-local/")
        } // Gradle license plugin snapshot
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") } // For PhotoView
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

// plugins {
//     id("com.gradle.enterprise") version("3.15")
// }
//
// gradleEnterprise {
//     buildScan {
//         termsOfServiceUrl = "https://gradle.com/terms-of-service"
//         termsOfServiceAgree = "yes"
//     }
// }
//
// buildCache {
//     local {
//         removeUnusedEntriesAfterDays = 30
//     }
// }

rootProject.name = "VGLeadSheets"

include(
    ":app",

    ":core:android:animation",
    ":core:android:bitmaps",
    ":core:android:conversion",
    ":core:android:coroutines",
    ":core:android:database",
    ":core:android:firebase",
    ":core:android:images",
    ":core:android:list",
    ":core:android:logging",
    ":core:android:mvrx",
    ":core:android:nav",
    ":core:android:recyclerview",
    ":core:android:repository",
    ":core:android:resources",
    ":core:android:storage",
    ":core:android:tracking",
    ":core:android:ui:core",
    ":core:android:ui:components",
    ":core:android:ui:colors",
    ":core:android:ui:icons",
    ":core:android:ui:fonts",
    ":core:android:ui:strings",
    ":core:android:ui:themes",

    ":core:common:conversion",
    ":core:common:coroutines",
    ":core:common:database",
    ":core:common:debug",
    ":core:common:logging",
    ":core:common:model",
    ":core:common:network",
    ":core:common:perf",
    ":core:common:repository",
    ":core:common:tracking",

    ":core:fake:perf",
    ":core:fake:tracking",

    ":features:main",
    ":features:main:about",
    ":features:main:composer",
    ":features:main:composers",
    ":features:main:debug",
    ":features:main:favorites",
    ":features:main:games",
    ":features:main:game",
    ":features:main:hud",
    ":features:main:license",
    ":features:main:search",
    ":features:main:settings",
    ":features:main:sheet",
    ":features:main:songs",
    ":features:main:tag-key",
    ":features:main:tag-songs",
    ":features:main:tag-value",
    ":features:main:viewer"
)
