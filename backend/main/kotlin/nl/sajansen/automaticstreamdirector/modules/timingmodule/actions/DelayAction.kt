package nl.sajansen.automaticstreamdirector.modules.timingmodule.actions


import com.google.gson.Gson
import nl.sajansen.automaticstreamdirector.actions.Action
import nl.sajansen.automaticstreamdirector.actions.StaticAction
import nl.sajansen.automaticstreamdirector.api.json.FormDataJson
import nl.sajansen.automaticstreamdirector.common.FormComponent
import nl.sajansen.automaticstreamdirector.db.entities.ActionEntity
import nl.sajansen.automaticstreamdirector.jsonBuilder
import java.util.logging.Logger
import kotlin.math.round

class DelayAction(
    private val milliseconds: Long,
    override var id: Long? = null,
) : Action() {
    private val logger = Logger.getLogger(DelayAction::class.java.name)

    override fun execute() {
        Thread.sleep(milliseconds)
    }

    override fun displayName(): String {
        val seconds = round((milliseconds / 1000.0) * 1000) / 1000.0
        return "Wait $seconds seconds"
    }

    override fun toString() = displayName()

    override fun getDbDataSet(): String? = jsonBuilder(prettyPrint = false).toJson(
        DbDataSet(milliseconds)
    )

    data class DbDataSet(
        val milliseconds: Long
    )

    companion object : StaticAction {
        override val name: String = DelayAction::class.java.simpleName
        override val previewText: String = "Wait ... seconds"

        override fun formComponents(): List<FormComponent> = listOf(
            FormComponent(
                "milliseconds",
                "Milli seconds",
                FormComponent.Type.Number,
                required = true
            ),
        )

        @JvmStatic
        override fun save(data: FormDataJson): Any {
            val milliseconds = data["milliseconds"]?.toLongOrNull()
            if (milliseconds == null || milliseconds <= 0) {
                return listOf("Milli seconds must be greater than 0")
            }

            DelayAction(milliseconds).also {
                saveOrUpdate(it)
                return it
            }
        }

        override fun fromDbEntity(actionEntity: ActionEntity): Action? {
            val data = Gson().fromJson(actionEntity.dataString, DbDataSet::class.java)

            return DelayAction(
                data.milliseconds,
                id = actionEntity.id,
            )
        }
    }
}