import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.jetbrains.kotlin.konan.file.File
import org.jetbrains.kotlin.konan.properties.loadProperties

plugins {
    id(GradlePluginId.ANDROID_APPLICATION)
    kotlin(GradlePluginId.ANDROID_KTX)
    kotlin(GradlePluginId.ANDROID_EXTENSIONS_KTX)
    kotlin(GradlePluginId.KAPT)
    id(GradlePluginId.DAGGER_HILT)
    id(GradlePluginId.FIREBASE_CRASHLYTICS)
}

android {
    compileSdkVersion(AndroidConfig.COMPILE_SDK_VERSION)
    buildToolsVersion(AndroidConfig.BUILD_TOOLS_VERSION)

    buildFeatures.viewBinding = true

    defaultConfig {
        applicationId = AndroidConfig.ID
        vectorDrawables.useSupportLibrary = true
        minSdkVersion(AndroidConfig.MIN_SDK_VERSION)
        targetSdkVersion(AndroidConfig.TARGET_SDK_VERSION)
        versionCode = AndroidConfig.VERSION_CODE
        versionName = AndroidConfig.VERSION_NAME

        testInstrumentationRunner = AndroidConfig.TEST_INSTRUMENTATION_RUNNER

        kapt {
            arguments {
                arg("room.schemaLocation", "$projectDir/schemas")
            }
        }

        buildConfigField("String", "TMDB_API_KEY", "\"${gradleLocalProperties(rootDir).getProperty("tmdb_api_key")}\"")
        buildConfigField("String", "INTERSTITIAL_ID", "\"${gradleLocalProperties(rootDir).getProperty("interstitial_ad_id")}\"")
        buildConfigField("String", "REWARD_AD_ID", "\"${gradleLocalProperties(rootDir).getProperty("reward_ad_id")}\"")
        buildConfigField("String", "GOOGLE_CLIENT_ID", "\"${gradleLocalProperties(rootDir).getProperty("google_client_id_web")}\"")
    }

//    signingConfigs {
//        create(BuildType.RELEASE) {
//            val properties = File("${rootProject.projectDir}/key.properties").loadProperties()
//            storeFile = file(properties["signing.keyFile"] as String)
//            keyPassword = properties["signing.keyPassword"] as String
//            storePassword = properties["signing.storePassword"] as String
//            keyAlias = properties["signing.alias"] as String
//        }
//    }

    buildTypes {
//        getByName(BuildType.RELEASE) {
//            isMinifyEnabled = BuildTypeRelease.isMinifyEnabled
//            signingConfig = signingConfigs.getByName(BuildType.RELEASE)
//            proguardFiles(
//                getDefaultProguardFile("proguard-android-optimize.txt"),
//                "proguard-rules.pro"
//            )
//        }
        getByName(BuildType.DEBUG) {
            applicationIdSuffix = ".debug"
            isDebuggable = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
        freeCompilerArgs = listOf(
            "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-Xopt-in=kotlinx.coroutines.FlowPreview"
        )
    }

    lintOptions {
        isCheckReleaseBuilds = false
        isAbortOnError = false
    }

    packagingOptions {
        exclude("META-INF/DEPENDENCIES")
        exclude("META-INF/LICENSE")
        exclude("META-INF/LICENSE.txt")
        exclude("META-INF/license.txt")
        exclude("META-INF/NOTICE")
        exclude("META-INF/NOTICE.txt")
        exclude("META-INF/notice.txt")
        exclude("META-INF/ASL2.0")
    }

}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    for (moduleId in ModuleDependency.getAllModules().filterNot { it == ModuleDependency.APP })
        implementation(project(moduleId))
    implementation(LibraryDependency.KOTLIN_STDLIB)
    implementation(LibraryDependency.APP_COMPAT)
    implementation(LibraryDependency.CORE_KTX)
    implementation(LibraryDependency.FRAGMENT_KTX)
    implementation(LibraryDependency.COROUTINES_CORE)
    implementation(LibraryDependency.COROUTINES_ANDROID)
    implementation(LibraryDependency.GOOGLE_OAUTH)
    implementation(LibraryDependency.GOOGLE_ADS)
    implementation(LibraryDependency.APP_STARTUP)

    implementation(platform(LibraryDependency.FIREBASE_BOM))
    implementation(LibraryDependency.FIREBASE_AUTH)
    implementation(LibraryDependency.FIREBASE_CRASHLYTICS)
    implementation(LibraryDependency.FIREBASE_ANALYTICS)

    implementation(LibraryDependency.MEDIA_ROUTER)
    implementation(LibraryDependency.CAST_FRAMEWORK)
    implementation(LibraryDependency.ANDROID_LOCAL_CAST_SAMPLE)

    api(LibraryDependency.MATERIAL)
    api(LibraryDependency.GIRAFFE_PLAYER2)

    implementation(LibraryDependency.GSON)
    implementation(LibraryDependency.LOTTIE)
    implementation(LibraryDependency.ANDROID_BROWSER)
    implementation(LibraryDependency.CONSTRAINT_LAYOUT)
    implementation(LibraryDependency.ANDROIDX_PREFERENCES)
    implementation(LibraryDependency.FB_SHIMMER)
    implementation(LibraryDependency.ROOM_RUNTIME)
    implementation(LibraryDependency.ROOM_KTX)
    implementation(LibraryDependency.LOCALBROADCAST_MANAGER)
    implementation(LibraryDependency.LIFECYCLE_VIEWMODEL)
    implementation(LibraryDependency.LIFECYCLE_LIVEDATA)
    implementation(LibraryDependency.LIFECYCLE_RUNTIME)
    implementation(LibraryDependency.LIFECYCLE_COMMON)
    implementation(LibraryDependency.PAGING)
    implementation(LibraryDependency.RETROFIT) {
        exclude("okhttp")
    }
    implementation(LibraryDependency.OKHTTP)
    implementation(LibraryDependency.OKHTTP_LOGGING_INTERCEPTOR)
    implementation(LibraryDependency.RETROFIT_GSON_CONVERTER)
    implementation(LibraryDependency.RETROFIT_COROUTINES_ADAPTER)
    implementation(LibraryDependency.SWIPE_REFRESH_LAYOUT)

    implementation(LibraryDependency.GLIDE)
    implementation(LibraryDependency.CAOC)

    implementation(LibraryDependency.RXANDROID)
    implementation(LibraryDependency.RXJAVA)
    implementation(LibraryDependency.RXBINDING)
    implementation(LibraryDependency.TOASTY)
    implementation(LibraryDependency.BLURRY)
    implementation(LibraryDependency.JSOUP)
    implementation(LibraryDependency.YOUTUBE_PLAYER)
    implementation(LibraryDependency.PHOTOVIEW)
    implementation(LibraryDependency.TORRENT_STREAM_ANDROID)
    implementation(LibraryDependency.CAFEBAR)
    implementation(LibraryDependency.DYNAMIC_ANIMATION)
    implementation(LibraryDependency.WORK_MANAGER)

    implementation(LibraryDependency.HILT_ANDROID)
    implementation(LibraryDependency.HILT_VIEWODEL)
    implementation(LibraryDependency.HILT_WORK_MANAGER)
    implementation(LibraryDependency.AUTOBINDINGS)
    implementation(LibraryDependency.IMAGELOADERVIEW)
    implementation(LibraryDependency.APP_UPDATER)

    implementation(LibraryDependency.NAVIGATOR)
    implementation(LibraryDependency.NAVIGATOR_EXTENSIONS)
    implementation(LibraryDependency.NAVIGATOR_BOTTOM_NAVIGATION)
    implementation(LibraryDependency.NAVIGATOR_TAB_NAVIGATION)

    implementation(LibraryDependency.DATASTORE_PREFERENCES)
    implementation(LibraryDependency.VIEWPAGER2)
    implementation(LibraryDependency.ROUND_CORNER_PROGRESSBAR)

    implementation(LibraryDependency.GOOGLE_HTTPCLIENT_API) {
        exclude(group = GradlePluginId.APACHE_HTTP)
    }
    implementation(LibraryDependency.GOOGLE_HTTPCLIENT_GSON)
    implementation(LibraryDependency.DRIVE_REST) {
        exclude(group = GradlePluginId.APACHE_HTTP)
    }
    implementation(LibraryDependency.GUAVA_CONFLICT)
    implementation(LibraryDependency.EPOXY_CORE)
    implementation(project(mapOf("path" to ":app:common")))
    implementation(project(mapOf("path" to ":app:sheets")))

    kapt(LibraryDependency.AUTOBINDINGS_COMPILER)
    kapt(LibraryDependency.GLIDE_COMPILER)
    kapt(LibraryDependency.HILT_COMPILER)
    kapt(LibraryDependency.HILT_VIEWODEL_COMPILER)
    kapt(LibraryDependency.ROOM_COMPILER_KAPT)
    kapt(LibraryDependency.EPOXY_COMPILER)

    //debugImplementation(TestLibraryDependency.ANDROID_DEBUG_DB)
    //debugImplementation(TestLibraryDependency.STETHO)
   // debugImplementation(TestLibraryDependency.LEAK_CANARY) // TODO: Some memory leaks need to fixed but are in won't fix mode

    annotationProcessor(LibraryDependency.GLIDE_COMPILER)
    testImplementation(TestLibraryDependency.JUNIT)
    androidTestImplementation(TestLibraryDependency.JUNIT_TEST_EXT)
    androidTestImplementation(TestLibraryDependency.ESPRESSO_CORE)
}

apply(from = "${project.rootDir}/gradle/changelog.gradle.kts")