// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        mavenCentral()
        google()
    }
    dependencies {
        classpath ("com.google.gms:google-services:4.4.0")
        classpath ("com.android.tools.build:gradle:4.0.2")
    }
}

plugins {
    id("com.android.application") version "8.1.2" apply false
    id("com.google.gms.google-services") version "4.4.0" apply true
}
