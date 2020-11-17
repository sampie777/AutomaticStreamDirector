package nl.sajansen.automaticstreamdirector

import nl.sajansen.automaticstreamdirector.api.ApiServer
import nl.sajansen.automaticstreamdirector.config.Config
import nl.sajansen.automaticstreamdirector.db.DB
import nl.sajansen.automaticstreamdirector.modules.Modules
import nl.sajansen.automaticstreamdirector.project.Project
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

    DB.connect()

    Project.load()

    ApiServer.start()

    Director.start()
}


fun exitApplication() {
    logger.info("Shutting down application")

    ApiServer.stop()

    DB.disconnect()

    logger.info("Shutdown finished")
    exitProcess(0)
}