package nl.sajansen.automaticstreamdirector.modules.timingmodule.conditions


import nl.sajansen.automaticstreamdirector.triggers.Condition
import java.time.LocalTime
import java.util.logging.Logger

class ClockCondition(
    val time: LocalTime,
    val matchSeconds: Boolean = false,
) : Condition {
    private val logger = Logger.getLogger(ClockCondition::class.java.name)

    override fun check(): Boolean {
        val now = LocalTime.now()

        return now.hour == time.hour
                && now.minute == time.minute
                && (!matchSeconds || now.second == time.second)
    }
}