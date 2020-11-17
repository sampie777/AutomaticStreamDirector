package nl.sajansen.automaticstreamdirector.mocks


import nl.sajansen.automaticstreamdirector.actions.Action
import java.util.logging.Logger

class ActionMock : Action() {
    private val logger = Logger.getLogger(ActionMock::class.java.name)

    var isExecuted = false

    override fun execute() {
        isExecuted = true
    }

    override fun displayName(): String {
        return "ActinoMock"
    }

    override fun getDbDataSet(): String? {
        return null
    }

}