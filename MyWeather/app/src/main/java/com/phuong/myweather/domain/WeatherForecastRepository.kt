package com.phuong.myweather.domain

import com.phuong.myweather.domain.entity.TemperatureUnit
import com.phuong.myweather.domain.entity.WeatherForecast
import io.reactivex.Single

interface WeatherForecastRepository {
    fun getDailyForecast(
        query: String,
        daysRange: Int,
        temperatureUnit: TemperatureUnit
    ): Single<List<WeatherForecast>>
}