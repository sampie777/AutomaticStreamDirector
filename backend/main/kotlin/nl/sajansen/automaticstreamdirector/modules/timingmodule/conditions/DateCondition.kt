package nl.sajansen.automaticstreamdirector.modules.timingmodule.conditions


import nl.sajansen.automaticstreamdirector.format
import nl.sajansen.automaticstreamdirector.triggers.Condition
import nl.sajansen.automaticstreamdirector.triggers.StaticCondition
import java.util.*
import java.util.logging.Logger

class DateCondition(
    val date: Date,
    val matchTime: Boolean = false,
) : Condition {
    private val logger = Logger.getLogger(DateCondition::class.java.name)

    override fun check(): Boolean {
        val now = Date()

        // Precision is in minutes or in days
        val precision = if (matchTime) 60 * 1000 else 24 * 60 * 60 * 1000

        return now.time / precision == date.time / precision
    }

    override fun displayName(): String {
        return if (matchTime) {
            "If current date and time is ${date.format("d-M-yyyy, H:mm")}"
        } else {
            "If current date is ${date.format("d-M-yyyy")}"
        }
    }

    override fun toString() = displayName()

    companion object : StaticCondition {
        override fun name(): String = DateCondition::class.java.simpleName
        override fun previewText(): String = "If today is [d-M-yyyy (H:mm)]"
    }
}