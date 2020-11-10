package nl.sajansen.automaticstreamdirector.modules.timingmodule


import nl.sajansen.automaticstreamdirector.modules.Module
import java.util.logging.Logger

class TimingModule : Module {
    private val logger = Logger.getLogger(TimingModule::class.java.name)

    override val name: String = TimingModule::class.java.name
}