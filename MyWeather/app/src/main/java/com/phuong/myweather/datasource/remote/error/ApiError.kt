package com.phuong.myweather.datasource.remote.error

class ApiError(errorMessage: String, error: Throwable? = null) : Exception(errorMessage, error)