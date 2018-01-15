package com.nicholasdoglio.notes.di

import com.nicholasdoglio.notes.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

/**
 * @author Nicholas Doglio
 */
@Module
abstract class MainActivityBindingModule {

    @ContributesAndroidInjector(modules = arrayOf(FragmentBindingModule::class))
    abstract fun contributeMainActivity(): MainActivity
}