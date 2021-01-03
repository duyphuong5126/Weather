package com.phuong.myweather.domain.usecase

import com.phuong.myweather.domain.WeatherForecastRepository
import com.phuong.myweather.domain.entity.SearchForecastResult
import com.phuong.myweather.domain.entity.SearchForecastResult.ForecastResults
import com.phuong.myweather.domain.entity.SearchForecastResult.ValidationError
import com.phuong.myweather.domain.entity.SearchForecastResult.InvalidDayRange
import com.phuong.myweather.domain.entity.TemperatureUnit
import com.phuong.myweather.utils.Constants.INVALID_DAYS_RANGE
import com.phuong.myweather.utils.Constants.SEARCH_TERM_LENGTH_NOT_ENOUGH
import io.reactivex.Single
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class GetWeatherForecastUseCaseImpl @Inject constructor(
    private val repository: WeatherForecastRepository
) : GetWeatherForecastUseCase {
    override fun execute(
        rawSearchQuery: String,
        sinceDate: Date,
        daysRange: Int,
        temperatureUnit: TemperatureUnit
    ): Single<SearchForecastResult> {
        val searchQuery = rawSearchQuery.trim().toLowerCase(Locale.getDefault())
        return when {
            searchQuery.length < MINIMUM_CHARACTERS -> {
                Single.just(ValidationError(SEARCH_TERM_LENGTH_NOT_ENOUGH))
            }

            daysRange <= 0 -> {
                Single.just(InvalidDayRange(INVALID_DAYS_RANGE))
            }

            else -> {
                repository.getDailyForecast(searchQuery, sinceDate, daysRange, temperatureUnit)
                    .map(::ForecastResults)
            }
        }
    }

    companion object {
        private const val MINIMUM_CHARACTERS = 3
    }
}