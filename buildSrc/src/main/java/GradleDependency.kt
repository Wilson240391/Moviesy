private object GradlePluginVersion {
    const val KOTLIN = CoreVersion.KOTLIN
    const val ANDROID_GRADLE = "4.1.1"
    const val GOOGLE_SERVICE = "4.3.10"
    const val SAFE_ARGS = CoreVersion.JETPACK_NAVIGATION
    const val HILT = "2.33-beta"
    const val CRASHLYTICS = "2.4.1"
    const val MAVEN_GRADLE = "2.1"
}

object GradlePluginId {
    const val ANDROID_APPLICATION = "com.android.application"
    const val ANDROID_LIBRARY = "com.android.library"
    const val ANDROID_KTX = "android"
    const val ANDROID_EXTENSIONS_KTX = "android.extensions"
    const val KAPT = "kapt"
    const val PARCELIZE = "kotlin-parcelize"
    const val SAFE_ARGS = "androidx.navigation.safeargs.kotlin"
    const val DAGGER_HILT = "dagger.hilt.android.plugin"
    const val APACHE_HTTP = "org.apache.httpcomponents"
    const val MAVEN_PLUGIN = "com.github.dcendents.android-maven"
    const val GOOGLE_SERVICE = "com.google.gms.google-services"
    const val FIREBASE_CRASHLYTICS = "com.google.firebase.crashlytics"
}

object GradleDependency {
    const val GRADLE_BUILD_TOOLS = "com.android.tools.build:gradle:${GradlePluginVersion.ANDROID_GRADLE}"
    const val KOTLIN_PLUGIN = "org.jetbrains.kotlin:kotlin-gradle-plugin:${GradlePluginVersion.KOTLIN}"
    const val GOOGLE_SERVICE = "com.google.gms:google-services:${GradlePluginVersion.GOOGLE_SERVICE}"
    const val SAFE_ARGS = "androidx.navigation:navigation-safe-args-gradle-plugin:${GradlePluginVersion.SAFE_ARGS}"
    const val DAGGER_HILT = "com.google.dagger:hilt-android-gradle-plugin:${GradlePluginVersion.HILT}"
    const val CRASHLYTICS = "com.google.firebase:firebase-crashlytics-gradle:${GradlePluginVersion.CRASHLYTICS}"
    const val MAVEN_GRADLE = "com.github.dcendents:android-maven-gradle-plugin:${GradlePluginVersion.MAVEN_GRADLE}"
}