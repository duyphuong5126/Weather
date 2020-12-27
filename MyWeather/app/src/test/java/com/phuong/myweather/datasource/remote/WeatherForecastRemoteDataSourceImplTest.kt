package com.phuong.myweather.datasource.remote

import com.phuong.myweather.TestDataProvider
import com.phuong.myweather.any
import com.phuong.myweather.datasource.remote.apiresponse.ForecastResponseData
import com.phuong.myweather.datasource.remote.apiservice.WeatherApiService
import com.phuong.myweather.datasource.remote.error.ApiError
import com.phuong.myweather.datasource.remote.error.ErrorResponse
import com.phuong.myweather.datasource.remote.error.transformer.ErrorResponseTransformer
import com.phuong.myweather.domain.entity.TemperatureUnit.Celsius
import com.phuong.myweather.utils.Constants
import io.reactivex.disposables.CompositeDisposable
import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.Buffer
import okio.BufferedSource
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException
import org.mockito.Mockito.`when` as whenever

class WeatherForecastRemoteDataSourceImplTest {
    private val apiService = mock(WeatherApiService::class.java)
    private val errorResponseTransformer = mock(ErrorResponseTransformer::class.java)
    private val compositeDisposable = CompositeDisposable()

    @Captor
    lateinit var callCaptor: ArgumentCaptor<Callback<ForecastResponseData>>

    private val mockCall = mock(MockCall::class.java)

    private val remoteDataSourceImpl = WeatherForecastRemoteDataSourceImpl(
        apiService, errorResponseTransformer
    )

    @Before
    fun init() {
        MockitoAnnotations.initMocks(this)
    }

    @After
    fun cleanUp() {
        compositeDisposable.clear()
    }

    @Test
    fun `getDailyForecast, request timeout`() {
        whenever(apiService.getWeatherForecast(Constants.API_KEY, "saigon", 1, Celsius.code))
            .thenReturn(mockCall)

        remoteDataSourceImpl.getDailyForecast("saigon", 1, Celsius.code)
            .subscribe({
            }, {
                assertTrue(it is ApiError && it.cause is SocketTimeoutException)
            }).let(compositeDisposable::add)

        verify(apiService).getWeatherForecast(Constants.API_KEY, "saigon", 1, Celsius.code)
        verify(mockCall).enqueue(callCaptor.capture())

        callCaptor.value.onFailure(mockCall, SocketTimeoutException())

        verify(apiService).getWeatherForecast(Constants.API_KEY, "saigon", 1, Celsius.code)
        verify(errorResponseTransformer, never()).transform(any(ResponseBody::class.java))
    }

    @Test
    fun `getDailyForecast, city not found`() {

        whenever(apiService.getWeatherForecast(Constants.API_KEY, "saigon", 1, Celsius.code))
            .thenReturn(mockCall)

        val responseBody = CityNotFoundResponseBody()

        whenever(errorResponseTransformer.transform(responseBody)).thenReturn(
            ErrorResponse(
                "404",
                CITY_NOT_FOUND
            )
        )

        remoteDataSourceImpl.getDailyForecast("saigon", 1, Celsius.code)
            .subscribe({
            }, {
                assertTrue(it is ApiError && it.cause == null && it.message == CITY_NOT_FOUND)
            }).let(compositeDisposable::add)

        verify(apiService).getWeatherForecast(Constants.API_KEY, "saigon", 1, Celsius.code)
        verify(mockCall).enqueue(callCaptor.capture())

        callCaptor.value.onResponse(mockCall, Response.error(404, responseBody))

        verify(apiService).getWeatherForecast(Constants.API_KEY, "saigon", 1, Celsius.code)
        verify(errorResponseTransformer).transform(responseBody)
    }

    @Test
    fun `getDailyForecast, forecast is fetched successfully with data`() {
        val responseData = ForecastResponseData(
            listOf(
                TestDataProvider.generateWeatherForecastData(
                    dateTime = 1609041600,
                    minTemp = 20.0,
                    maxTemp = 30.0,
                    pressure = 1012,
                    humidity = 74,
                    weatherDescriptions = listOf("broken clouds", "light rain")
                )
            )
        )

        whenever(apiService.getWeatherForecast(Constants.API_KEY, "saigon", 1, Celsius.code))
            .thenReturn(mockCall)

        remoteDataSourceImpl.getDailyForecast("saigon", 1, Celsius.code)
            .subscribe({
                assertEquals(responseData, it)
            }, {

            }).let(compositeDisposable::add)

        verify(apiService).getWeatherForecast(Constants.API_KEY, "saigon", 1, Celsius.code)
        verify(mockCall).enqueue(callCaptor.capture())

        callCaptor.value.onResponse(mockCall, Response.success(responseData))

        verify(apiService).getWeatherForecast(Constants.API_KEY, "saigon", 1, Celsius.code)
        verify(errorResponseTransformer, never()).transform(any(ResponseBody::class.java))
    }

    @Test
    fun `getDailyForecast, forecast is fetched successfully with null body`() {

        whenever(apiService.getWeatherForecast(Constants.API_KEY, "saigon", 1, Celsius.code))
            .thenReturn(mockCall)

        remoteDataSourceImpl.getDailyForecast("saigon", 1, Celsius.code)
            .subscribe({

            }, {
                assertTrue(it is ApiError && it.message == "Null response body")
            }).let(compositeDisposable::add)

        verify(apiService).getWeatherForecast(Constants.API_KEY, "saigon", 1, Celsius.code)
        verify(mockCall).enqueue(callCaptor.capture())

        callCaptor.value.onResponse(mockCall, Response.success(null))

        verify(apiService).getWeatherForecast(Constants.API_KEY, "saigon", 1, Celsius.code)
        verify(errorResponseTransformer, never()).transform(any(ResponseBody::class.java))
    }

    private interface MockCall : Call<ForecastResponseData>

    private class CityNotFoundResponseBody : ResponseBody() {
        override fun contentLength(): Long {
            return 1024
        }

        override fun contentType(): MediaType? {
            return null
        }

        override fun source(): BufferedSource {
            return Buffer()
        }
    }

    companion object {
        private const val CITY_NOT_FOUND = "city not found"
    }
}