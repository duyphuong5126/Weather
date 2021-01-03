package com.phuong.myweather.datasource.local

import com.phuong.myweather.domain.entity.WeatherForecast
import io.reactivex.Maybe
import java.util.Date

interface WeatherForecastLocalDataSource {
    fun saveSearchResult(searchQuery: String, forecastList: List<WeatherForecast>)
    fun getWeatherForecast(
        searchQuery: String,
        sinceDate: Date,
        daysRange: Int
    ): Maybe<List<WeatherForecast>>
}