package com.phuong.myweather.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.phuong.myweather.datasource.remote.error.ApiError
import com.phuong.myweather.domain.entity.SearchForecastResult.ValidationError
import com.phuong.myweather.domain.entity.SearchForecastResult.InvalidDayRange
import com.phuong.myweather.domain.entity.SearchForecastResult.ForecastResults
import com.phuong.myweather.domain.entity.TemperatureUnit
import com.phuong.myweather.domain.entity.WeatherForecast
import com.phuong.myweather.domain.usecase.GetWeatherForecastUseCase
import com.phuong.myweather.threading.SchedulersProvider
import com.phuong.myweather.utils.Constants.NETWORK_ERROR
import com.phuong.myweather.utils.Constants.WEATHER_FORECAST_GENERAL_ERROR
import com.phuong.myweather.utils.isConnectionError
import com.phuong.myweather.view.WeatherForecastUiModel
import io.reactivex.disposables.CompositeDisposable
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class WeatherForecastViewModelImpl @Inject constructor(
    private val getWeatherForecastUseCase: GetWeatherForecastUseCase,
    private val schedulersProvider: SchedulersProvider
) : ViewModel(), WeatherForecastViewModel {
    private val compositeDisposable = CompositeDisposable()

    private val _weatherForecastLiveData = MutableLiveData<List<WeatherForecastUiModel>>()
    override val weatherForecastLiveData: LiveData<List<WeatherForecastUiModel>>
        get() = _weatherForecastLiveData

    private val _searchValidationErrorLiveData = MutableLiveData<String>()
    override val searchValidationErrorLiveData: LiveData<String>
        get() = _searchValidationErrorLiveData

    private val _searchErrorLiveData = MutableLiveData<String>()
    override val searchErrorLiveData: LiveData<String>
        get() = _searchErrorLiveData

    private val _daysRangeErrorLiveData = MutableLiveData<String>()
    override val daysRangeErrorLiveData: LiveData<String>
        get() = _daysRangeErrorLiveData

    override fun getWeatherForecast(
        rawSearchQuery: String,
        daysRange: Int,
        tempUnit: TemperatureUnit
    ) {
        getWeatherForecastUseCase.execute(rawSearchQuery, daysRange, tempUnit)
            .subscribeOn(schedulersProvider.io())
            .observeOn(schedulersProvider.mainThread())
            .subscribe({
                when (it) {
                    is ValidationError -> {
                        _weatherForecastLiveData.postValue(emptyList())
                        _searchErrorLiveData.postValue(null)
                        _daysRangeErrorLiveData.postValue(null)
                        _searchValidationErrorLiveData.postValue(it.message)
                    }

                    is InvalidDayRange -> {
                        _weatherForecastLiveData.postValue(emptyList())
                        _searchErrorLiveData.postValue(null)
                        _daysRangeErrorLiveData.postValue(it.message)
                        _searchValidationErrorLiveData.postValue(null)
                    }

                    is ForecastResults -> {
                        val weatherForecasts = transformDisplayableForecasts(it.weatherForecasts)
                        Timber.d("Fetched ${weatherForecasts.size} items")
                        _weatherForecastLiveData.postValue(weatherForecasts)
                        _searchErrorLiveData.postValue(null)
                        _daysRangeErrorLiveData.postValue(null)
                        _searchValidationErrorLiveData.postValue(null)
                    }
                }
            }, {
                Timber.e(it)
                val error = when {
                    it.isConnectionError() -> NETWORK_ERROR
                    it is ApiError -> it.message ?: WEATHER_FORECAST_GENERAL_ERROR
                    else -> WEATHER_FORECAST_GENERAL_ERROR
                }
                _weatherForecastLiveData.postValue(emptyList())
                _searchErrorLiveData.postValue(error)
                _daysRangeErrorLiveData.postValue(null)
                _searchValidationErrorLiveData.postValue(null)
            }).let(compositeDisposable::add)
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    private fun transformDisplayableForecasts(
        forecastList: List<WeatherForecast>
    ): List<WeatherForecastUiModel> {
        val dateFormat = SimpleDateFormat("EEE',' dd MMM yyyy", Locale.getDefault())
        return forecastList.map {
            val averageTemp = it.averageTemperature
            WeatherForecastUiModel(
                date = dateFormat.format(it.date),
                averageTemperature = "${averageTemp.value.toInt()}${averageTemp.unit.symbol}",
                pressure = it.pressure,
                humidity = "${it.humidity}%",
                description = it.description
            )
        }
    }
}