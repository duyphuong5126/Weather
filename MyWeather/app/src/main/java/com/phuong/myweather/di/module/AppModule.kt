package com.phuong.myweather.di.module

import com.phuong.myweather.datasource.WeatherForecastRepositoryImpl
import com.phuong.myweather.datasource.local.WeatherForecastLocalDataSource
import com.phuong.myweather.datasource.local.WeatherForecastLocalDataSourceImpl
import com.phuong.myweather.datasource.remote.WeatherForecastRemoteDataSource
import com.phuong.myweather.datasource.remote.WeatherForecastRemoteDataSourceImpl
import com.phuong.myweather.domain.WeatherForecastRepository
import com.phuong.myweather.domain.usecase.GetWeatherForecastUseCase
import com.phuong.myweather.domain.usecase.GetWeatherForecastUseCaseImpl
import com.phuong.myweather.threading.SchedulersProvider
import com.phuong.myweather.threading.SchedulersProviderImpl
import dagger.Binds
import dagger.Module

@Module(includes = [NetworkModule::class, DatabaseModule::class, MainModule::class])
interface AppModule {
    @Binds
    fun bindsWeatherForecastRemoteDataSource(
        remoteDataSourceImpl: WeatherForecastRemoteDataSourceImpl
    ): WeatherForecastRemoteDataSource

    @Binds
    fun bindsWeatherForecastLocalDataSource(
        localDataSourceImpl: WeatherForecastLocalDataSourceImpl
    ): WeatherForecastLocalDataSource

    @Binds
    fun bindsWeatherForecastRepository(
        repositoryImpl: WeatherForecastRepositoryImpl
    ): WeatherForecastRepository

    @Binds
    fun bindsGetWeatherForecastUseCase(
        useCaseImpl: GetWeatherForecastUseCaseImpl
    ): GetWeatherForecastUseCase

    @Binds
    fun bindsSchedulersProvider(schedulersProviderImpl: SchedulersProviderImpl): SchedulersProvider
}