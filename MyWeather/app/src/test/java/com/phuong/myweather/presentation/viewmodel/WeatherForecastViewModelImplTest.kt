package com.phuong.myweather.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jraska.livedata.TestObserver
import com.phuong.myweather.TestDataProvider
import com.phuong.myweather.TestSchedulersProvider
import com.phuong.myweather.domain.entity.SearchForecastResult.ForecastResults
import com.phuong.myweather.domain.entity.SearchForecastResult.ValidationError
import com.phuong.myweather.domain.entity.TemperatureUnit.Celsius
import com.phuong.myweather.domain.entity.TemperatureUnit.Fahrenheit
import com.phuong.myweather.domain.entity.TemperatureUnit.Kelvin
import com.phuong.myweather.domain.usecase.GetWeatherForecastUseCase
import com.phuong.myweather.utils.Constants.NETWORK_ERROR
import com.phuong.myweather.utils.Constants.SEARCH_TERM_LENGTH_NOT_ENOUGH
import com.phuong.myweather.utils.Constants.WEATHER_FORECAST_GENERAL_ERROR
import com.phuong.myweather.view.WeatherForecastUiModel
import io.reactivex.Single
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.rules.TestRule
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import java.net.UnknownHostException
import org.mockito.Mockito.`when` as whenever

class WeatherForecastViewModelImplTest {
    private val getWeatherForecastUseCase = mock(GetWeatherForecastUseCase::class.java)
    private val schedulersProvider = TestSchedulersProvider()

    private val viewModelImpl = WeatherForecastViewModelImpl(
        getWeatherForecastUseCase,
        schedulersProvider
    )

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Test
    fun `getWeatherForecast, use-case returns normal data using Celsius unit`() {
        val oneDayForecast = listOf(Day1ForecastCelsius)
        whenever(getWeatherForecastUseCase.execute(SEARCH_TERM, 1, Celsius))
            .thenReturn(Single.just(ForecastResults(oneDayForecast)))

        viewModelImpl.getWeatherForecast(SEARCH_TERM, 1, Celsius)

        verify(getWeatherForecastUseCase).execute(SEARCH_TERM, 1, Celsius)

        TestObserver.test(viewModelImpl.searchValidationErrorLiveData)
            .assertHasValue()
            .assertValue(null)

        TestObserver.test(viewModelImpl.searchErrorLiveData)
            .assertHasValue()
            .assertValue(null)

        TestObserver.test(viewModelImpl.weatherForecastLiveData)
            .assertHasValue()
            .assertValue {
                val day1Item = it[0]
                assertEquals(DAY_1, day1Item.date)
                assertEquals("31°C", day1Item.averageTemperature)
                assertEquals("1012", day1Item.pressure.toString())
                assertEquals("70%", day1Item.humidity)
                assertEquals("scattered clouds", day1Item.description)
                true
            }
    }

    @Test
    fun `getWeatherForecast, use-case returns normal data using Fahrenheit unit`() {
        val oneDayForecast = listOf(Day1ForecastFahrenheit)
        whenever(getWeatherForecastUseCase.execute(SEARCH_TERM, 1, Fahrenheit))
            .thenReturn(Single.just(ForecastResults(oneDayForecast)))

        viewModelImpl.getWeatherForecast(SEARCH_TERM, 1, Fahrenheit)

        verify(getWeatherForecastUseCase).execute(SEARCH_TERM, 1, Fahrenheit)

        TestObserver.test(viewModelImpl.searchValidationErrorLiveData)
            .assertHasValue()
            .assertValue(null)

        TestObserver.test(viewModelImpl.searchErrorLiveData)
            .assertHasValue()
            .assertValue(null)

        TestObserver.test(viewModelImpl.weatherForecastLiveData)
            .assertHasValue()
            .assertValue {
                val day1Item = it[0]
                assertEquals(DAY_1, day1Item.date)
                assertEquals("88°F", day1Item.averageTemperature)
                assertEquals("1012", day1Item.pressure.toString())
                assertEquals("70%", day1Item.humidity)
                assertEquals("scattered clouds", day1Item.description)
                true
            }
    }

    @Test
    fun `getWeatherForecast, use-case returns normal data using Kelvin unit`() {
        val oneDayForecast = listOf(Day1ForecastKelvin)
        whenever(getWeatherForecastUseCase.execute(SEARCH_TERM, 1, Kelvin))
            .thenReturn(Single.just(ForecastResults(oneDayForecast)))

        viewModelImpl.getWeatherForecast(SEARCH_TERM, 1, Kelvin)

        verify(getWeatherForecastUseCase).execute(SEARCH_TERM, 1, Kelvin)

        TestObserver.test(viewModelImpl.searchValidationErrorLiveData)
            .assertHasValue()
            .assertValue(null)

        TestObserver.test(viewModelImpl.searchErrorLiveData)
            .assertHasValue()
            .assertValue(null)

        TestObserver.test(viewModelImpl.weatherForecastLiveData)
            .assertHasValue()
            .assertValue {
                val day1Item = it[0]
                assertEquals(DAY_1, day1Item.date)
                assertEquals("304°K", day1Item.averageTemperature)
                assertEquals("1012", day1Item.pressure.toString())
                assertEquals("70%", day1Item.humidity)
                assertEquals("scattered clouds", day1Item.description)
                true
            }
    }

