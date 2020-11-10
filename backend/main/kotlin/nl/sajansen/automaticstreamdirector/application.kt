package nl.sajansen.automaticstreamdirector

import nl.sajansen.automaticstreamdirector.api.ApiServer
import nl.sajansen.automaticstreamdirector.config.Config
import nl.sajansen.automaticstreamdirector.modules.Modules
import nl.sajansen.automaticstreamdirector.modules.timingmodule.conditions.ClockCondition
import nl.sajansen.automaticstreamdirector.project.Project
import nl.sajansen.automaticstreamdirector.triggers.Trigger
import java.time.LocalTime
import java.util.logging.Logger
import kotlin.system.exitProcess


private val logger = Logger.getLogger("Application")

@Suppress("UNUSED_PARAMETER")
fun main(args: Array<String>) {
    logger.info("Starting application ${ApplicationInfo.artifactId}:${ApplicationInfo.version}")
    logger.info("Executing JAR directory: " + getCurrentJarDirectory(ApplicationInfo).absolutePath)

    if ("--virtualConfig" !in args) {
        Config.enableWriteToFile(true)
    }
    Config.load()
    Config.save()

    Modules.loadAll()

    ApiServer.start()

    Director.start()

    val trigger = Trigger("Trigger1")
    trigger.conditions.add(ClockCondition(LocalTime.now()))
    trigger.conditions.add(ClockCondition(LocalTime.now()))
    Project.triggers.add(trigger)
    Project.triggers.add(Trigger("Trigger2", importance = 1))
    Project.triggers.add(Trigger("Trigger3"))
}


fun exitApplication() {
    logger.info("Shutting down application")

    ApiServer.stop()

    logger.info("Shutdown finished")
    exitProcess(0)
}