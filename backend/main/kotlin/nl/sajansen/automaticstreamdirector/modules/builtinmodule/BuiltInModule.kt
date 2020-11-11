package nl.sajansen.automaticstreamdirector.modules.builtinmodule


import nl.sajansen.automaticstreamdirector.modules.Module
import java.util.logging.Logger

class BuiltInModule : Module {
    private val logger = Logger.getLogger(BuiltInModule::class.java.name)

    override val name: String = BuiltInModule::class.java.name
}