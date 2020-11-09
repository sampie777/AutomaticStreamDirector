package nl.sajansen.automaticstreamdirector.mocks


import nl.sajansen.automaticstreamdirector.actions.ActionSet
import nl.sajansen.automaticstreamdirector.triggers.Condition
import nl.sajansen.automaticstreamdirector.triggers.Trigger
import java.util.logging.Logger

class TriggerMock :Trigger {
    private val logger = Logger.getLogger(TriggerMock::class.java.name)
    override val name = "TriggerMock"
    override val importance = 0
    override val conditions = arrayListOf<Condition>()
    override val actionSets = arrayListOf<ActionSet>()
}