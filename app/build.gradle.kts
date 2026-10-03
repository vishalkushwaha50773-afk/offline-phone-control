plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.vishal.offlinephonecontrol"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.vishal.offlinephonecontrol"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }
}

dependencies {
}
