package com.phuong.myweather.datasource.remote.apiresponse

import com.google.gson.annotations.SerializedName

data class TemperatureData(
    @SerializedName("min") val minValue: Double,
    @SerializedName("max") val maxValue: Double
)