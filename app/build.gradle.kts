plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
    id("io.gitlab.arturbosch.detekt")
    id("org.jlleitschuh.gradle.ktlint")
    id("jacoco")
}

android {
    namespace = "com.alpha.books_explorer"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.alpha.books_explorer"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            // Enables instrumentation coverage for connected tests
            enableUnitTestCoverage = true
        }

        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    testOptions {
        unitTests.isIncludeAndroidResources = true
    }
}

jacoco {
    toolVersion = "0.8.10" // use latest
}

tasks.withType<Test>().configureEach {
    extensions.configure<JacocoTaskExtension> {
        isIncludeNoLocationClasses = true
        excludes = listOf("jdk.internal.*")
    }
}


tasks.register<JacocoReport>("jacocoTestReport") {
    dependsOn("testDebugUnitTest") // make sure tests run first

    reports {
        xml.required.set(true)
        html.required.set(true)
        csv.required.set(true)
    }

    val debugTree = fileTree("${project.buildDir}/tmp/kotlin-classes/debug") {
        include("**/com/alpha/**")
        exclude(
            "**/R.class",
            "**/R$*.class",
            "**/BuildConfig.*",
            "**/Manifest*.*",
            "**/*Test*.*",
            "android/**/*.*",
            "**/com/alpha/books_explorer/presentation/ui/CommonUiKt*.*",
            "**/com/alpha/books_explorer/presentation/ui/theme/**/*.*",

            "**/com/alpha/books_explorer/di/**/*.*",
            "**/com/alpha/books_explorer/data/**/*.*",
            "**/com/alpha/books_explorer/presentation/navigation/**/*.*",

            "**/com/alpha/books_explorer/presentation/ui/search/SearchScreen*.*",
            "**/presentation/ui/search/ComposableSingletons\$SearchScreen*.*",
            "**/com/alpha/books_explorer/presentation/ui/profile/ProfileScreen*.*",
            "**/presentation/ui/profile/ComposableSingletons\$ProfileScreen*.*",
            "**/com/alpha/books_explorer/presentation/ui/home/HomeScreen*.*",
            "**/presentation/ui/home/ComposableSingletons\$HomeScreen*.*",
            "**/com/alpha/books_explorer/presentation/ui/wishList/WishlistScreen*.*",
            "**/presentation/ui/wishList/ComposableSingletons\$WishlistScreen*.*",
            "**/com/alpha/books_explorer/presentation/ui/details/BookDetailScreen*.*",
            "**/presentation/ui/details/ComposableSingletons\$BookDetailScreen*.*",

            "**/BooksExplorerApplication*.*",
            "**/MainActivity*.*",
            "**/ComposableSingletons\$MainActivity*.*",
        )
    }

    sourceDirectories.setFrom(files("src/main/java", "src/main/kotlin"))
    classDirectories.setFrom(files(debugTree))
    executionData.setFrom(fileTree(project.buildDir) {
        include(
            "outputs/unit_test_code_coverage/debugUnitTest/testDebugUnitTest.exec"
        )
    })
}

detekt {
    toolVersion = "1.23.6"
    config.setFrom("$rootDir/config/detekt/detekt.yml") // optional custom rules
    buildUponDefaultConfig = true
    reports {
        html.required.set(true)
        txt.required.set(true)
        xml.required.set(true)
    }
}

ktlint {
    version.set("1.2.1")
    android.set(true)
    outputToConsole.set(true)
    ignoreFailures.set(false)
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.hilt.navigation.compose)

    // Retrofit + OkHttp
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)

    // Room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // Paging 3
    implementation(libs.paging.runtime)
    implementation(libs.paging.compose)

    // Coroutines
    implementation(libs.coroutines.core)
    implementation(libs.coroutines.android)
    testImplementation(libs.coroutines.test)

    // Lifecycle
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.livedata.ktx)

    // Navigation
    implementation(libs.navigation.compose)

    // Coil
    implementation(libs.coil.compose)

    // Testing
    testImplementation(libs.mockito.core)
    testImplementation(libs.mockito.kotlin)
    testImplementation(libs.turbine)

    detektPlugins(libs.detekt.formatting)

    testImplementation(libs.junit)
    testImplementation(libs.truth)

    // Paging testing helpers
    testImplementation(libs.androidx.paging.common)

    // Mocking
    testImplementation(libs.mockk)

    // Retrofit + MockWebServer
    testImplementation(libs.mockwebserver)
    testImplementation(libs.retrofit)
    testImplementation(libs.converter.gson)

    // LiveData/arch rules if you still use some LiveData
    testImplementation(libs.androidx.core.testing)

    // Room testing helpers
    androidTestImplementation(libs.androidx.room.testing)
    androidTestImplementation(libs.core.ktx)

    // Compose UI tests (if you’re testing composables)
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.test.manifest)
    testImplementation(kotlin("test"))
}
