package com.erman.battery.data

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import java.util.concurrent.TimeUnit

/** RxJava streams cannot carry null, so a current the device does not report is wrapped */
data class CurrentReading(val microAmps: Int?)

fun pollCurrent(read: () -> Int?, periodMillis: Long, scheduler: Scheduler): Observable<CurrentReading> =
    Observable.interval(0, periodMillis, TimeUnit.MILLISECONDS, scheduler)
        .map { CurrentReading(read()) }
