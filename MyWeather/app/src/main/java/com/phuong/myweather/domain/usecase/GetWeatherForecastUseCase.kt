package com.phuong.myweather.domain.usecase

import com.phuong.myweather.domain.entity.SearchForecastResult
import com.phuong.myweather.domain.entity.TemperatureUnit
import io.reactivex.Single

interface GetWeatherForecastUseCase {
    fun execute(
        rawSearchQuery: String,
        daysRange: Int,
        temperatureUnit: TemperatureUnit
    ): Single<SearchForecastResult>
}