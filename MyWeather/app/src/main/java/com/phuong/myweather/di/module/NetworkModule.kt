package com.phuong.myweather.di.module

import com.phuong.myweather.datasource.remote.apiservice.WeatherApiService
import com.phuong.myweather.datasource.remote.error.transformer.ErrorResponseTransformer
import com.phuong.myweather.utils.Constants.BASE_URL
import com.phuong.myweather.utils.ServiceGenerator
import dagger.Module
import dagger.Provides

@Module
class NetworkModule {
    @Provides
    fun providesServiceGenerator(): ServiceGenerator = ServiceGenerator(BASE_URL)

    @Provides
    fun providesWeatherApiService(serviceGenerator: ServiceGenerator): WeatherApiService {
        return serviceGenerator.createService(WeatherApiService::class.java)
    }

    @Provides
    fun providesErrorResponseTransformer(
        serviceGenerator: ServiceGenerator
    ): ErrorResponseTransformer {
        return serviceGenerator.createErrorResponseTransformer()
    }
}