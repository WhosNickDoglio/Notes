import org.gradle.plugin.use.PluginDependenciesSpec
import org.gradle.plugin.use.PluginDependencySpec

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

val PluginDependenciesSpec.detekt: PluginDependencySpec
    inline get() =
        id("io.gitlab.arturbosch.detekt").version(Versions.detekt)

val PluginDependenciesSpec.benManesVersions: PluginDependencySpec
    inline get() =
        id("com.github.ben-manes.versions").version(Versions.benManesVersions)

object GradlePlugin {
    const val sqlDelight = "com.squareup.sqldelight:gradle-plugin:${Versions.sqlDelight}"
    const val ktlint = "org.jlleitschuh.gradle:ktlint-gradle:${Versions.ktlintGradlePlugin}"
    const val navigationSafeArgs =
        "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigation}"
    const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val android = "com.android.tools.build:gradle:${Versions.androidBuildTools}"
    const val delect = "com.soundcloud.delect:delect-plugin:${Versions.delect}"
    const val junitJacoco = "com.vanniktech:gradle-android-junit-jacoco-plugin:${Versions.junitJacoco}"
    const val aboutLibraries =
        "com.mikepenz.aboutlibraries.plugin:aboutlibraries-plugin:${Versions.about}"
}

object Plugins {
    object Android {
        const val application = "com.android.application"
        const val safeArgs = "androidx.navigation.safeargs.kotlin"
    }

    object Kotlin {
        const val android = "android"
        const val kapt = "kapt"
    }

    const val detekt = "io.gitlab.arturbosch.detekt"
    const val ktlint = "org.jlleitschuh.gradle.ktlint"
    const val sqlDelight = "com.squareup.sqldelight"
    const val delect = "com.soundcloud.delect"
    const val scabbard = "scabbard.gradle"
    const val junitJacoco = "com.vanniktech.android.junit.jacoco"
    const val aboutLibs = "com.mikepenz.aboutlibraries.plugin"
}
