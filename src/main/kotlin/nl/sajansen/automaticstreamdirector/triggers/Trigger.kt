package nl.sajansen.automaticstreamdirector.triggers

import nl.sajansen.automaticstreamdirector.actions.ActionSet


data class Trigger (
    val name: String,
    val importance: Int = 0,
    val conditions: ArrayList<Condition> = arrayListOf(),
    val actionSets: ArrayList<ActionSet> = arrayListOf(),
)