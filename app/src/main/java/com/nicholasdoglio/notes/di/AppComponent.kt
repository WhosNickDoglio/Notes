package com.nicholasdoglio.notes.di

import android.app.Application
import com.nicholasdoglio.notes.NotesApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * @author Nicholas Doglio
 */
@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        DatabaseModule::class,
        BindingModule::class,
        FragmentsBindingModule::class,
        ViewModelBindingModule::class
    ]
)
interface AppComponent : AndroidInjector<NotesApplication> {
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application): AppComponent
    }
}
