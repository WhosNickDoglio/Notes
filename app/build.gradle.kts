import dev.arunkumar.scabbard.gradle.ScabbardSpec
import org.gradle.api.tasks.testing.logging.TestExceptionFormat
import org.gradle.api.tasks.testing.logging.TestLogEvent

/*
 * MIT License
 *
 * Copyright (c) 2020 Nicholas Doglio
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
    id(Plugins.Android.application)
    kotlin(Plugins.Kotlin.android)
    kotlin(Plugins.Kotlin.kapt)
    id(Plugins.Android.safeArgs)
    id(Plugins.detekt)
    id(Plugins.ktlint)
    id(Plugins.sqlDelight)
    id(Plugins.delect)
    id(Plugins.license)
    id(Plugins.scabbard) version Versions.scabbard
}

kapt {
    correctErrorTypes = true
    javacOptions {
        option("-source", "8")
        option("-target", "8")
    }
}

tasks.withType<Test> {
    testLogging {
        exceptionFormat = TestExceptionFormat.FULL
        events = setOf(
            TestLogEvent.SKIPPED,
            TestLogEvent.PASSED,
            TestLogEvent.FAILED
        )
        showStandardStreams = true
    }
}

scabbard.configure(closureOf<ScabbardSpec> {
    enabled(true)
})

delect {
    daggerReflectVersion = Versions.daggerReflect
}

ktlint {
    version.set(Versions.ktlint)
    android.set(true)
    outputColorName.set("RED")
    disabledRules.set(setOf("import-ordering"))
}

android {
    compileSdkVersion(Config.compileSdk)
    defaultConfig {
        applicationId = Config.applicationId
        minSdkVersion(Config.minSdk)
        targetSdkVersion(Config.targetSdk)
        versionCode = Config.versionCode
        versionName = Config.versionName
        testInstrumentationRunner = Config.testRunner
    }

    buildTypes {
        named("release") {
            isMinifyEnabled = false
            setProguardFiles(
                listOf(
                    getDefaultProguardFile("proguard-android.txt"),
                    "proguard-rules.pro"
                )
            )
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

sqldelight {
    database("NoteDatabase") {
    }
}

dependencies {
    implementation(Libs.Kotlin.Stdlib)

    implementation(Libs.Android.fragmentKtx)
    implementation(Libs.Android.appcompat)
    implementation(Libs.Android.recyclerview)
    implementation(Libs.Android.material)
    implementation(Libs.Android.constraintLayout)
    implementation(Libs.Android.navigationFragmentKtx)
    implementation(Libs.Android.navigationUiKtx)
    implementation("androidx.coordinatorlayout:coordinatorlayout:1.1.0")

    implementation(Libs.Square.sqlDelightAndroidDriver)
    implementation(Libs.Square.sqlDelightRxExt)

    implementation(Libs.threetenabp)
    implementation(Libs.Rx.rxjava)
    implementation(Libs.Rx.rxkotlin)
    implementation(Libs.Rx.rxandroid)
    implementation(Libs.Rx.Binding) // TODO think about other bindings?)
    implementation(Libs.Rx.BindingRecyclerView)
    implementation(Libs.Rx.autodisposeAndroidArchcomponents)
    implementation(Libs.Rx.rxdogtag)
    implementation(Libs.Rx.rxdogtagAutodispose)

    implementation(Libs.Dagger.dagger)
    kapt(Libs.Dagger.daggerCompiler)

    implementation(Libs.timber)
    debugImplementation(Libs.Square.leakCanary)

    testImplementation(Libs.Test.junit)
    testImplementation(Libs.Test.truth)
    testImplementation(Libs.Test.coreTesting)
    testImplementation(Libs.Test.mockk)
    testImplementation(Libs.Square.sqlDelightJvm)

    testImplementation("org.threeten:threetenbp:1.4.0") {
        exclude(group = "com.jakewharton.threetenabp", module = "threetenabp")
    }

    androidTestImplementation(Libs.Android.fragmentTesting)
    androidTestImplementation(Libs.Test.truth)
    androidTestImplementation(Libs.Test.coreTesting)
    androidTestImplementation(Libs.Test.androidxTestCore)
    androidTestImplementation(Libs.Test.androidxTestExtJunit)
    androidTestImplementation(Libs.Test.androidxTestExtTruth)
    androidTestImplementation(Libs.Test.androidxTestRunner)
    androidTestImplementation(Libs.Test.androidxTestRules)
    androidTestImplementation(Libs.Test.espressoCore)
}
