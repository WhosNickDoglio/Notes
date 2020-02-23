object Libs {

    object Square {
        const val sqlDelightAndroidDriver =
            "com.squareup.sqldelight:android-driver:${Versions.sqlDelight}"

        const val sqlDelightJvm = "com.squareup.sqldelight:sqlite-driver:${Versions.sqlDelight}"

        const val sqlDelightFlow = "com.squareup.sqldelight:coroutines-extensions:${Versions.sqlDelight}"


        const val leakCanary: String =
            "com.squareup.leakcanary:leakcanary-android:${Versions.leakCanary}"
    }


    object Dagger {

        const val dagger: String = "com.google.dagger:dagger:${Versions.dagger}"

        const val daggerCompiler: String = "com.google.dagger:dagger-compiler:${Versions.dagger}"
    }

    object Kotlin {

        const val Stdlib: String = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"

        object Coroutines {
            const val core: String = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"

            const val android: String = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"

            object Extensions {

                const val flowPreferences: String = "com.github.tfcporciuncula:flow-preferences:${Versions.flowPreferences}"

                object Binding {

                    const val android: String = "io.github.reactivecircus.flowbinding:flowbinding-android:${Versions.flowBinding}"
                    const val recyclerview: String = "io.github.reactivecircus.flowbinding:flowbinding-recyclerview:${Versions.flowBinding}"
                    const val activity: String = "io.github.reactivecircus.flowbinding:flowbinding-activity:${Versions.flowBinding}"
                }
            }
        }
    }

    object Android {

        const val coordinatorLayout: String =
            "androidx.coordinatorlayout:coordinatorlayout:${Versions.coordinator}"

        const val navigationFragmentKtx: String =
            "androidx.navigation:navigation-fragment-ktx:${Versions.navigation}"

        const val navigationUiKtx: String =
            "androidx.navigation:navigation-ui-ktx:${Versions.navigation}"

        const val fragmentKtx: String = "androidx.fragment:fragment-ktx:${Versions.fragment}"

        const val fragmentTesting: String =
            "androidx.fragment:fragment-testing:${Versions.fragment}"

        const val material: String = "com.google.android.material:material:${Versions.material}"

        const val constraintLayout: String =
            "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"

        const val recyclerview: String =
            "androidx.recyclerview:recyclerview:${Versions.recyclerview}"

        const val appcompat: String = "androidx.appcompat:appcompat:${Versions.appcompat}"

        const val preferences: String = "androidx.preference:preference-ktx:${Versions.preferences}"

        const val cardview = "androidx.cardview:cardview:${Versions.cardview}"

        const val lifecycleJava8 = "androidx.lifecycle:lifecycle-common-java8:${Versions.lifecycle}"
    }

    object Test {

        const val junit: String = "junit:junit:${Versions.junit}"

        const val androidxTestCore: String = "androidx.test:core:${Versions.androidTest}"

        const val androidxTestRules: String = "androidx.test:rules:${Versions.androidTest}"

        const val androidxTestRunner: String = "androidx.test:runner:${Versions.androidTest}"

        const val androidxTestExtJunit: String =
            "androidx.test.ext:junit:${Versions.junitTextExtensions}"

        const val androidxTestExtTruth: String =
            "androidx.test.ext:truth:${Versions.truthTestExtensions}"

        const val truth: String = "com.google.truth:truth:${Versions.truth}"

        const val espressoCore: String =
            "androidx.test.espresso:espresso-core:${Versions.espressoCore}"

        const val threeten: String = "org.threeten:threetenbp:${Versions.threeten}"

        const val threeTenGroup: String = "com.jakewharton.threetenabp"

        const val threeTenModule: String = "threetenabp"
    }

    object Flipper {
        const val debug: String = "com.facebook.flipper:flipper:${Versions.flipper}"
        const val release: String = "com.facebook.flipper:flipper-noop:${Versions.flipper}"
        const val soloader: String = "com.facebook.soloader:soloader:${Versions.soloader}"

    }

    object AboutLibs {
        const val core = "com.mikepenz:aboutlibraries-core:${Versions.about}"
        const val about = "com.mikepenz:aboutlibraries:${Versions.about}"

    }

    const val threetenabp: String =
        "com.jakewharton.threetenabp:threetenabp:${Versions.threetenabp}"

    const val timber: String = "com.jakewharton.timber:timber:${Versions.timber}"
}
