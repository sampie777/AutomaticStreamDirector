package nl.sajansen.automaticstreamdirector.modules.builtinmodule


import nl.sajansen.automaticstreamdirector.actions.StaticAction
import nl.sajansen.automaticstreamdirector.modules.Module
import nl.sajansen.automaticstreamdirector.modules.builtinmodule.actions.ToggleAction
import nl.sajansen.automaticstreamdirector.triggers.StaticCondition
import java.util.logging.Logger

class BuiltInModule : Module {
    private val logger = Logger.getLogger(BuiltInModule::class.java.name)

    override val name: String = BuiltInModule::class.java.name

    override fun actions(): List<StaticAction> {
        return listOf<StaticAction>(
            ToggleAction,
        )
    }

    override fun conditions(): List<StaticCondition> {
        return listOf<StaticCondition>(
        )
    }
}