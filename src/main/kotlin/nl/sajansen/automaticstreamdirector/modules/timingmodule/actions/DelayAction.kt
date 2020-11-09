package nl.sajansen.automaticstreamdirector.modules.timingmodule.actions


import nl.sajansen.automaticstreamdirector.actions.Action
import java.util.logging.Logger

class DelayAction(
    val milliseconds: Long
) : Action {
    private val logger = Logger.getLogger(DelayAction::class.java.name)

    override fun execute() {
        Thread.sleep(milliseconds)
    }
}