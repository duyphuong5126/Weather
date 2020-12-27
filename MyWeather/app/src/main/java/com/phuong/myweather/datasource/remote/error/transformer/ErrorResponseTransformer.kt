package com.phuong.myweather.datasource.remote.error.transformer

import com.phuong.myweather.datasource.remote.error.ErrorResponse
import okhttp3.ResponseBody

interface ErrorResponseTransformer {
    fun transform(errorBody: ResponseBody): ErrorResponse?
}