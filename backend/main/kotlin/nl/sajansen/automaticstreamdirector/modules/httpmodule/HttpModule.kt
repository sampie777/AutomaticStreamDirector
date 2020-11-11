package nl.sajansen.automaticstreamdirector.modules.httpmodule


import nl.sajansen.automaticstreamdirector.modules.Module
import java.util.logging.Logger

class HttpModule : Module {
    private val logger = Logger.getLogger(HttpModule::class.java.name)

    override val name: String = HttpModule::class.java.name
}