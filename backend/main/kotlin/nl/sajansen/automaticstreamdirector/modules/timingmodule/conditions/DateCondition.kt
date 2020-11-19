package nl.sajansen.automaticstreamdirector.modules.timingmodule.conditions


import com.google.gson.Gson
import nl.sajansen.automaticstreamdirector.api.json.FormDataJson
import nl.sajansen.automaticstreamdirector.common.FormComponent
import nl.sajansen.automaticstreamdirector.db.entities.ConditionEntity
import nl.sajansen.automaticstreamdirector.format
import nl.sajansen.automaticstreamdirector.jsonBuilder
import nl.sajansen.automaticstreamdirector.triggers.Condition
import nl.sajansen.automaticstreamdirector.triggers.StaticCondition
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.Logger

class DateCondition(
    val date: Date,
    val matchTime: Boolean = false,
    override var id: Long? = null,
) : Condition() {

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

    override fun getDbDataSet(): String? = jsonBuilder(prettyPrint = false).toJson(
        DbDataSet(
            date = date,
            matchTime = matchTime,
        )
    )

    data class DbDataSet(
        val date: Date,
        val matchTime: Boolean,
    )

    companion object : StaticCondition {
        private val logger = Logger.getLogger(DateCondition::class.java.name)

        override val name: String = DateCondition::class.java.simpleName
        override val previewText: String = "If today is [d-M-yyyy (H:mm)]"

        override fun formComponents() = listOf(
            FormComponent("date", "Date", FormComponent.Type.Date),
            FormComponent("matchTime", "Match time", FormComponent.Type.Checkbox),
        )

        @JvmStatic
        override fun save(data: FormDataJson): Any {
            val dateString = data["date"] ?: ""
            val matchTime = data["matchTime"] == "on"

            val validationErrors = arrayListOf<String>()

            if (dateString.isEmpty()) {
                validationErrors.add("Date cannot be left empty")
            }

            val date = try {
                SimpleDateFormat("dd-MM-yyyy").parse(dateString)
            } catch (e: Exception) {
                logger.warning("Could not parse date: $dateString")
                e.printStackTrace()
                validationErrors.add("Entered date format is invalid")
                null
            }

            if (validationErrors.isNotEmpty()) {
                return validationErrors
            }

            DateCondition(
                date = date!!,
                matchTime = matchTime,
            ).also {
                saveOrUpdate(it)
                return it
            }
        }

        override fun fromDbEntity(conditionEntity: ConditionEntity): Condition? {
            val data = Gson().fromJson(conditionEntity.dataString, DbDataSet::class.java)

            return DateCondition(
                date = data.date,
                matchTime = data.matchTime,
                id = conditionEntity.id,
            )
        }
    }
}