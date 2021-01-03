package com.phuong.myweather.utils

import com.phuong.myweather.datasource.remote.error.ApiError
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.UnknownHostException
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

fun Throwable.isConnectionError(): Boolean {
    return (this is UnknownHostException ||
            this is NoRouteToHostException ||
            this is ConnectException) ||
            (this is ApiError && (this.cause is UnknownHostException ||
                    this.cause is NoRouteToHostException ||
                    this.cause is ConnectException))
}

fun Date.getMidnight(): Date {
    val calendar = GregorianCalendar()
    calendar.time = this
    calendar.set(Calendar.HOUR_OF_DAY, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    return calendar.time
}