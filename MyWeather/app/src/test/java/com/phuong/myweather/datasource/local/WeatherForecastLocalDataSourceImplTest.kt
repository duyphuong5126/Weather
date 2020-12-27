package com.phuong.myweather.datasource.local

import com.phuong.myweather.TestDataProvider
import com.phuong.myweather.datasource.local.db.ForecastResultDAO
import com.phuong.myweather.domain.entity.TemperatureUnit.Celsius
import com.phuong.myweather.safeArgThat
import io.reactivex.Maybe
import org.junit.Test
import org.junit.Assert.assertEquals
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import java.util.Date
import org.mockito.Mockito.`when` as whenever

class WeatherForecastLocalDataSourceImplTest {
    private val forecastResultDAO = mock(ForecastResultDAO::class.java)

    private val localDataSourceImpl = WeatherForecastLocalDataSourceImpl(forecastResultDAO)

    @Test
    fun `getWeatherForecast, local cache is empty`() {
        whenever(forecastResultDAO.getMatchedResults("saigon", 1))
            .thenReturn(Maybe.just(emptyList()))

        localDataSourceImpl.getWeatherForecast("saigon", 1)
            .test()
            .assertComplete()
    }

    @Test
    fun `getWeatherForecast, local cache is not empty`() {
        val localModel = TestDataProvider.generateForecastResultModel(
            searchQuery = "saigon",
            date = 1609041600000,
            avgTemp = 25.0,
            avgTempUnit = Celsius.code,
            pressure = 1012,
            humidity = 74,
            description = "broken clouds"
        )
        whenever(forecastResultDAO.getMatchedResults("saigon", 1))
            .thenReturn(Maybe.just(listOf(localModel)))

        localDataSourceImpl.getWeatherForecast("saigon", 1)
            .test()
            .assertValue {
                assertEquals(1, it.size)
                val weatherForecast = it[0]
                assertEquals(Date(1609041600000), weatherForecast.date)
                assertEquals(25.0, weatherForecast.averageTemperature.value, 0.0)
                assertEquals(Celsius, weatherForecast.averageTemperature.unit)
                assertEquals(1012, weatherForecast.pressure)
                assertEquals(74, weatherForecast.humidity)
                assertEquals("broken clouds", weatherForecast.description)
                true
            }
    }

    @Test
    fun saveSearchResult() {
        val weatherForecast = TestDataProvider.generateWeatherForecast(
            dateLong = 1609041600000,
            avgTemp = 31.4,
            avgTempUnit = Celsius.code,
            pressure = 1012,
            humidity = 70,
            description = "scattered clouds"
        )
        val weatherForecastList = listOf(weatherForecast)

        localDataSourceImpl.saveSearchResult("saigon", weatherForecastList)

        verify(forecastResultDAO).insert(safeArgThat(emptyList(), {
            assertEquals(1, it.size)
            val model = it[0]
            assertEquals(1609041600000, model.date)
            assertEquals(31.4, model.avgTemp, 0.0)
            assertEquals(Celsius.code, model.avgTempUnit)
            assertEquals(1012, model.pressure)
            assertEquals(70, model.humidity)
            assertEquals("scattered clouds", model.description)
            assertEquals("saigon", model.searchQuery)
            true
        }))
    }
}