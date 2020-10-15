object Libs {

    object Square {
        const val sqlDelightAndroidDriver =
                "com.squareup.sqldelight:android-driver:${Versions.sqlDelight}"

        const val sqlDelightJvm = "com.squareup.sqldelight:sqlite-driver:${Versions.sqlDelight}"

        const val sqlDelightFlow = "com.squareup.sqldelight:coroutines-extensions:${Versions.sqlDelight}"

        const val leakCanary: String =
                "com.squareup.leakcanary:leakcanary-android:${Versions.leakCanary}"

        const val turbine: String = "app.cash.turbine:turbine:${Versions.turbine}"
    }


    object Dagger {

        const val dagger: String = "com.google.dagger:dagger:${Versions.dagger}"

        const val daggerCompiler: String = "com.google.dagger:dagger-compiler:${Versions.dagger}"
    }

    object Kotlin {

        const val date = "org.jetbrains.kotlinx:kotlinx-datetime:${Versions.dateTime}"

        object Coroutines {
            const val core: String = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutines}"

            const val android: String = "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}"
        }
    }

    object Android {
        
        const val appcompat: String = "androidx.appcompat:appcompat:${Versions.appcompat}"

        const val material: String = "com.google.android.material:material:${Versions.material}"

        const val desguar: String = "com.android.tools:desugar_jdk_libs:${Versions.desguar}"

        const val dataStore: String = "androidx.datastore:datastore-preferences:${Versions.dataStore}"

        object Compose {
            
            const val runtime: String = "androidx.compose.runtime:runtime:${Versions.compose}"

            const val router: String = "com.github.zsoltk:compose-router:${Versions.router}"

            const val backstack: String = "com.zachklipp:compose-backstack:${Versions.backstack}"
            
            object Ui {
                const val tooling: String = "androidx.ui:ui-tooling:${Versions.compose}"
                
                const val layout: String = "androidx.compose.foundation:foundation-layout:${Versions.compose}"
                
                const val material: String = "androidx.compose.material:material:${Versions.compose}"
                
                const val core: String = "androidx.compose.ui:ui:${Versions.compose}"

                const val icons_extened = "androidx.compose.material:material-icons-extended:${Versions.compose}"

                const val foundation = "androidx.compose.foundation:foundation:${Versions.compose}"

                const val foundation_text = "androidx.compose.foundation:foundation-text:${Versions.compose}"
            }
        }
    }

    object Test {

        const val junit: String = "junit:junit:${Versions.junit}"

        const val truth: String = "com.google.truth:truth:${Versions.truth}"

    }

    const val timber: String = "com.jakewharton.timber:timber:${Versions.timber}"
}
