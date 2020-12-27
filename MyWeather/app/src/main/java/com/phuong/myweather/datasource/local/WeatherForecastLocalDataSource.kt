package com.phuong.myweather.datasource.local

import com.phuong.myweather.domain.entity.WeatherForecast
import io.reactivex.Maybe

interface WeatherForecastLocalDataSource {
    fun saveSearchResult(searchQuery: String, forecastList: List<WeatherForecast>)
    fun getWeatherForecast(searchQuery: String, daysRange: Int): Maybe<List<WeatherForecast>>
}