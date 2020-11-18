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
    private val actionSetToBeToggledId: Long? = null,
    private val startToggledOn: Boolean = true,
    override var id: Long? = null,
) : Action() {
    private val logger = Logger.getLogger(ToggleAction::class.java.name)

    private var nextToggle = !startToggledOn

    private fun getActionSetToBeToggled(): ActionSet? {
        return Project.availableActionSets.find {
            it.id == actionSetToBeToggledId
        }
    }

    override fun execute() {
        nextToggle = !nextToggle

        if (!nextToggle) {
            logger.info("Action toggled off")
            return
        }

        logger.info("Action toggled on. Executing toggle actionSet: ${getActionSetToBeToggled()?.name}")
        getActionSetToBeToggled()?.execute()
    }

    override fun displayName(): String {
        return "Toggle action set: ${getActionSetToBeToggled()?.name}"
    }

    override fun getDbDataSet(): String? = jsonBuilder(prettyPrint = false).toJson(
        DbDataSet(
            actionSetToBeToggledId = getActionSetToBeToggled()?.id,
            startToggledOn = startToggledOn,
        )
    )

    data class DbDataSet(
        val actionSetToBeToggledId: Long?,
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
                selectValues = Project.availableActionSets.map {
                    FormComponent.SelectOption(it.id, it.name)
                }
            ),
            FormComponent("startToggledOn", "Start toggled on", FormComponent.Type.Checkbox),
        )

        @JvmStatic
        override fun save(data: FormDataJson): Any {
            val actionSetToBeToggledId = data["actionSetToBeToggled"]?.toLongOrNull()
            val startToggledOn = data["startToggledOn"] == "on"

            val actionSet = Project.availableActionSets.find { it.id == actionSetToBeToggledId }

            if (actionSet == null) {
                return listOf("Action set with id=$actionSetToBeToggledId not found")
            }

            ToggleAction(
                actionSetToBeToggledId = actionSet.id,
                startToggledOn = startToggledOn
            ).also {
                saveOrUpdate(it)
                return it
            }
        }

        override fun fromDbEntity(actionEntity: ActionEntity): Action? {
            val data = Gson().fromJson(actionEntity.dataString, DbDataSet::class.java)

            return ToggleAction(
                actionSetToBeToggledId = data.actionSetToBeToggledId,
                startToggledOn = data.startToggledOn,
                id = actionEntity.id,
            )
        }
    }
}