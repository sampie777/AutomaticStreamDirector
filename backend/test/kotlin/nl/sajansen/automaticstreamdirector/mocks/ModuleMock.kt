package nl.sajansen.automaticstreamdirector.mocks


import nl.sajansen.automaticstreamdirector.actions.StaticAction
import nl.sajansen.automaticstreamdirector.modules.Module
import nl.sajansen.automaticstreamdirector.triggers.StaticCondition
import java.util.logging.Logger

class ModuleMock(override val name: String = "ModuleMock") : Module {
    private val logger = Logger.getLogger(ModuleMock::class.java.name)
    override fun actions(): List<StaticAction> {
        TODO("Not yet implemented")
    }

    override fun conditions(): List<StaticCondition> {
        TODO("Not yet implemented")
    }
}