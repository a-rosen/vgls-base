plugins {
    alias(libs.plugins.vgls.core.android)
}

dependencies {
    implementation(projects.core.common.ui.icons)
}

android {
    namespace = "com.vgleadsheets.ui.icons"
}
