package nl.sajansen.automaticstreamdirector.modules

import nl.sajansen.automaticstreamdirector.actions.StaticAction
import nl.sajansen.automaticstreamdirector.triggers.StaticCondition

object Modules {

    val modules: ArrayList<Module> = arrayListOf()

    fun loadAll() {
        modules.clear()
        ModuleLoader().loadAll()
    }

    fun actions(): List<StaticAction> {
        return modules.flatMap { module -> module.actions() }
    }

    fun conditions(): List<StaticCondition> {
        return modules.flatMap { module -> module.conditions() }
    }
}