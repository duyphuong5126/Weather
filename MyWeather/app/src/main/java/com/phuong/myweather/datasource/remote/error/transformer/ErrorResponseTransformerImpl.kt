package com.phuong.myweather.datasource.remote.error.transformer

import com.phuong.myweather.datasource.remote.error.ErrorResponse
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit

class ErrorResponseTransformerImpl(retrofit: Retrofit) : ErrorResponseTransformer {
    private val errorResponseConverter: Converter<ResponseBody, ErrorResponse> =
        retrofit.responseBodyConverter(
            ErrorResponse::class.java, arrayOfNulls(0)
        )

    override fun transform(errorBody: ResponseBody): ErrorResponse? {
        return errorResponseConverter.convert(errorBody)
    }
}