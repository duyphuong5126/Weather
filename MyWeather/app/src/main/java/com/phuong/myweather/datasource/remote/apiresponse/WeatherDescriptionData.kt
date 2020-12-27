package com.phuong.myweather.datasource.remote.apiresponse

import com.google.gson.annotations.SerializedName

data class WeatherDescriptionData(@SerializedName("description") val description: String)