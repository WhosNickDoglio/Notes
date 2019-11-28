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

buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:${Versions.com_android_tools_build_gradle}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin_gradle_plugin}")
        classpath("com.dicedmelon.gradle:jacoco-android:${Versions.jacoco_android}")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.androidx_navigation}")
        classpath("com.jaredsburrows:gradle-license-plugin:${Versions.gradle_license_plugin}")
        classpath("com.soundcloud.delect:delect-plugin:${Versions.delect_plugin}")
    }
}

plugins {
    id("com.gradle.build-scan") version("3.0")
    id("io.gitlab.arturbosch.detekt") version("1.1.1")
    id("de.fayard.buildSrcVersions") version("0.7.0")
}

buildSrcVersions {
    rejectVersionIf {
        candidate.version.contains("EAP")
    }
    indent = "\t"
    useFqdnFor("org_jetbrains_kotlin_kotlin_stdlib_jdk8")
}

buildScan {
    setTermsOfServiceUrl("https://gradle.com/terms-of-service")
    setTermsOfServiceAgree("yes")
    publishAlways()
}

allprojects {
    repositories {
        google()
        jcenter()
    }
}

tasks.wrapper {
    distributionType = Wrapper.DistributionType.ALL
}

tasks.register<Delete>("clean") {
    delete("build")
}