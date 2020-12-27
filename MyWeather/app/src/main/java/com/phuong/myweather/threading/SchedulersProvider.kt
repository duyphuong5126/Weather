package com.phuong.myweather.threading

import io.reactivex.Scheduler

interface SchedulersProvider {
    fun io(): Scheduler

    fun mainThread(): Scheduler
}