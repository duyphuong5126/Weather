package com.phuong.myweather.datasource.local.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ForecastResultModel::class],
    version = 1
)
abstract class WeatherDB : RoomDatabase() {
    abstract fun getForecastResultDAO(): ForecastResultDAO
}