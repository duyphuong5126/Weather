package com.phuong.myweather.domain.entity

sealed class SearchForecastResult {
    data class ValidationError(val message: String) : SearchForecastResult()

    data class ForecastResults(val weatherForecasts: List<WeatherForecast>) : SearchForecastResult()
}