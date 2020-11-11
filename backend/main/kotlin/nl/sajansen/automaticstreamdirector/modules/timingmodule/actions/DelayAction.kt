package nl.sajansen.automaticstreamdirector.modules.timingmodule.actions


import nl.sajansen.automaticstreamdirector.actions.Action
import nl.sajansen.automaticstreamdirector.modules.httpmodule.actions.HttpRequestAction
import java.util.logging.Logger
import kotlin.math.round

class DelayAction(
    val milliseconds: Long
) : Action {
    private val logger = Logger.getLogger(HttpRequestAction::class.java.name)

    override fun execute() {
        Thread.sleep(milliseconds)
    }

    override fun displayName(): String {
        val seconds = round((milliseconds / 1000.0) * 1000) / 1000.0
        return "Wait $seconds seconds"
    }
}