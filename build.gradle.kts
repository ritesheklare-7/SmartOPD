// Top-level build file
plugins {
    alias(libs.plugins.android.application) apply false
    id("com.google.gms.google-services") version "4.4.4" apply false
}
buildscript {
    dependencies {
        classpath ("com.google.gms:google-services:4.4.4")
        classpath ("com.google.gms:google-services:4.3.15")

    }
}

