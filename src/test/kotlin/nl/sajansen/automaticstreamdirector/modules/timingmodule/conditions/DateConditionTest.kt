package nl.sajansen.automaticstreamdirector.modules.timingmodule.conditions


import nl.sajansen.automaticstreamdirector.triggers.Condition
import java.sql.Time
import java.util.*
import java.util.logging.Logger
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DateConditionTest {

    @Test
    fun testPresentDate() {
        val date = Date()
        val condition = DateCondition(date, matchTime = false)

        assertTrue(condition.check())
    }

    @Test
    fun testPastDate() {
        val date = Date(Date().time - 24 * 60 * 60 * 1000)
        val condition = DateCondition(date, matchTime = false)

        assertFalse(condition.check())
    }

    @Test
    fun testFutureDate() {
        val date = Date(Date().time + 24 * 60 * 60 * 1000)
        val condition = DateCondition(date, matchTime = false)

        assertFalse(condition.check())
    }

    @Test
    fun testPresentDateMatchTime() {
        val date = Date()
        val condition = DateCondition(date, matchTime = true)

        assertTrue(condition.check())
    }

    @Test
    fun testPresentDateDifferentTimeButNotMatchTime() {
        val date = Date(Date().time - 60 * 1000)
        val condition = DateCondition(date, matchTime = false)

        assertTrue(condition.check())
    }

    @Test
    fun testPresentDateDifferentTimeButMatchTime() {
        val date = Date(Date().time - 60 * 1000)
        val condition = DateCondition(date, matchTime = true)

        assertFalse(condition.check())
    }

    @Test
    fun testPresentDateDifferentTimeSecondsButMatchTime() {
        val date = Date(Date().time - 1000)
        val condition = DateCondition(date, matchTime = true)

        assertTrue(condition.check())
    }
}