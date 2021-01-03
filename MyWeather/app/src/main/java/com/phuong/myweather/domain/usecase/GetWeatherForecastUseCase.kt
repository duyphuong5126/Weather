package com.phuong.myweather.domain.usecase

import com.phuong.myweather.domain.entity.SearchForecastResult
import com.phuong.myweather.domain.entity.TemperatureUnit
import io.reactivex.Single
import java.util.Date

interface GetWeatherForecastUseCase {
    fun execute(
        rawSearchQuery: String,
        sinceDate: Date,
        daysRange: Int,
        temperatureUnit: TemperatureUnit
    ): Single<SearchForecastResult>
}