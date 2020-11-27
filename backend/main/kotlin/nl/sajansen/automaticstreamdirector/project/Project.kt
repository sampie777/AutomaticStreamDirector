package nl.sajansen.automaticstreamdirector.project

import nl.sajansen.automaticstreamdirector.actions.ActionSet
import nl.sajansen.automaticstreamdirector.triggers.Trigger
import java.util.*
import java.util.logging.Logger

object Project {
    private val logger = Logger.getLogger(Project::class.java.name)

    val availableActionSets: ArrayList<ActionSet> = arrayListOf()
    val triggers: ArrayList<Trigger> = arrayListOf()

    fun load() {
        loadActionSets()
        loadTriggers()
    }

    private fun loadActionSets() {
        logger.info("Loading available action sets")

        val actionSets = ActionSet.list()?.filterNotNull()

        this.availableActionSets.clear()
        if (actionSets == null) {
            logger.info("No actionSets found in database")
            return
        }

        availableActionSets.addAll(actionSets)
        logger.info("${actionSets.size} actionSets loaded from database")
    }

    fun loadTriggers() {
        logger.info("Loading triggers")

        val triggers = Trigger.list()?.filterNotNull()

        this.triggers.clear()
        if (triggers == null) {
            logger.info("No triggers found in database")
            return
        }

        this.triggers.addAll(triggers)
        triggers.forEach(Trigger::syncActionSetsWithProjectState)
        logger.info("${triggers.size} triggers loaded from database")
    }
}