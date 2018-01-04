package com.nicholasdoglio.notes.di

import android.app.Application
import com.nicholasdoglio.notes.NotesApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

/**
 * @author Nicholas Doglio
 */
@Singleton
@Component(modules = arrayOf(
        AndroidInjectionModule::class,
        AndroidSupportInjectionModule::class,
        AppModule::class,
        MainActivityBindingModule::class,
        ViewModelModule::class
))
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    fun inject(app: NotesApplication)
}