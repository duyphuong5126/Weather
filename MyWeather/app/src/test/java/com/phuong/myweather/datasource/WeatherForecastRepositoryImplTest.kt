package com.phuong.myweather.datasource

import com.phuong.myweather.TestDataProvider
import com.phuong.myweather.TestSchedulersProvider
import com.phuong.myweather.datasource.local.WeatherForecastLocalDataSource
import com.phuong.myweather.datasource.remote.WeatherForecastRemoteDataSource
import com.phuong.myweather.datasource.remote.apiresponse.ForecastResponseData
import com.phuong.myweather.domain.entity.TemperatureUnit.Celsius
import com.phuong.myweather.safeAnyList
import com.phuong.myweather.safeAnyString
import com.phuong.myweather.safeEq
import io.reactivex.Maybe
import io.reactivex.Single
import org.junit.Test
import org.junit.Assert.assertEquals
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import java.util.Date
import org.mockito.Mockito.`when` as whenever

class WeatherForecastRepositoryImplTest {
    private val remoteDataSource = mock(WeatherForecastRemoteDataSource::class.java)
    private val localDataSource = mock(WeatherForecastLocalDataSource::class.java)
    private val schedulersProvider = TestSchedulersProvider()

    private val repositoryImpl =
        WeatherForecastRepositoryImpl(remoteDataSource, localDataSource, schedulersProvider)

    @Test
    fun `getDailyForecast, local cache is empty`() {
        val response = ForecastResponseData(
            listOf(
                TestDataProvider.generateWeatherForecastData(
                    dateTime = 1609041600,
                    minTemp = 20.0,
                    maxTemp = 30.0,
                    pressure = 1012,
                    humidity = 74,
                    weatherDescriptions = listOf("broken clouds", "light rain")
                )
            )
        )
        whenever(remoteDataSource.getDailyForecast("saigon", 1, Celsius.code))
            .thenReturn(Single.just(response))

        whenever(localDataSource.getWeatherForecast("saigon", 1))
            .thenReturn(Maybe.empty())

        repositoryImpl.getDailyForecast("saigon", 1, Celsius)
            .test()
            .assertValue {
                assertEquals(1, it.size)
                val weatherForecast = it[0]
                assertEquals(Date(1609041600000), weatherForecast.date)
                assertEquals(25.0, weatherForecast.averageTemperature.value, 0.0)
                assertEquals(Celsius, weatherForecast.averageTemperature.unit)
                assertEquals(1012, weatherForecast.pressure)
                assertEquals(74, weatherForecast.humidity)
                assertEquals("broken clouds, light rain", weatherForecast.description)
                true
            }

        verify(localDataSource).getWeatherForecast("saigon", 1)
        verify(remoteDataSource).getDailyForecast("saigon", 1, Celsius.code)
        verify(localDataSource).saveSearchResult(safeEq("saigon"), safeAnyList())
    }

    @Test
    fun `getDailyForecast, local cache is not empty`() {
        whenever(remoteDataSource.getDailyForecast("saigon", 1, Celsius.code))
            .thenReturn(Single.just(ForecastResponseData(emptyList())))
        whenever(localDataSource.getWeatherForecast("saigon", 1))
            .thenReturn(Maybe.just(listOf(Day1ForecastCelsius)))

        repositoryImpl.getDailyForecast("saigon", 1, Celsius)
            .test()
            .assertValue {
                assertEquals(1, it.size)
                assertEquals(Day1ForecastCelsius, it[0])
                true
            }

        verify(localDataSource).getWeatherForecast("saigon", 1)
        verify(remoteDataSource).getDailyForecast("saigon", 1, Celsius.code)
        verify(localDataSource, never()).saveSearchResult(safeAnyString(), safeAnyList())
    }

    companion object {
        private val Day1ForecastCelsius = TestDataProvider.generateWeatherForecast(
            dateLong = 1609041600000,
            avgTemp = 31.4,
            avgTempUnit = Celsius.code,
            pressure = 1012,
            humidity = 70,
            description = "scattered clouds"
        )
    }
}