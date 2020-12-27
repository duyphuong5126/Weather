package com.phuong.myweather.domain.usecase

import com.phuong.myweather.domain.WeatherForecastRepository
import com.phuong.myweather.domain.entity.SearchForecastResult
import com.phuong.myweather.domain.entity.SearchForecastResult.ForecastResults
import com.phuong.myweather.domain.entity.SearchForecastResult.ValidationError
import com.phuong.myweather.domain.entity.TemperatureUnit
import com.phuong.myweather.utils.Constants.SEARCH_TERM_LENGTH_NOT_ENOUGH
import io.reactivex.Single
import java.util.Locale
import javax.inject.Inject

class GetWeatherForecastUseCaseImpl @Inject constructor(
    private val repository: WeatherForecastRepository
) : GetWeatherForecastUseCase {
    override fun execute(
        rawSearchQuery: String,
        daysRange: Int,
        temperatureUnit: TemperatureUnit
    ): Single<SearchForecastResult> {
        val searchQuery = rawSearchQuery.trim().toLowerCase(Locale.getDefault())
        return if (searchQuery.length < MINIMUM_CHARACTERS) {
            Single.just(ValidationError(SEARCH_TERM_LENGTH_NOT_ENOUGH))
        } else {
            repository.getDailyForecast(searchQuery, daysRange, temperatureUnit)
                .map(::ForecastResults)
        }
    }

    companion object {
        private const val MINIMUM_CHARACTERS = 3
    }
}