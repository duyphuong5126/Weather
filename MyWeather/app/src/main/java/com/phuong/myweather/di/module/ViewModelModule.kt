package com.phuong.myweather.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.phuong.myweather.presentation.viewmodel.ViewModelFactory
import com.phuong.myweather.presentation.viewmodel.ViewModelKey
import com.phuong.myweather.presentation.viewmodel.WeatherForecastViewModelImpl
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {
    @Binds
    fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(WeatherForecastViewModelImpl::class)
    fun postListViewModel(viewModel: WeatherForecastViewModelImpl): ViewModel
}