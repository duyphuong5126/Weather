package com.phuong.myweather.domain.entity

import org.junit.Test
import org.junit.Assert.assertEquals

class TemperatureUnitTest {
    @Test
    fun fromUnitCode() {
        assertEquals(TemperatureUnit.Kelvin, TemperatureUnit.fromUnitCode("standard"))
        assertEquals(TemperatureUnit.Celsius, TemperatureUnit.fromUnitCode("metric"))
        assertEquals(TemperatureUnit.Fahrenheit, TemperatureUnit.fromUnitCode("imperial"))
        assertEquals(TemperatureUnit.Kelvin, TemperatureUnit.fromUnitCode("fdjbnvcj"))
    }
}