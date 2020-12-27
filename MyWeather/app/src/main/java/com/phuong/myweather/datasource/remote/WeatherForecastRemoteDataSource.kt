package com.phuong.myweather.datasource.remote

import com.phuong.myweather.datasource.remote.apiresponse.ForecastResponseData
import io.reactivex.Single

interface WeatherForecastRemoteDataSource {
    fun getDailyForecast(
        query: String,
        daysRange: Int,
        temperatureUnit: String
    ): Single<ForecastResponseData>
}