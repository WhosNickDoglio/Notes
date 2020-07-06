plugins {
    kotlin("jvm")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    implementation(Libs.Kotlin.Stdlib)
    implementation(Libs.Kotlin.Coroutines.core)
    implementation(Libs.Kotlin.Coroutines.android)
}