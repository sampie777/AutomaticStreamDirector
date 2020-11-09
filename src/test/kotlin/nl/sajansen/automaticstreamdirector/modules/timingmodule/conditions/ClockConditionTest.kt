package nl.sajansen.automaticstreamdirector.modules.timingmodule.conditions


import nl.sajansen.automaticstreamdirector.triggers.Condition
import java.sql.Time
import java.util.*
import java.util.logging.Logger
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ClockConditionTest {

    @Test
    fun testCheckPresentTimeWithDayDifferenceInPast() {
        val time = Time(Date().time - 24 * 60 * 60 * 1000).toLocalTime()
        val condition = ClockCondition(time)

        assertTrue(condition.check())
    }

    @Test
    fun testCheckPresentTimeWithDayDifferenceInFuture() {
        val time = Time(Date().time + 24 * 60 * 60 * 1000).toLocalTime()
        val condition = ClockCondition(time)

        assertTrue(condition.check())
    }

    @Test
    fun testCheckPastTimeWithDayDifference() {
        val time = Time(Date().time + 24 * 60 * 60 * 1000).toLocalTime().minusHours(1)
        val condition = ClockCondition(time)

        assertFalse(condition.check())
    }

    @Test
    fun testCheckFutureTimeWithDayDifference() {
        val time = Time(Date().time + 24 * 60 * 60 * 1000).toLocalTime().plusHours(1)
        val condition = ClockCondition(time)

        assertFalse(condition.check())
    }

    @Test
    fun testCheckPresentTimeWithHours() {
        val time = Time(Date().time).toLocalTime()
        val condition = ClockCondition(time)

        assertTrue(condition.check())
    }

    @Test
    fun testCheckPastTimeWithHours() {
        val time = Time(Date().time).toLocalTime().minusHours(1)
        val condition = ClockCondition(time)

        assertFalse(condition.check())
    }

    @Test
    fun testCheckFutureTimeWithHours() {
        val time = Time(Date().time).toLocalTime().plusHours(1)
        val condition = ClockCondition(time)

        assertFalse(condition.check())
    }

    @Test
    fun testCheckPastTimeWithMinutes() {
        val time = Time(Date().time).toLocalTime().minusMinutes(1)
        val condition = ClockCondition(time)

        assertFalse(condition.check())
    }

    @Test
    fun testCheckFutureTimeWithMinutes() {
        val time = Time(Date().time).toLocalTime().plusMinutes(1)
        val condition = ClockCondition(time)

        assertFalse(condition.check())
    }

    @Test
    fun testCheckPresentTimeWithSecondsButNotMatchSeconds() {
        val time = Time(Date().time).toLocalTime()
        val condition = ClockCondition(time, matchSeconds = false)

        assertTrue(condition.check())
    }

    @Test
    fun testCheckPresentTimeWithSecondsButMatchSeconds() {
        val time = Time(Date().time).toLocalTime()
        val condition = ClockCondition(time, matchSeconds = true)

        assertTrue(condition.check())
    }

    @Test
    fun testCheckPastTimeWithSecondsButNotMatchSeconds() {
        val time = Time(Date().time).toLocalTime().minusSeconds(1)
        val condition = ClockCondition(time, matchSeconds = false)

        assertTrue(condition.check())
    }

    @Test
    fun testCheckPastTimeWithSecondsButMatchSeconds() {
        val time = Time(Date().time).toLocalTime().minusSeconds(1)
        val condition = ClockCondition(time, matchSeconds = true)

        assertFalse(condition.check())
    }

    @Test
    fun testCheckFutureTimeWithSecondsButNotMatchSeconds() {
        val time = Time(Date().time).toLocalTime().plusSeconds(1)
        val condition = ClockCondition(time, matchSeconds = false)

        assertTrue(condition.check())
    }

    @Test
    fun testCheckFutureTimeWithSecondsButMatchSeconds() {
        val time = Time(Date().time).toLocalTime().plusSeconds(1)
        val condition = ClockCondition(time, matchSeconds = true)

        assertFalse(condition.check())
    }
}