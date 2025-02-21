import java.util.Properties

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    alias(libs.plugins.jetbrainsKotlinSerialization)
    alias(libs.plugins.googleKsp)
    alias(libs.plugins.hiltplugin)
    alias(libs.plugins.safeArgsPlugin)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "ru.maxi.kinomaxi.demo"
    compileSdk = 34

    defaultConfig {
        applicationId = "ru.maxi.kinomaxi.demo"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        val propertiesFile = rootProject.file("local.properties")
        val properties = Properties()
        properties.load(propertiesFile.inputStream())

        buildConfigField("String", "MOVIEDB_API_KEY", "\"${properties.getProperty("MOVIEDB_API_KEY")}\"")
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
        compose = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation (libs.compose.animation)
    implementation (libs.navigation.compose)
    implementation (libs.hilt.navigation.compose)
    implementation (libs.accompanist.navigation.animation)

    implementation(platform(libs.composeBom))
    androidTestImplementation(platform(libs.composeBom))

    implementation(libs.compose.material3)

    implementation(libs.ui.tooling.preview)
    debugImplementation(libs.ui.tooling)

    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.test.manifest)

    implementation(libs.activity.compose)
    implementation(libs.lifecycle.viewmodel.compose)

    implementation(libs.coil)

    implementation(libs.datastore)

    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)

    implementation(libs.room)
    ksp(libs.room.compiler)
    implementation(libs.room.ktx)

    implementation(libs.hilt)
    ksp(libs.hilt.compiler)

    implementation(libs.dagger)
    ksp(libs.dagger.compiler)

    implementation(libs.kotlinx.coroutines)

    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.coordinatorlayout)

    implementation(libs.material)

    implementation(libs.glide)
    ksp(libs.glide.ksp)

    implementation(platform(libs.retrofit.bom))
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.kotlinx)

    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp.interceptor.logging)

    implementation(libs.kotlinx.serialization)
    implementation(libs.kotlinx.datetime)
}
