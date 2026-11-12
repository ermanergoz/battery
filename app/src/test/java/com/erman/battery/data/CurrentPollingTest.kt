package com.erman.battery.data

import io.reactivex.rxjava3.schedulers.TestScheduler
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.concurrent.TimeUnit

class CurrentPollingTest {

    private val scheduler = TestScheduler()

    @Test
    fun readsRightAwayAndThenEveryPeriod() {
        var reading = 100
        val observer = pollCurrent({ reading++ }, 1_000, scheduler).test()

        scheduler.triggerActions()
        observer.assertValues(CurrentReading(100))

        scheduler.advanceTimeBy(2, TimeUnit.SECONDS)
        observer.assertValues(CurrentReading(100), CurrentReading(101), CurrentReading(102))
    }

    @Test
    fun unsupportedReadingIsWrappedInsteadOfBreakingTheStream() {
        val observer = pollCurrent({ null }, 1_000, scheduler).test()

        scheduler.advanceTimeBy(1, TimeUnit.SECONDS)

        observer.assertValues(CurrentReading(null), CurrentReading(null))
        observer.assertNoErrors()
    }

    @Test
    fun stopsPollingWhenDisposed() {
        var reads = 0
        val observer = pollCurrent({ reads++ }, 1_000, scheduler).test()
        scheduler.triggerActions()

        observer.dispose()
        scheduler.advanceTimeBy(5, TimeUnit.SECONDS)

        assertEquals(1, reads)
    }
}
