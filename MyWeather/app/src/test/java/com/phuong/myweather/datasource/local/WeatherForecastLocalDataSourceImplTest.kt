package com.phuong.myweather.datasource.local

import com.phuong.myweather.TestDataProvider
import com.phuong.myweather.datasource.local.db.ForecastResultDAO
import com.phuong.myweather.domain.entity.TemperatureUnit.Celsius
import com.phuong.myweather.safeArgThat
import com.phuong.myweather.safeEq
import io.reactivex.Maybe
import org.junit.Test
import org.junit.Assert.assertEquals
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import org.mockito.Mockito.`when` as whenever

class WeatherForecastLocalDataSourceImplTest {
    private val forecastResultDAO = mock(ForecastResultDAO::class.java)

    private val localDataSourceImpl = WeatherForecastLocalDataSourceImpl(forecastResultDAO)

    private val today: Date
    private val todayMidNight: Date

    init {
        val dateFormat = SimpleDateFormat("EEE',' dd MMM yyyy HH':'mm':'ss 'GMT'", Locale.US)
        today = dateFormat.parse("Sun, 03 Jan 2021 09:09:09 GMT")!!
        todayMidNight = dateFormat.parse("Sun, 03 Jan 2021 00:00:00 GMT")!!
    }

    @Test
    fun `getWeatherForecast, local cache is empty`() {
        whenever(
            forecastResultDAO.getMatchedResults(
                safeEq("saigon"),
                safeArgThat(todayMidNight.time, { it == todayMidNight.time }),
                safeEq(1)
            )
        ).thenReturn(Maybe.just(emptyList()))

        localDataSourceImpl.getWeatherForecast("saigon", today, 1)
            .test()
            .assertComplete()

        verify(forecastResultDAO).getMatchedResults(
            safeEq("saigon"),
            safeArgThat(todayMidNight.time, { it == todayMidNight.time }),
            safeEq(1)
        )
    }

    @Test
    fun `getWeatherForecast, local cache is not empty`() {
        val localModel = TestDataProvider.generateForecastResultModel(
            searchQuery = "saigon",
            date = today.time,
            avgTemp = 25.0,
            avgTempUnit = Celsius.code,
            pressure = 1012,
            humidity = 74,
            description = "broken clouds"
        )
        whenever(
            forecastResultDAO.getMatchedResults(
                safeEq("saigon"),
                safeArgThat(todayMidNight.time, { it == todayMidNight.time }),
                safeEq(1)
            )
        ).thenReturn(Maybe.just(listOf(localModel)))

        localDataSourceImpl.getWeatherForecast("saigon", today, 1)
            .test()
            .assertValue {
                assertEquals(1, it.size)
                val weatherForecast = it[0]
                assertEquals(Date(today.time), weatherForecast.date)
                assertEquals(25.0, weatherForecast.averageTemperature.value, 0.0)
                assertEquals(Celsius, weatherForecast.averageTemperature.unit)
                assertEquals(1012, weatherForecast.pressure)
                assertEquals(74, weatherForecast.humidity)
                assertEquals("broken clouds", weatherForecast.description)
                true
            }

        verify(forecastResultDAO).getMatchedResults(
            safeEq("saigon"),
            safeArgThat(todayMidNight.time, { it == todayMidNight.time }),
            safeEq(1)
        )
    }

    @Test
    fun `getWeatherForecast, local cache is not empty but not enough data as requested`() {
        val localModel = TestDataProvider.generateForecastResultModel(
            searchQuery = "saigon",
            date = today.time,
            avgTemp = 25.0,
            avgTempUnit = Celsius.code,
            pressure = 1012,
            humidity = 74,
            description = "broken clouds"
        )
        whenever(
            forecastResultDAO.getMatchedResults(
                safeEq("saigon"),
                safeArgThat(todayMidNight.time, { it == todayMidNight.time }),
                safeEq(7)
            )
        ).thenReturn(Maybe.just(listOf(localModel)))

        localDataSourceImpl.getWeatherForecast("saigon", today, 7)
            .test()
            .assertComplete()

        verify(forecastResultDAO).getMatchedResults(
            safeEq("saigon"),
            safeArgThat(todayMidNight.time, { it == todayMidNight.time }),
            safeEq(7)
        )
    }

    @Test
    fun saveSearchResult() {
        val weatherForecast = TestDataProvider.generateWeatherForecast(
            dateLong = today.time,
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
            assertEquals(today.time, model.date)
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