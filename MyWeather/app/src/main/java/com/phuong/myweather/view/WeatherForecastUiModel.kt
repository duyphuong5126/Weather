package com.phuong.myweather.view

data class WeatherForecastUiModel(
    val date: String,
    val averageTemperature: String,
    val pressure: Int,
    val humidity: String,
    val description: String
)