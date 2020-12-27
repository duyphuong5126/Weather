package com.phuong.myweather.datasource.remote.apiresponse

import com.google.gson.annotations.SerializedName

data class WeatherForecastData(
    @SerializedName("dt") val dateTime: Long,
    @SerializedName("temp") val temperature: TemperatureData,
    @SerializedName("pressure") val pressure: Int,
    @SerializedName("humidity") val humidity: Int,
    @SerializedName("weather") val weatherDescriptions: List<WeatherDescriptionData>
)