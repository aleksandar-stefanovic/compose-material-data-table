plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    // Recommended by https://github.com/kotlin-hands-on/fibonacci
    alias(libs.plugins.vanniktechMavenPublish)
    alias(libs.plugins.dokka)
}

dokka {
    moduleName.set("Compose Material Data Table")
    dokkaPublications.html {
        outputDirectory.set(layout.projectDirectory.dir("docs"))
    }
}

dependencies {
    dokka(project(":lib"))
}