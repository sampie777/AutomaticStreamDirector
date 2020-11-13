package nl.sajansen.automaticstreamdirector.modules.timingmodule.actions


import nl.sajansen.automaticstreamdirector.actions.Action
import nl.sajansen.automaticstreamdirector.actions.StaticAction
import nl.sajansen.automaticstreamdirector.api.json.FormDataJson
import nl.sajansen.automaticstreamdirector.common.FormComponent
import nl.sajansen.automaticstreamdirector.modules.httpmodule.actions.HttpRequestAction
import java.util.logging.Logger
import kotlin.math.round

class DelayAction(
    val milliseconds: Long
) : Action {
    private val logger = Logger.getLogger(HttpRequestAction::class.java.name)

    override fun execute() {
        Thread.sleep(milliseconds)
    }

    override fun displayName(): String {
        val seconds = round((milliseconds / 1000.0) * 1000) / 1000.0
        return "Wait $seconds seconds"
    }

    override fun toString() = displayName()

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

            return DelayAction(milliseconds)
        }
    }
}