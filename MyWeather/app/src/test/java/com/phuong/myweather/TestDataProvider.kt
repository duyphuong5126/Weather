package com.phuong.myweather

import com.phuong.myweather.datasource.local.db.ForecastResultModel
import com.phuong.myweather.datasource.remote.apiresponse.TemperatureData
import com.phuong.myweather.datasource.remote.apiresponse.WeatherDescriptionData
import com.phuong.myweather.datasource.remote.apiresponse.WeatherForecastData
import com.phuong.myweather.datasource.remote.error.ApiError
import com.phuong.myweather.domain.entity.Temperature
import com.phuong.myweather.domain.entity.TemperatureUnit.Companion.fromUnitCode
import com.phuong.myweather.domain.entity.TemperatureUnit.Celsius
import com.phuong.myweather.domain.entity.WeatherForecast
import java.util.Date

object TestDataProvider {
    fun generateApiError(message: String = "", throwable: Throwable? = null): ApiError {
        return ApiError(message, throwable)
    }

    fun generateWeatherForecast(
        dateLong: Long = 1609041600000,
        avgTemp: Double = 32.5,
        avgTempUnit: String = Celsius.code,
        pressure: Int = 1012,
        humidity: Int = 70,
        description: String = "scattered clouds"
    ): WeatherForecast {
        return WeatherForecast(
            date = Date(dateLong),
            averageTemperature = Temperature(avgTemp, fromUnitCode(avgTempUnit)),
            pressure = pressure,
            humidity = humidity,
            description = description
        )
    }

    fun generateWeatherForecastData(
        dateTime: Long = 1609041600,
        minTemp: Double = 23.5,
        maxTemp: Double = 31.73,
        pressure: Int = 1012,
        humidity: Int = 74,
        weatherDescriptions: List<String> = listOf("broken clouds")
    ): WeatherForecastData {
        return WeatherForecastData(
            dateTime = dateTime,
            temperature = TemperatureData(minTemp, maxTemp),
            pressure = pressure,
            humidity = humidity,
            weatherDescriptions = weatherDescriptions.map(::WeatherDescriptionData)
        )
    }

    fun generateForecastResultModel(
        searchQuery: String,
        date: Long = 1609041600000,
        avgTemp: Double = 25.0,
        avgTempUnit: String = Celsius.code,
        pressure: Int = 1012,
        humidity: Int = 74,
        description: String = "broken clouds"
    ): ForecastResultModel {
        return ForecastResultModel(
            date = date,
            avgTemp = avgTemp,
            avgTempUnit = avgTempUnit,
            pressure = pressure,
            humidity = humidity,
            description = description,
            searchQuery = searchQuery
        )
    }
}