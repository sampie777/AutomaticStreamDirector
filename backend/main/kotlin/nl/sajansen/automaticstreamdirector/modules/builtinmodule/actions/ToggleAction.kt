package nl.sajansen.automaticstreamdirector.modules.builtinmodule.actions


import com.google.gson.Gson
import nl.sajansen.automaticstreamdirector.actions.Action
import nl.sajansen.automaticstreamdirector.actions.ActionSet
import nl.sajansen.automaticstreamdirector.actions.StaticAction
import nl.sajansen.automaticstreamdirector.api.json.FormDataJson
import nl.sajansen.automaticstreamdirector.common.FormComponent
import nl.sajansen.automaticstreamdirector.db.entities.ActionEntity
import nl.sajansen.automaticstreamdirector.jsonBuilder
import nl.sajansen.automaticstreamdirector.project.Project
import java.util.logging.Logger

class ToggleAction(
    private val actionSetToBeToggled: ActionSet,
    private val startToggledOn: Boolean = true,
    override var id: Long? = null,
) : Action() {
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

    override fun getDbDataSet(): String? = jsonBuilder(prettyPrint = false).toJson(
        DbDataSet(
            actionSetToBeToggledName = actionSetToBeToggled.name,
            startToggledOn = startToggledOn,
        )
    )

    data class DbDataSet(
        val actionSetToBeToggledName: String,
        val startToggledOn: Boolean,
    )

    companion object : StaticAction {
        override val name: String = ToggleAction::class.java.simpleName
        override val previewText: String = "Toggle action set: ..."

        override fun formComponents(): List<FormComponent> = listOf(
            FormComponent(
                "actionSetToBeToggled",
                "Toggle action set (name)",
                FormComponent.Type.Select,
                required = true,
                selectValues = Project.availableActionSets.map(ActionSet::name)
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

            ToggleAction(actionSet, startToggledOn).also {
                saveOrUpdate(it)
                return it
            }
        }

        override fun fromDbEntity(actionEntity: ActionEntity): Action? {
            val data = Gson().fromJson(actionEntity.dataString, DbDataSet::class.java)

            val actionSetToBeToggled = Project.availableActionSets.find {
                it.name == data.actionSetToBeToggledName
            } ?: return null

            return ToggleAction(
                actionSetToBeToggled = actionSetToBeToggled,
                startToggledOn = data.startToggledOn,
                id = actionEntity.id,
            )
        }
    }
}