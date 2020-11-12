package nl.sajansen.automaticstreamdirector.modules.httpmodule


import nl.sajansen.automaticstreamdirector.actions.StaticAction
import nl.sajansen.automaticstreamdirector.modules.Module
import nl.sajansen.automaticstreamdirector.modules.httpmodule.actions.HttpRequestAction
import nl.sajansen.automaticstreamdirector.modules.httpmodule.conditions.HttpRequestBodyCondition
import nl.sajansen.automaticstreamdirector.modules.httpmodule.conditions.HttpRequestResponseCodeCondition
import nl.sajansen.automaticstreamdirector.triggers.StaticCondition
import java.util.logging.Logger

class HttpModule : Module {
    private val logger = Logger.getLogger(HttpModule::class.java.name)

    override val name: String = HttpModule::class.java.name

    override fun actions(): List<StaticAction> {
        return listOf<StaticAction>(
            HttpRequestAction,
        )
    }

    override fun conditions(): List<StaticCondition> {
        return listOf<StaticCondition>(
            HttpRequestBodyCondition,
            HttpRequestResponseCodeCondition,
        )
    }
}