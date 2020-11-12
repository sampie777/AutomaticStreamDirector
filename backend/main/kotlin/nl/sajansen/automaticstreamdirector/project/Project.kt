package nl.sajansen.automaticstreamdirector.project

import nl.sajansen.automaticstreamdirector.actions.ActionSet
import nl.sajansen.automaticstreamdirector.modules.builtinmodule.actions.ToggleAction
import nl.sajansen.automaticstreamdirector.modules.httpmodule.actions.HttpRequestAction
import nl.sajansen.automaticstreamdirector.modules.httpmodule.conditions.HttpRequestResponseCodeCondition
import nl.sajansen.automaticstreamdirector.modules.timingmodule.actions.DelayAction
import nl.sajansen.automaticstreamdirector.modules.timingmodule.conditions.ClockCondition
import nl.sajansen.automaticstreamdirector.modules.timingmodule.conditions.DateCondition
import nl.sajansen.automaticstreamdirector.modules.timingmodule.conditions.TimerCondition
import nl.sajansen.automaticstreamdirector.triggers.Trigger
import java.time.LocalTime
import java.util.*
import java.util.logging.Logger

object Project {
    private val logger = Logger.getLogger(Project::class.java.name)

    val triggers: ArrayList<Trigger> = arrayListOf()
    val availableActionSets: ArrayList<ActionSet> = arrayListOf()

    fun load() {
        loadActionSets()
        loadTriggers()
    }

    private fun loadActionSets() {
        logger.info("Loading available action sets")
        availableActionSets.add(ActionSet("Action set 1").also {
            it.actions.add(DelayAction(2500))
            it.actions.add(HttpRequestAction("http://localhost:8080/api/v1/director/status"))
        })

        availableActionSets.add(ActionSet("Toggle set").also {
            val toggleAction1 = HttpRequestAction("http://localhost:8080/api/v1/actionsets/list")
            it.actions.add(ToggleAction(toggleAction1, startToggledOn = true))

            val toggleAction2 = HttpRequestAction("http://localhost:8080/api/v1/triggers/list")
            it.actions.add(ToggleAction(toggleAction2, startToggledOn = false))
        })
    }

    private fun loadTriggers() {
        logger.info("Loading triggers")
        triggers.add(Trigger("Trigger1").also {
            it.conditions.add(ClockCondition(LocalTime.now()))
            it.conditions.add(DateCondition(Date(Date().time - 24 * 60 * 60 * 1000)))
            it.conditions.add(
                HttpRequestResponseCodeCondition(
                    "http://localhost:8080/api/v1/director/status",
                    expectedCode = 200
                )
            )
        })

        triggers.add(Trigger("Trigger2", importance = -1).also {
            it.conditions.add(DateCondition(Date(Date().time)))
//            it.actionSets.add(availableActionSets.first())
        })

        triggers.add(Trigger("Toggle trigger", importance = 1).also {
            it.conditions.add(TimerCondition(5L))
            availableActionSets.find { actionSet -> actionSet.name == "Toggle set" }
                ?.let { actionSet -> it.actionSets.add(actionSet) }
        })

        triggers.add(Trigger("Trigger3").also {
        })
    }
}