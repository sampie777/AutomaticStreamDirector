package nl.sajansen.automaticstreamdirector.modules.timingmodule.conditions


import nl.sajansen.automaticstreamdirector.triggers.Condition
import java.util.*
import java.util.logging.Logger

class DateCondition(
    val date: Date,
    val matchTime: Boolean = false,
) : Condition {
    private val logger = Logger.getLogger(DateCondition::class.java.name)

    override fun check(): Boolean {
        val now = Date()

        val precision = if (matchTime) 60 * 1000 else 24 * 60 * 60 * 1000

        return now.time / precision == date.time / precision
    }
}