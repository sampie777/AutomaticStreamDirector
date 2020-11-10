package nl.sajansen.automaticstreamdirector.modules

object Modules {

    val modules: ArrayList<Module> = arrayListOf()

    fun loadAll() {
        modules.clear()
        ModuleLoader().loadAll()
    }
}