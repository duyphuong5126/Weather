package com.phuong.myweather.domain

import com.phuong.myweather.domain.entity.TemperatureUnit
import com.phuong.myweather.domain.entity.WeatherForecast
import io.reactivex.Single
import java.util.Date

interface WeatherForecastRepository {
    fun getDailyForecast(
        query: String,
        sinceDate: Date,
        daysRange: Int,
        temperatureUnit: TemperatureUnit
    ): Single<List<WeatherForecast>>
}