package com.phuong.myweather.datasource.remote

import com.phuong.myweather.datasource.remote.apiresponse.ForecastResponseData
import com.phuong.myweather.datasource.remote.apiservice.WeatherApiService
import com.phuong.myweather.datasource.remote.error.ApiError
import com.phuong.myweather.datasource.remote.error.transformer.ErrorResponseTransformer
import io.reactivex.Single
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class WeatherForecastRemoteDataSourceImpl @Inject constructor(
    private val apiService: WeatherApiService,
    private val errorResponseTransformer: ErrorResponseTransformer
) : WeatherForecastRemoteDataSource {
    override fun getDailyForecast(
        query: String,
        daysRange: Int,
        temperatureUnit: String
    ): Single<ForecastResponseData> {
        return fetchDailyForecast(query, daysRange, temperatureUnit)
    }

    private fun fetchDailyForecast(
        query: String,
        daysRange: Int,
        temperatureUnit: String
    ): Single<ForecastResponseData> {
        return Single.create { responseEmitter ->
            apiService.getWeatherForecast(
                queryString = query,
                daysRange = daysRange,
                units = temperatureUnit
            ).enqueue(object : Callback<ForecastResponseData> {
                override fun onResponse(
                    call: Call<ForecastResponseData>,
                    response: Response<ForecastResponseData>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let(responseEmitter::onSuccess)
                            ?: responseEmitter.onError(ApiError("Null response body"))
                    } else {
                        val errorMessage = response.errorBody()?.let {
                            errorResponseTransformer.transform(it)?.message
                        } ?: "Unknown error"
                        responseEmitter.onError(ApiError(errorMessage))
                    }
                }

                override fun onFailure(call: Call<ForecastResponseData>, t: Throwable) {
                    responseEmitter.onError(ApiError("Failed to get weather forecast", t))
                }
            })
        }
    }
}