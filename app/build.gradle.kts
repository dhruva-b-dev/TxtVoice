plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias (libs.plugins.hilt.android)
    alias (libs.plugins.ksp)
    alias(libs.plugins.jetbrains.kotlin.serialization)
    alias(libs.plugins.google.oss.licenses)
}

android {
    namespace = "com.dhruva.txtvoice"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.dhruva.txtvoice"
        minSdk = 24
        targetSdk = 37
        versionCode = 2
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            // These will be populated from local.properties or environment variables
            storeFile = file(project.findProperty("RELEASE_STORE_FILE") ?: "path/to/keystore.jks")
            storePassword = project.findProperty("RELEASE_STORE_PASSWORD")?.toString()
            keyAlias = project.findProperty("RELEASE_KEY_ALIAS")?.toString()
            keyPassword = project.findProperty("RELEASE_KEY_PASSWORD")?.toString()
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "src/main/keepRules/rules.keep"
            )
            signingConfig = signingConfigs.getByName("release")

            optimization {
                enable = false
            }

            ndk {
                debugSymbolLevel = "FULL"
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.datastore.preferences)
    //hilt/dagger
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.compose)
    ksp(libs.hilt.android.compiler)
    //navigation 3
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.androidx.material3.adaptive.navigation3)
    implementation(libs.kotlinx.serialization.core)
    //lottie animation
    implementation(libs.lottie.compose)
    implementation(libs.play.services.oss.licenses)
    implementation(libs.google.material)

    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(kotlin("reflect"))
}