package nl.sajansen.automaticstreamdirector.mocks


import nl.sajansen.automaticstreamdirector.modules.Module
import java.util.logging.Logger

class ModuleMock(override val name: String = "ModuleMock") : Module {
    private val logger = Logger.getLogger(ModuleMock::class.java.name)
}