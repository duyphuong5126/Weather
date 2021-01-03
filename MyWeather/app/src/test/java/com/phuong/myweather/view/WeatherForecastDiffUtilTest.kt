package com.phuong.myweather.view

import com.phuong.myweather.TestDataProvider
import org.junit.Test
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse

class WeatherForecastDiffUtilTest {
    private val forecastList1 = arrayListOf<WeatherForecastUiModel>()
    private val forecastList2 = arrayListOf<WeatherForecastUiModel>()

    init {
        forecastList1.add(
            TestDataProvider.generateWeatherForecastUiModel(
                date = "Sun, 03 Jan 2021",
                averageTemperature = "29°C",
                pressure = 1014,
                humidity = "60%",
                description = "light rain"
            )
        )
        forecastList1.add(
            TestDataProvider.generateWeatherForecastUiModel(
                date = "Mon, 04 Jan 2021",
                averageTemperature = "32°C",
                pressure = 1015,
                humidity = "50%",
                description = "sky is clear"
            )
        )

        forecastList2.add(
            TestDataProvider.generateWeatherForecastUiModel(
                date = "Sun, 03 Jan 2021",
                averageTemperature = "29°C",
                pressure = 1014,
                humidity = "60%",
                description = "light rain"
            )
        )
        forecastList2.add(
            TestDataProvider.generateWeatherForecastUiModel(
                date = "Mon, 04 Jan 2021",
                averageTemperature = "32°C",
                pressure = 1015,
                humidity = "50%",
                description = "sky is clear"
            )
        )
        forecastList2.add(
            TestDataProvider.generateWeatherForecastUiModel(
                date = "Tue, 05 Jan 2021",
                averageTemperature = "30°C",
                pressure = 1010,
                humidity = "55%",
                description = "few clouds"
            )
        )
        forecastList2.add(
            TestDataProvider.generateWeatherForecastUiModel(
                date = "Wed, 06 Jan 2021",
                averageTemperature = "28°C",
                pressure = 1010,
                humidity = "70%",
                description = "light rain"
            )
        )
    }

    @Test
    fun test() {
        val diffUtil = WeatherForecastDiffUtil(forecastList1, forecastList2)
        assertEquals(2, diffUtil.oldListSize)
        assertEquals(4, diffUtil.newListSize)

        assertTrue(diffUtil.areItemsTheSame(0, 0))
        assertTrue(diffUtil.areItemsTheSame(1, 1))
        assertTrue(diffUtil.areContentsTheSame(0, 0))
        assertTrue(diffUtil.areContentsTheSame(1, 1))

        assertFalse(diffUtil.areItemsTheSame(0, 1))
        assertFalse(diffUtil.areItemsTheSame(1, 0))
        assertFalse(diffUtil.areContentsTheSame(0, 1))
        assertFalse(diffUtil.areContentsTheSame(0, 2))
        assertFalse(diffUtil.areContentsTheSame(0, 3))
        assertFalse(diffUtil.areContentsTheSame(1, 0))
        assertFalse(diffUtil.areContentsTheSame(1, 2))
        assertFalse(diffUtil.areContentsTheSame(1, 3))
    }
}