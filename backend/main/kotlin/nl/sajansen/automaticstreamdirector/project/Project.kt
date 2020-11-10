package nl.sajansen.automaticstreamdirector.project

import nl.sajansen.automaticstreamdirector.actions.ActionSet
import nl.sajansen.automaticstreamdirector.triggers.Trigger
import java.util.logging.Logger

object Project {
    private val logger = Logger.getLogger(Project::class.java.name)

    val triggers: ArrayList<Trigger> = arrayListOf()
    val availableActionSets: ArrayList<ActionSet> = arrayListOf()
}