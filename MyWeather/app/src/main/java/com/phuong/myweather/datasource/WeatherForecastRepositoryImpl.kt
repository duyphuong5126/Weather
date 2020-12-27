package com.phuong.myweather.datasource

import com.phuong.myweather.datasource.local.WeatherForecastLocalDataSource
import com.phuong.myweather.datasource.remote.WeatherForecastRemoteDataSource
import com.phuong.myweather.datasource.remote.apiresponse.ForecastResponseData
import com.phuong.myweather.datasource.remote.apiresponse.WeatherDescriptionData
import com.phuong.myweather.domain.WeatherForecastRepository
import com.phuong.myweather.domain.entity.Temperature
import com.phuong.myweather.domain.entity.TemperatureUnit
import com.phuong.myweather.domain.entity.WeatherForecast
import com.phuong.myweather.threading.SchedulersProvider
import io.reactivex.Single
import java.util.Date
import javax.inject.Inject

class WeatherForecastRepositoryImpl @Inject constructor(
    private val remoteDataSource: WeatherForecastRemoteDataSource,
    private val localDataSource: WeatherForecastLocalDataSource,
    private val schedulersProvider: SchedulersProvider
) : WeatherForecastRepository {
    override fun getDailyForecast(
        query: String,
        daysRange: Int,
        temperatureUnit: TemperatureUnit
    ): Single<List<WeatherForecast>> {
        val remoteSource = remoteDataSource.getDailyForecast(
            query = query, daysRange = daysRange, temperatureUnit = temperatureUnit.code
        ).map {
            transformRemoteData(it, temperatureUnit)
        }.observeOn(
            schedulersProvider.io()
        ).doOnSuccess {
            localDataSource.saveSearchResult(query, it)
        }

        val localSource = localDataSource.getWeatherForecast(query, daysRange)

        return localSource.switchIfEmpty(remoteSource)
    }

    private fun transformRemoteData(
        forecastResponseData: ForecastResponseData,
        temperatureUnit: TemperatureUnit
    ): List<WeatherForecast> {
        return forecastResponseData.weatherForecasts.map {
            val date = Date(it.dateTime * 1000)
            val averageTemperature = Temperature(
                value = (it.temperature.minValue + it.temperature.maxValue) / 2,
                unit = temperatureUnit
            )
            val weatherDescriptions = it.weatherDescriptions
                .map(WeatherDescriptionData::description)
                .joinToString(separator = ", ")

            WeatherForecast(
                date = date,
                averageTemperature = averageTemperature,
                pressure = it.pressure,
                humidity = it.humidity,
                description = weatherDescriptions
            )
        }
    }
}