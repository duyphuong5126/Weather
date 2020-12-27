package com.phuong.myweather.utils

import com.phuong.myweather.datasource.remote.error.ApiError
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.UnknownHostException

fun Throwable.isConnectionError(): Boolean {
    return (this is UnknownHostException ||
            this is NoRouteToHostException ||
            this is ConnectException) ||
            (this is ApiError && (this.cause is UnknownHostException ||
                    this.cause is NoRouteToHostException ||
                    this.cause is ConnectException))
}