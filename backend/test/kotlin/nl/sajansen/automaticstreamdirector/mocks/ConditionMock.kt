package nl.sajansen.automaticstreamdirector.mocks


import nl.sajansen.automaticstreamdirector.triggers.Condition
import java.util.logging.Logger

class ConditionMock(
    var checkReturnValue: Boolean = false, override var id: Long? = null
) : Condition() {
    private val logger = Logger.getLogger(ConditionMock::class.java.name)

    override fun check(): Boolean {
        return checkReturnValue
    }

    override fun displayName(): String {
        return "ConditionMock"
    }

    override fun getDataSet(): Any? {
        return ""
    }
}