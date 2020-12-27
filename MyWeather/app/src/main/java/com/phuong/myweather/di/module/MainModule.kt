package com.phuong.myweather.di.module

import com.phuong.myweather.presentation.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = [ViewModelModule::class])
interface MainModule {
    @ContributesAndroidInjector
    fun contributesMainActivity(): MainActivity
}