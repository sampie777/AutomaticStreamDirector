package nl.sajansen.automaticstreamdirector.modules.timingmodule.conditions


import nl.sajansen.automaticstreamdirector.getTimeAsClock
import nl.sajansen.automaticstreamdirector.triggers.Condition
import java.util.*
import java.util.logging.Logger

class TimerCondition(
    val seconds: Long,
) : Condition {
    private val logger = Logger.getLogger(TimerCondition::class.java.name)

    private var startTime = Date()

    override fun check(): Boolean {
        val now = Date()

        if (!now.after(Date(startTime.time + seconds * 1000))) {
            return false
        }

        startTime = now
        return true
    }

    override fun displayName(): String {
        return "If ${getTimeAsClock(seconds, looseFormat = true)} went by"
    }

    override fun toString() = displayName()
}