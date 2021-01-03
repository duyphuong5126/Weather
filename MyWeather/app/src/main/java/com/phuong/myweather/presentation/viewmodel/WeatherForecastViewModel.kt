package com.phuong.myweather.presentation.viewmodel

import androidx.lifecycle.LiveData
import com.phuong.myweather.domain.entity.TemperatureUnit
import com.phuong.myweather.view.WeatherForecastUiModel
import java.util.Date

interface WeatherForecastViewModel {
    val weatherForecastLiveData: LiveData<List<WeatherForecastUiModel>>
    val searchValidationErrorLiveData: LiveData<String>
    val daysRangeErrorLiveData: LiveData<String>
    val searchErrorLiveData: LiveData<String>

    fun getWeatherForecast(
        rawSearchQuery: String,
        sinceDate: Date,
        daysRange: Int,
        tempUnit: TemperatureUnit
    )
}