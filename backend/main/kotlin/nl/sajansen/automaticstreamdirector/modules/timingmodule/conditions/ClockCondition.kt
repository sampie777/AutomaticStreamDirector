package nl.sajansen.automaticstreamdirector.modules.timingmodule.conditions


import com.google.gson.Gson
import nl.sajansen.automaticstreamdirector.api.json.FormDataJson
import nl.sajansen.automaticstreamdirector.common.FormComponent
import nl.sajansen.automaticstreamdirector.db.entities.ConditionEntity
import nl.sajansen.automaticstreamdirector.format
import nl.sajansen.automaticstreamdirector.jsonBuilder
import nl.sajansen.automaticstreamdirector.triggers.Condition
import nl.sajansen.automaticstreamdirector.triggers.StaticCondition
import java.time.LocalTime
import java.util.logging.Logger

class ClockCondition(
    val time: LocalTime,
    val matchSeconds: Boolean = false,
    override var id: Long? = null,
) : Condition() {

    override fun check(): Boolean {
        val now = LocalTime.now()

        return now.hour == time.hour
                && now.minute == time.minute
                && (!matchSeconds || now.second == time.second)
    }

    override fun displayName(): String {
        return if (matchSeconds) {
            "If current time is ${time.format("H:mm:ss")}"
        } else {
            "If current time is ${time.format("H:mm")}"
        }
    }

    override fun getDbDataSet(): String? = jsonBuilder(prettyPrint = false).toJson(
        DbDataSet(time = time, matchSeconds = matchSeconds)
    )

    data class DbDataSet(
        val time: LocalTime,
        val matchSeconds: Boolean,
    )

    companion object : StaticCondition {
        private val logger = Logger.getLogger(ClockCondition::class.java.name)

        override val name: String = ClockCondition::class.java.simpleName
        override val previewText: String = "If current time is [H:mm(:ss)]"

        override fun formComponents() = listOf(
            FormComponent("time", "Time", FormComponent.Type.Time, required = true),
            FormComponent("matchSeconds", "Match seconds", FormComponent.Type.Checkbox),
        )

        @JvmStatic
        override fun save(data: FormDataJson): Any {
            val timeString = data["time"] ?: ""
            val matchSeconds = data["matchSeconds"] == "on"

            val validationErrors = arrayListOf<String>()

            if (timeString.isEmpty()) {
                validationErrors.add("Time cannot be left empty")
            }

            val time = try {
                LocalTime.parse(timeString)
            } catch (e: Exception) {
                logger.warning("Could not parse time: $timeString")
                e.printStackTrace()
                validationErrors.add("Entered time format is invalid")
                null
            }

            if (validationErrors.isNotEmpty()) {
                return validationErrors
            }

            ClockCondition(
                time = time!!,
                matchSeconds = matchSeconds,
            ).also {
                saveOrUpdate(it)
                return it
            }
        }

        override fun fromDbEntity(conditionEntity: ConditionEntity): Condition? {
            val data = Gson().fromJson(conditionEntity.dataString, DbDataSet::class.java)

            return ClockCondition(
                time = data.time,
                matchSeconds = data.matchSeconds,
                id = conditionEntity.id,
            )
        }
    }
}