package nl.sajansen.automaticstreamdirector.modules.builtinmodule.actions


import nl.sajansen.automaticstreamdirector.actions.Action
import nl.sajansen.automaticstreamdirector.actions.StaticAction
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
        override fun name(): String = ToggleAction::class.java.simpleName
        override fun previewText(): String = "Toggle action: ..."
    }
}