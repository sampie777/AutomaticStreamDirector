package nl.sajansen.automaticstreamdirector.modules

import nl.sajansen.automaticstreamdirector.modules.builtinmodule.BuiltInModule
import nl.sajansen.automaticstreamdirector.modules.httpmodule.HttpModule
import nl.sajansen.automaticstreamdirector.modules.timingmodule.TimingModule

class ModuleLoader {

    fun loadAll() {
        Modules.modules.add(BuiltInModule())
        Modules.modules.add(TimingModule())
        Modules.modules.add(HttpModule())
    }
}