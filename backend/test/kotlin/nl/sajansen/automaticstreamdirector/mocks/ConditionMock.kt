package nl.sajansen.automaticstreamdirector.mocks


import nl.sajansen.automaticstreamdirector.triggers.Condition
import java.util.logging.Logger

class ConditionMock(
    var checkReturnValue: Boolean = false
) : Condition {
    private val logger = Logger.getLogger(ConditionMock::class.java.name)

    override fun check(): Boolean {
        return checkReturnValue
    }
}