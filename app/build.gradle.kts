/*
 * MIT License
 *
 * Copyright (c) 2019 Nicholas Doglio
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("androidx.navigation.safeargs.kotlin")
    id("io.gitlab.arturbosch.detekt")
}

apply(from = "$rootDir/ktlint.gradle.kts")

androidExtensions {
    isExperimental = true
}

kapt {
    correctErrorTypes = true
    javacOptions {
        option("-source", "8")
        option("-target", "8")
    }
}

android {
    compileSdkVersion(Config.compileSdk)
    defaultConfig {
        applicationId = "com.nicholasdoglio.notes"
        minSdkVersion(Config.minSdk)
        targetSdkVersion(Config.targetSdk)
        versionCode = Config.versionCode
        versionName = Config.versionName
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        javaCompileOptions {
            annotationProcessorOptions {
                arguments = mapOf("room.incremental" to "true")
            }
        }
    }

    buildTypes {
        named("release") {
            isMinifyEnabled = false
            setProguardFiles(listOf(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro"))
        }
    }

    sourceSets {
        named("main") {
            java.setSrcDirs(arrayListOf("src/main/kotlin"))
        }
        named("test") {
            java.setSrcDirs(arrayListOf("src/test/kotlin"))
        }
        named("androidTest") {
            java.setSrcDirs(arrayListOf("src/androidTest/kotlin"))
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
    implementation(Libs.kotlin_stdlib_jdk8)

    implementation(Libs.fragment_ktx)
    implementation(Libs.appcompat)
    implementation(Libs.recyclerview)
    implementation(Libs.material)
    implementation(Libs.constraintlayout)
    implementation(Libs.navigation_fragment_ktx)
    implementation(Libs.navigation_ui_ktx)

    implementation(Libs.room_runtime)
    implementation(Libs.room_rxjava2)
    kapt(Libs.room_compiler)

    implementation(Libs.threetenabp)

    implementation(Libs.rxjava)
    implementation(Libs.rxkotlin)
    implementation(Libs.rxandroid)
    implementation(Libs.rxbinding) // TODO think about other bindings?)
    implementation(Libs.rxbinding_recyclerview)
    implementation(Libs.autodispose_android_archcomponents)
    implementation(Libs.rxdogtag)
    implementation(Libs.rxdogtag_autodispose)

    implementation(Libs.dagger)
    kapt(Libs.dagger_compiler)

    implementation(Libs.timber)
    debugImplementation(Libs.leakcanary_android)

    testImplementation(Libs.junit_junit)
    testImplementation(Libs.com_google_truth_truth)
    testImplementation(Libs.core_testing)
    testImplementation(Libs.mockk)

    androidTestImplementation(Libs.fragment_testing)
    androidTestImplementation(Libs.com_google_truth_truth)
    androidTestImplementation(Libs.core_testing)
    androidTestImplementation(Libs.room_testing)
    androidTestImplementation(Libs.androidx_test_core)
    androidTestImplementation(Libs.androidx_test_ext_junit)
    androidTestImplementation(Libs.androidx_test_ext_truth)
    androidTestImplementation(Libs.androidx_test_runner)
    androidTestImplementation(Libs.androidx_test_rules)
    androidTestImplementation(Libs.espresso_core)
}
