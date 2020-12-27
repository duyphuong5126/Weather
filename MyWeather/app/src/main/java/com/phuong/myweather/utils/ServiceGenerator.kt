package com.phuong.myweather.utils

import com.google.gson.GsonBuilder
import com.phuong.myweather.datasource.remote.error.transformer.ErrorResponseTransformer
import com.phuong.myweather.datasource.remote.error.transformer.ErrorResponseTransformerImpl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ServiceGenerator(baseUrl: String) {
    private val mRetrofitBuilder = Retrofit.Builder().addConverterFactory(
        GsonConverterFactory.create(
            GsonBuilder().create()
        )
    )

    private var mRetrofit: Retrofit? = null
    private val mLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    private val mHttpClientBuilder = OkHttpClient.Builder().addInterceptor(mLoggingInterceptor)
        .connectTimeout(30000, TimeUnit.MILLISECONDS)
        .readTimeout(30000, TimeUnit.MILLISECONDS)
        .writeTimeout(30000, TimeUnit.MILLISECONDS)

    init {
        mHttpClientBuilder.addInterceptor(mLoggingInterceptor)
        mRetrofitBuilder.client(mHttpClientBuilder.build())
        mRetrofitBuilder.baseUrl(baseUrl)
        mRetrofit = mRetrofitBuilder.build()
    }

    fun <S> createService(serviceClass: Class<S>): S {
        return mRetrofit!!.create(serviceClass)
    }

    fun createErrorResponseTransformer(): ErrorResponseTransformer {
        return ErrorResponseTransformerImpl(mRetrofit!!)
    }
}