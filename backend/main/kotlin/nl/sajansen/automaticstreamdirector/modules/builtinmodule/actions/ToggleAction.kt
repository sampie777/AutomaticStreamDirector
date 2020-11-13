package nl.sajansen.automaticstreamdirector.modules.builtinmodule.actions


import nl.sajansen.automaticstreamdirector.actions.Action
import nl.sajansen.automaticstreamdirector.actions.StaticAction
import nl.sajansen.automaticstreamdirector.api.json.FormDataJson
import nl.sajansen.automaticstreamdirector.common.FormComponent
import nl.sajansen.automaticstreamdirector.common.FormComponentType
import java.util.logging.Logger

class ToggleAction(
    private val actionToBeToggled: Action,
    startToggledOn: Boolean = true
) : Action {
    private val logger = Logger.getLogger(ToggleAction::class.java.name)

    private var nextToggle = !startToggledOn

    override fun execute() {
        nextToggle = !nextToggle

        if (!nextToggle) {
            logger.info("Action toggled off")
            return
        }

        logger.info("Action toggled on. Executing toggle action: ${actionToBeToggled.displayName()}")
        actionToBeToggled.execute()
    }

    override fun displayName(): String {
        return "Toggle action: ${actionToBeToggled.displayName()}"
    }

    override fun toString() = displayName()

    companion object : StaticAction {
        override val name: String = ToggleAction::class.java.simpleName
        override val previewText: String = "Toggle action: ..."

        override val formComponents: List<FormComponent> = listOf(
            FormComponent("startToggledOn", "Start toggled on", FormComponentType.Checkbox),
        )

        @JvmStatic
        override fun save(data: FormDataJson): Any {
            val milliseconds = data["startToggledOn"] == "on"

            return listOf("Missing action to be toggled")
//            return ToggleAction(startToggledOn)
        }
    }
}