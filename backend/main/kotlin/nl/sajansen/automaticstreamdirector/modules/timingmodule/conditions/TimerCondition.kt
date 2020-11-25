package nl.sajansen.automaticstreamdirector.modules.timingmodule.conditions


import com.google.gson.Gson
import nl.sajansen.automaticstreamdirector.api.json.FormDataJson
import nl.sajansen.automaticstreamdirector.common.FormComponent
import nl.sajansen.automaticstreamdirector.db.entities.ConditionEntity
import nl.sajansen.automaticstreamdirector.getTimeAsClock
import nl.sajansen.automaticstreamdirector.triggers.Condition
import nl.sajansen.automaticstreamdirector.triggers.StaticCondition
import java.util.*
import java.util.logging.Logger

class TimerCondition(
    val seconds: Long,
    override var id: Long? = null,
) : Condition() {
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

    override fun getDataSet(): Any? = DbDataSet(seconds = seconds)

    data class DbDataSet(
        val seconds: Long,
    )

    companion object : StaticCondition {
        override val name: String = TimerCondition::class.java.simpleName
        override val previewText: String = "If [...] seconds went by"

        override fun formComponents() = listOf(
            FormComponent("seconds", "Seconds", FormComponent.Type.Number, required = true),
        )

        @JvmStatic
        override fun save(data: FormDataJson): Any {
            val id = data["id"]?.toLongOrNull()
            val seconds = data["seconds"]?.toLongOrNull()

            val validationErrors = arrayListOf<String>()

            if (seconds == null || seconds <= 0) {
                validationErrors.add("Time must be greater than 0 seconds")
            }

            if (validationErrors.isNotEmpty()) {
                return validationErrors
            }

            TimerCondition(
                id = id,
                seconds = seconds!!,
            ).also {
                saveOrUpdate(it)
                return it
            }
        }

        override fun fromDbEntity(conditionEntity: ConditionEntity): Condition? {
            val data = Gson().fromJson(conditionEntity.dataString, DbDataSet::class.java)

            return TimerCondition(
                seconds = data.seconds,
                id = conditionEntity.id,
            )
        }
    }
}