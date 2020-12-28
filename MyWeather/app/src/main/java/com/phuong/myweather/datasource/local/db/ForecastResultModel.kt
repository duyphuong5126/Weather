package com.phuong.myweather.datasource.local.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import com.phuong.myweather.utils.Constants.COLUMN_AVG_TEMP
import com.phuong.myweather.utils.Constants.COLUMN_AVG_TEMP_UNIT
import com.phuong.myweather.utils.Constants.COLUMN_DATE
import com.phuong.myweather.utils.Constants.COLUMN_DESCRIPTION
import com.phuong.myweather.utils.Constants.COLUMN_HUMIDITY
import com.phuong.myweather.utils.Constants.COLUMN_PRESSURE
import com.phuong.myweather.utils.Constants.COLUMN_SEARCH_QUERY
import com.phuong.myweather.utils.Constants.TABLE_FORECAST_RESULT

@Entity(
    tableName = TABLE_FORECAST_RESULT,
    primaryKeys = [COLUMN_DATE, COLUMN_SEARCH_QUERY],
    indices = [Index(value = [COLUMN_DATE]), Index(value = [COLUMN_SEARCH_QUERY])],
)
class ForecastResultModel(
    @ColumnInfo(name = COLUMN_DATE) val date: Long,
    @ColumnInfo(name = COLUMN_AVG_TEMP) val avgTemp: Double,
    @ColumnInfo(name = COLUMN_AVG_TEMP_UNIT) val avgTempUnit: String,
    @ColumnInfo(name = COLUMN_PRESSURE) val pressure: Int,
    @ColumnInfo(name = COLUMN_HUMIDITY) val humidity: Int,
    @ColumnInfo(name = COLUMN_DESCRIPTION) val description: String,
    @ColumnInfo(name = COLUMN_SEARCH_QUERY) val searchQuery: String
)