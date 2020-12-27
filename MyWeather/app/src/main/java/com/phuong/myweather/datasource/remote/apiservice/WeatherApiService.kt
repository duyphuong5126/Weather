package com.phuong.myweather.datasource.remote.apiservice

import com.phuong.myweather.datasource.remote.apiresponse.ForecastResponseData
import com.phuong.myweather.utils.Constants.API_KEY
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApiService {
    @GET("forecast/daily")
    fun getWeatherForecast(
        @Query("appid") appId: String = API_KEY,
        @Query("q") queryString: String,
        @Query("cnt") daysRange: Int,
        @Query("units") units: String
    ): Call<ForecastResponseData>
}