package nl.sajansen.automaticstreamdirector

import nl.sajansen.automaticstreamdirector.actions.ActionSet
import nl.sajansen.automaticstreamdirector.project.Project
import nl.sajansen.automaticstreamdirector.project.UpdateTimer
import nl.sajansen.automaticstreamdirector.triggers.Condition
import nl.sajansen.automaticstreamdirector.triggers.Trigger
import java.util.logging.Logger

object Director {
    private val logger = Logger.getLogger(Director::class.java.name)

    private var lastExecutionThread: Thread? = null

    private var lastTrigger: Trigger? = null
    fun getLastTrigger() = lastTrigger

    private var isRunning: Boolean = false
    fun isRunning() = isRunning

    fun start() {
        UpdateTimer.restart()
        isRunning = true
    }

    fun stop() {
        UpdateTimer.stop()
        isRunning = false
    }

    fun update() {
        val trigger = Project.triggers
            .filter { it.conditions.size > 0 && it.conditions.all(Condition::check) }
            .maxByOrNull(Trigger::importance)

        if (trigger == lastTrigger) {
            logger.fine("Trigger did not change")
            return
        }
        lastTrigger = trigger

        if (trigger == null) {
            logger.info("No Trigger to be executed found")
            return
        }

        if (trigger.actionSets.isEmpty()) {
            logger.info("No ActionSets found for trigger $trigger")
            return
        }

        logger.info("Executing ActionSets for trigger: $trigger")
        lastExecutionThread = Thread {
            trigger.actionSets.forEach(ActionSet::execute)
        }
        lastExecutionThread?.start()
    }

    fun waitForActionSetCompletion() {
        lastExecutionThread?.join()
    }
}