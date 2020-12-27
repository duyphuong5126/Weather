package com.phuong.myweather.domain.entity

enum class TemperatureUnit(val code: String, val symbol: String) {
    Kelvin("standard", "°K"),
    Celsius("metric", "°C"),
    Fahrenheit("imperial", "°F");

    companion object {
        @JvmStatic
        fun fromUnitCode(code: String): TemperatureUnit {
            return values().firstOrNull { it.code == code } ?: Kelvin
        }
    }
}