package com.phuong.myweather.domain.entity

import java.util.Date

data class WeatherForecast(
    val date: Date,
    val averageTemperature: Temperature,
    val pressure: Int,
    val humidity: Int,
    val description: String
)