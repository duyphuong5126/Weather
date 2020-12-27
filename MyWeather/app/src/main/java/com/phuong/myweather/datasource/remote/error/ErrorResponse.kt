package com.phuong.myweather.datasource.remote.error

import com.google.gson.annotations.SerializedName

data class ErrorResponse(
    @SerializedName("cod") val code: String,
    @SerializedName("message") val message: String
)