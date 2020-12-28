package com.phuong.myweather.datasource.local

import com.phuong.myweather.datasource.local.db.ForecastResultDAO
import com.phuong.myweather.datasource.local.db.ForecastResultModel
import com.phuong.myweather.domain.entity.Temperature
import com.phuong.myweather.domain.entity.TemperatureUnit
import com.phuong.myweather.domain.entity.WeatherForecast
import io.reactivex.Maybe
import java.util.Date
import javax.inject.Inject

class WeatherForecastLocalDataSourceImpl @Inject constructor(
    private val forecastResultDAO: ForecastResultDAO
) : WeatherForecastLocalDataSource {
    override fun saveSearchResult(searchQuery: String, forecastList: List<WeatherForecast>) {
        forecastResultDAO.insert(entities2Models(searchQuery, forecastList))
    }

    override fun getWeatherForecast(
        searchQuery: String,
        daysRange: Int
    ): Maybe<List<WeatherForecast>> {
        return forecastResultDAO.getMatchedResults(searchQuery, daysRange)
            .flatMap {
                if (it.size < daysRange) {
                    Maybe.empty()
                } else {
                    Maybe.just(models2Entities(it))
                }
            }
    }

    private fun models2Entities(resultModelList: List<ForecastResultModel>): List<WeatherForecast> {
        return resultModelList.map {
            WeatherForecast(
                date = Date(it.date),
                averageTemperature = Temperature(
                    it.avgTemp,
                    TemperatureUnit.fromUnitCode(it.avgTempUnit)
                ),
                pressure = it.pressure,
                humidity = it.humidity,
                description = it.description
            )
        }
    }

    private fun entities2Models(
        searchQuery: String,
        resultModelList: List<WeatherForecast>
    ): List<ForecastResultModel> {
        return resultModelList.map {
            ForecastResultModel(
                date = it.date.time,
                avgTemp = it.averageTemperature.value,
                avgTempUnit = it.averageTemperature.unit.code,
                pressure = it.pressure,
                humidity = it.humidity,
                description = it.description,
                searchQuery = searchQuery
            )
        }
    }
}