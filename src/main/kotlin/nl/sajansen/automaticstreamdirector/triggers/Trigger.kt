package nl.sajansen.automaticstreamdirector.triggers

import nl.sajansen.automaticstreamdirector.actions.ActionSet


interface Trigger {
    val name: String
    val importance: Int
    val conditions: ArrayList<Condition>
    val actionSets: ArrayList<ActionSet>
}