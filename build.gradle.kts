// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.devtools.ksp") version "1.9.0-1.0.13" apply false
}
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath (libs.kotlin.gradle.plugin)
        classpath(libs.dagger.hilt.android.gradle.plugin)
        classpath(libs.kotlin.serialization)
    }
}