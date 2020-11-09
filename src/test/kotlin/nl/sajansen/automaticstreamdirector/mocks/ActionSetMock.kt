package nl.sajansen.automaticstreamdirector.mocks


import nl.sajansen.automaticstreamdirector.actions.Action
import nl.sajansen.automaticstreamdirector.actions.ActionSet
import java.util.logging.Logger

class ActionSetMock : ActionSet {
    private val logger = Logger.getLogger(ActionSetMock::class.java.name)
    override val name = "ActionSetMock"
    override val actions = arrayListOf<Action>()
}