package com.phuong.myweather.datasource.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.phuong.myweather.utils.Constants.COLUMN_DATE
import com.phuong.myweather.utils.Constants.TABLE_FORECAST_RESULT
import com.phuong.myweather.utils.Constants.COLUMN_SEARCH_QUERY
import io.reactivex.Maybe

@Dao
interface ForecastResultDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(resultList: List<ForecastResultModel>): List<Long>

    @Query("select * from $TABLE_FORECAST_RESULT where $COLUMN_SEARCH_QUERY = :searchQuery order by $COLUMN_DATE asc limit :daysRange")
    fun getMatchedResults(searchQuery: String, daysRange: Int): Maybe<List<ForecastResultModel>>
}