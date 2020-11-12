package nl.sajansen.automaticstreamdirector.modules

import nl.sajansen.automaticstreamdirector.actions.StaticAction
import nl.sajansen.automaticstreamdirector.triggers.StaticCondition

interface Module {
    val name: String

    fun actions(): List<StaticAction>
    fun conditions(): List<StaticCondition>
}