    @Test
    fun `getWeatherForecast, use-case returns validation error`() {
        whenever(getWeatherForecastUseCase.execute(BAD_SEARCH_TERM, DEFAULT_DAYS_RANGE, Celsius))
            .thenReturn(Single.just(ValidationError(SEARCH_TERM_LENGTH_NOT_ENOUGH)))

        viewModelImpl.getWeatherForecast(BAD_SEARCH_TERM, DEFAULT_DAYS_RANGE, Celsius)

        verify(getWeatherForecastUseCase).execute(BAD_SEARCH_TERM, DEFAULT_DAYS_RANGE, Celsius)

        TestObserver.test(viewModelImpl.searchValidationErrorLiveData)
            .assertHasValue()
            .assertValue(SEARCH_TERM_LENGTH_NOT_ENOUGH)

        TestObserver.test(viewModelImpl.searchErrorLiveData)
            .assertHasValue()
            .assertValue(null)

        TestObserver.test(viewModelImpl.weatherForecastLiveData)
            .assertHasValue()
            .assertValue(List<WeatherForecastUiModel>::isEmpty)
    }

    @Test
    fun `getWeatherForecast, use-case fails due to network connection error`() {
        val networkError = TestDataProvider.generateApiError("", UnknownHostException())
        whenever(getWeatherForecastUseCase.execute(SEARCH_TERM, DEFAULT_DAYS_RANGE, Celsius))
            .thenReturn(Single.error(networkError))

        viewModelImpl.getWeatherForecast(SEARCH_TERM, DEFAULT_DAYS_RANGE, Celsius)

        verify(getWeatherForecastUseCase).execute(SEARCH_TERM, DEFAULT_DAYS_RANGE, Celsius)

        TestObserver.test(viewModelImpl.searchValidationErrorLiveData)
            .assertHasValue()
            .assertValue(null)

        TestObserver.test(viewModelImpl.searchErrorLiveData)
            .assertHasValue()
            .assertValue(NETWORK_ERROR)

        TestObserver.test(viewModelImpl.weatherForecastLiveData)
            .assertHasValue()
            .assertValue(List<WeatherForecastUiModel>::isEmpty)
    }

    @Test
    fun `getWeatherForecast, use-case fails due to invalid city error`() {
        val invalidCityError = TestDataProvider.generateApiError("city not found", null)
        whenever(getWeatherForecastUseCase.execute(SEARCH_TERM, DEFAULT_DAYS_RANGE, Celsius))
            .thenReturn(Single.error(invalidCityError))

        viewModelImpl.getWeatherForecast(SEARCH_TERM, DEFAULT_DAYS_RANGE, Celsius)

        verify(getWeatherForecastUseCase).execute(SEARCH_TERM, DEFAULT_DAYS_RANGE, Celsius)

        TestObserver.test(viewModelImpl.searchValidationErrorLiveData)
            .assertHasValue()
            .assertValue(null)

        TestObserver.test(viewModelImpl.searchErrorLiveData)
            .assertHasValue()
            .assertValue("city not found")

        TestObserver.test(viewModelImpl.weatherForecastLiveData)
            .assertHasValue()
            .assertValue(List<WeatherForecastUiModel>::isEmpty)
    }

    @Test
    fun `getWeatherForecast, use-case fails due to unknown error`() {
        val randomError = NullPointerException()
        whenever(getWeatherForecastUseCase.execute(SEARCH_TERM, DEFAULT_DAYS_RANGE, Celsius))
            .thenReturn(Single.error(randomError))

        viewModelImpl.getWeatherForecast(SEARCH_TERM, DEFAULT_DAYS_RANGE, Celsius)

        verify(getWeatherForecastUseCase).execute(SEARCH_TERM, DEFAULT_DAYS_RANGE, Celsius)

        TestObserver.test(viewModelImpl.searchValidationErrorLiveData)
            .assertHasValue()
            .assertValue(null)

        TestObserver.test(viewModelImpl.searchErrorLiveData)
            .assertHasValue()
            .assertValue(WEATHER_FORECAST_GENERAL_ERROR)

        TestObserver.test(viewModelImpl.weatherForecastLiveData)
            .assertHasValue()
            .assertValue(List<WeatherForecastUiModel>::isEmpty)
    }

    companion object {
        private const val BAD_SEARCH_TERM = "sa"

        private const val SEARCH_TERM = "saigon"

        private const val DEFAULT_DAYS_RANGE = 7

        private const val DAY_1 = "Sun, 27 Dec 2020"
        private val Day1ForecastCelsius = TestDataProvider.generateWeatherForecast(
            dateLong = 1609041600000,
            avgTemp = 31.4,
            avgTempUnit = Celsius.code,
            pressure = 1012,
            humidity = 70,
            description = "scattered clouds"
        )
        private val Day1ForecastFahrenheit = TestDataProvider.generateWeatherForecast(
            dateLong = 1609041600000,
            avgTemp = 88.52,
            avgTempUnit = Fahrenheit.code,
            pressure = 1012,
            humidity = 70,
            description = "scattered clouds"
        )
        private val Day1ForecastKelvin = TestDataProvider.generateWeatherForecast(
            dateLong = 1609041600000,
            avgTemp = 304.55,
            avgTempUnit = Kelvin.code,
            pressure = 1012,
            humidity = 70,
            description = "scattered clouds"
        )
    }
}