package com.phuong.myweather.utils

object Constants {
    const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    const val API_KEY = "60c6fbeb4b93ac653c492ba806fc346d"

    /*---------------------------DB constants---------------------------*/
    const val ID = "_id"

    const val DB_NAME = "weather_forecast"

    const val TABLE_FORECAST_RESULT = "forecast_result"

    const val COLUMN_SEARCH_QUERY = "search_query"
    const val COLUMN_DATE = "date"
    const val COLUMN_AVG_TEMP = "avg_temp"
    const val COLUMN_AVG_TEMP_UNIT = "avg_temp_unit"
    const val COLUMN_PRESSURE = "pressure"
    const val COLUMN_HUMIDITY = "humidity"
    const val COLUMN_DESCRIPTION = "description"

    /*---------------------------DB constants---------------------------*/


    /*---------------------------Error constants---------------------------*/
    const val SEARCH_TERM_LENGTH_NOT_ENOUGH = "Search term must be from 3 characters or above"
    const val NETWORK_ERROR = "Network error!"
    const val WEATHER_FORECAST_GENERAL_ERROR = "Failed to get weather data!"
    /*---------------------------Error constants---------------------------*/
}