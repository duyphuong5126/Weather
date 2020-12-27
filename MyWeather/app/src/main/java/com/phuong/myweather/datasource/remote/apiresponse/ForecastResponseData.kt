package com.phuong.myweather.datasource.remote.apiresponse

import com.google.gson.annotations.SerializedName

data class ForecastResponseData(
    @SerializedName("list") val weatherForecasts: List<WeatherForecastData>
)