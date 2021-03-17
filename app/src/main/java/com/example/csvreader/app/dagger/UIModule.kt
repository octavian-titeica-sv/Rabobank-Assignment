package com.example.csvreader.app.dagger

import com.example.csvreader.ui.MainFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class UIModule {

    @ContributesAndroidInjector
    abstract fun mainFragment(): MainFragment
}
