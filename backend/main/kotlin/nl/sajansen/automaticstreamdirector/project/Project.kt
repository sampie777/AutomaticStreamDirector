package nl.sajansen.automaticstreamdirector.project

import nl.sajansen.automaticstreamdirector.actions.ActionSet
import nl.sajansen.automaticstreamdirector.triggers.Trigger
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

        val actionSets = ActionSet.list()?.filterNotNull()

        if (actionSets == null) {
            logger.info("No actionSets found in database")
            return
        }

        availableActionSets.addAll(actionSets)
        logger.info("${actionSets.size} actionSets loaded from database")
//
//        availableActionSets.add(ActionSet("Action set 1").also {
//            it.actions.add(DelayAction(2500))
//            it.actions.add(HttpRequestAction("http://localhost:8080/api/v1/director/status"))
//        })
//
//        availableActionSets.add(ActionSet("Toggle set").also {
//            availableActionSets.add(ActionSet("To be toggled 1").also { set ->
//                set.actions.add(HttpRequestAction("http://localhost:8080/api/v1/actionsets/list"))
//                it.actions.add(ToggleAction(set, startToggledOn = true))
//            })
//
//            availableActionSets.add(ActionSet("To be toggled 1").also { set ->
//                set.actions.add(HttpRequestAction("http://localhost:8080/api/v1/triggers/list"))
//                it.actions.add(ToggleAction(set, startToggledOn = false))
//            })
//        })
    }

    private fun loadTriggers() {
        logger.info("Loading triggers")

        val triggers = Trigger.list()?.filterNotNull()

        if (triggers == null) {
            logger.info("No triggers found in database")
            return
        }

        this.triggers.addAll(triggers)
        logger.info("${triggers.size} triggers loaded from database")
        
//
//        triggers.add(Trigger("Trigger1").also {
//            it.conditions.add(ClockCondition(LocalTime.now()))
//            it.conditions.add(DateCondition(Date(Date().time - 24 * 60 * 60 * 1000)))
//            it.conditions.add(
//                HttpRequestResponseCodeCondition(
//                    "http://localhost:8080/api/v1/director/status",
//                    expectedCode = 200
//                )
//            )
//        })
//
//        triggers.add(Trigger("Trigger2", importance = -1).also {
//            it.conditions.add(DateCondition(Date(Date().time)))
////            it.actionSets.add(availableActionSets.first())
//        })
//
//        triggers.add(Trigger("Toggle trigger", importance = 1).also {
//            it.conditions.add(TimerCondition(5L))
//            availableActionSets.find { actionSet -> actionSet.name == "Toggle set" }
//                ?.let { actionSet -> it.actionSets.add(actionSet) }
//        })
//
//        triggers.add(Trigger("Trigger3").also {
//        })
    }
}