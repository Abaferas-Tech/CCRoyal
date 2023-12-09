// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {

    val navVersion by extra ("2.6.0")

    repositories{
        google()
        mavenCentral()
        maven{
            url  = uri("https://oss.sonatype.org/content/repositories/snapshots/")
        }
        maven{
            url = uri("https://oss.jfrog.org/artifactory/oss-snapshot-local")
        }
    }
    dependencies{
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$navVersion")
        classpath ("com.google.dagger:hilt-android-gradle-plugin:2.48")
        classpath ("com.android.tools.build:gradle:8.1.4")
    }
}

plugins {
    id("com.android.application") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
    id("com.google.gms.google-services") version "4.4.0" apply false
    id("org.jetbrains.kotlin.jvm") version "1.8.10" apply false
    id("com.google.dagger.hilt.android") version "2.44" apply false
    id("com.google.devtools.ksp") version "1.8.10-1.0.9" apply false
    kotlin("kapt") version "1.9.0"
}