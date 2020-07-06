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
        }
    }

    object Android {
        
        const val appcompat: String = "androidx.appcompat:appcompat:${Versions.appcompat}"

        const val material: String = "com.google.android.material:material:${Versions.material}"

        const val desguar: String = "com.android.tools:desugar_jdk_libs:${Versions.desguar}"

        object Compose {
            
            const val runtime: String = "androidx.compose:compose-runtime:${Versions.compose}"

            const val router: String = "com.github.zsoltk:compose-router:${Versions.router}"
            
            object Ui {
                const val tooling: String = "androidx.ui:ui-tooling:${Versions.compose}"
                
                const val layout: String = "androidx.ui:ui-layout:${Versions.compose}"
                
                const val material: String = "androidx.ui:ui-material:${Versions.compose}"
                
                const val core: String = "androidx.ui:ui-core:${Versions.compose}"

                const val icons_extened = "androidx.ui:ui-material-icons-extended:${Versions.compose}"

                const val foundation = "androidx.ui:ui-foundation:${Versions.compose}"
            }
        }
    }

    object Test {

        const val junit: String = "junit:junit:${Versions.junit}"

        const val truth: String = "com.google.truth:truth:${Versions.truth}"

    }

    const val timber: String = "com.jakewharton.timber:timber:${Versions.timber}"
}
