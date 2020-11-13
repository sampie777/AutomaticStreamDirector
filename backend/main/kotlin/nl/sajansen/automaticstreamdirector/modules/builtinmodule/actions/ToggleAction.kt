package nl.sajansen.automaticstreamdirector.modules.builtinmodule.actions


import nl.sajansen.automaticstreamdirector.actions.Action
import nl.sajansen.automaticstreamdirector.actions.ActionSet
import nl.sajansen.automaticstreamdirector.actions.StaticAction
import nl.sajansen.automaticstreamdirector.api.json.FormDataJson
import nl.sajansen.automaticstreamdirector.common.FormComponent
import nl.sajansen.automaticstreamdirector.project.Project
import java.util.logging.Logger

class ToggleAction(
    private val actionSetToBeToggled: ActionSet,
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

        logger.info("Action toggled on. Executing toggle actionSet: ${actionSetToBeToggled.name}")
        actionSetToBeToggled.execute()
    }

    override fun displayName(): String {
        return "Toggle action set: ${actionSetToBeToggled.name}"
    }

    override fun toString() = displayName()

    companion object : StaticAction {
        override val name: String = ToggleAction::class.java.simpleName
        override val previewText: String = "Toggle action set: ..."

        override val formComponents: List<FormComponent> = listOf(
            FormComponent(
                "actionSetToBeToggled",
                "Toggle action set (name)",
                FormComponent.Type.Text,
                required = true
            ),
            FormComponent("startToggledOn", "Start toggled on", FormComponent.Type.Checkbox),
        )

        @JvmStatic
        override fun save(data: FormDataJson): Any {
            val actionSetToBeToggled = data["actionSetToBeToggled"]
            val startToggledOn = data["startToggledOn"] == "on"

            val actionSet = Project.availableActionSets.find { it.name == actionSetToBeToggled }

            if (actionSet == null) {
                return listOf("Action set '$actionSetToBeToggled' not found")
            }

            return ToggleAction(actionSet, startToggledOn)
        }
    }
}