package com.phuong.myweather.di.module

import android.app.Application
import androidx.room.Room
import com.phuong.myweather.datasource.local.db.ForecastResultDAO
import com.phuong.myweather.datasource.local.db.WeatherDB
import com.phuong.myweather.utils.Constants.DB_NAME
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun providesWeatherDB(application: Application): WeatherDB {
        return Room.databaseBuilder(application.applicationContext, WeatherDB::class.java, DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun providesForecastResultDAO(weatherDB: WeatherDB): ForecastResultDAO {
        return weatherDB.getForecastResultDAO()
    }
}