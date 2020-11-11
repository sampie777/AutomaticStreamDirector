package nl.sajansen.automaticstreamdirector.modules.timingmodule


import nl.sajansen.automaticstreamdirector.modules.Module
import nl.sajansen.automaticstreamdirector.modules.httpmodule.HttpModule
import java.util.logging.Logger

class TimingModule : Module {
    private val logger = Logger.getLogger(HttpModule::class.java.name)

    override val name: String = HttpModule::class.java.name
}