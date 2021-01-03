package com.phuong.myweather.domain.usecase

import com.phuong.myweather.TestDataProvider
import com.phuong.myweather.domain.WeatherForecastRepository
import com.phuong.myweather.domain.entity.SearchForecastResult.ValidationError
import com.phuong.myweather.domain.entity.SearchForecastResult.InvalidDayRange
import com.phuong.myweather.domain.entity.SearchForecastResult.ForecastResults
import com.phuong.myweather.domain.entity.TemperatureUnit
import com.phuong.myweather.domain.entity.TemperatureUnit.Celsius
import com.phuong.myweather.safeAnyInt
import com.phuong.myweather.safeAnyString
import com.phuong.myweather.any
import com.phuong.myweather.utils.Constants.INVALID_DAYS_RANGE
import com.phuong.myweather.utils.Constants.SEARCH_TERM_LENGTH_NOT_ENOUGH
import io.reactivex.Single
import org.junit.Test
import org.junit.Assert.assertTrue
import org.junit.Assert.assertEquals
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import org.mockito.Mockito.`when` as whenever

class GetWeatherForecastUseCaseImplTest {
    private val repository = mock(WeatherForecastRepository::class.java)

    private val useCaseImpl = GetWeatherForecastUseCaseImpl(repository)

    private val today: Date

    init {
        val dateFormat = SimpleDateFormat("EEE',' dd MMM yyyy HH':'mm':'ss 'GMT'", Locale.US)
        today = dateFormat.parse("Sun, 03 Jan 2021 09:09:09 GMT")!!
    }

    @Test
    fun `execute, raw search query's length is not enough`() {
        useCaseImpl.execute("s", today, 1, Celsius)
            .test()
            .assertValue {
                assertTrue(it is ValidationError)
                assertEquals(SEARCH_TERM_LENGTH_NOT_ENOUGH, (it as ValidationError).message)
                true
            }

        verify(repository, never()).getDailyForecast(
            safeAnyString(),
            any(Date::class.java),
            safeAnyInt(),
            any(TemperatureUnit::class.java)
        )
    }

    @Test
    fun `execute, days range is 0`() {
        useCaseImpl.execute("saigon", today, 0, Celsius)
            .test()
            .assertValue {
                assertTrue(it is InvalidDayRange)
                assertEquals(INVALID_DAYS_RANGE, (it as InvalidDayRange).message)
                true
            }

        verify(repository, never()).getDailyForecast(
            safeAnyString(),
            any(Date::class.java),
            safeAnyInt(),
            any(TemperatureUnit::class.java)
        )
    }

    @Test
    fun `execute, raw search query contains both upper case and lower case chars`() {
        whenever(repository.getDailyForecast("saigon", today, 1, Celsius))
            .thenReturn(Single.just(listOf(Day1ForecastCelsius)))

        useCaseImpl.execute("SaIgOn", today, 1, Celsius)
            .test()
            .assertValue {
                assertTrue(it is ForecastResults)
                assertEquals(Day1ForecastCelsius, (it as ForecastResults).weatherForecasts[0])
                true
            }

        verify(repository).getDailyForecast("saigon", today, 1, Celsius)
    }

    @Test
    fun `execute, raw search query contains leading, middle and trailing whitespace`() {
        whenever(repository.getDailyForecast("ha noi", today, 1, Celsius))
            .thenReturn(Single.just(listOf(Day1ForecastCelsius)))

        useCaseImpl.execute(" Ha Noi ", today, 1, Celsius)
            .test()
            .assertValue {
                assertTrue(it is ForecastResults)
                assertEquals(Day1ForecastCelsius, (it as ForecastResults).weatherForecasts[0])
                true
            }

        verify(repository).getDailyForecast("ha noi", today, 1, Celsius)
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