plugins {
    alias(libs.plugins.vgls.compose.android.module)
    alias(libs.plugins.vgls.core.android)
}

dependencies {
    implementation(libs.androidx.navigation.compose)

    implementation(projects.core.android.ui.components)

    implementation(projects.features.remaster.bottombar)
    implementation(projects.features.remaster.topbar)
}

android {
    namespace = "com.vgleadsheets.scaffold"
}
