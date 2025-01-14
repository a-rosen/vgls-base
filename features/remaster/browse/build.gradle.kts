plugins {
    alias(libs.plugins.vgls.core.jvm)
    alias(libs.plugins.vgls.di.jvm)
}

dependencies {
    api(projects.core.common.list)
    api(projects.core.common.nav)
    api(projects.core.common.repository)
    api(projects.core.common.ui.components)
}
