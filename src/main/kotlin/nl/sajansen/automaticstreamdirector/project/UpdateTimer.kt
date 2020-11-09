package nl.sajansen.automaticstreamdirector.project

import nl.sajansen.automaticstreamdirector.Director
import nl.sajansen.automaticstreamdirector.config.Config
import java.util.*
import java.util.logging.Logger

object UpdateTimer {
    private val logger = Logger.getLogger(UpdateTimer::class.java.name)

    private var timer = Timer()

    @Volatile
    private var isUpdating = false

    fun restart() {
        stop()
        start()
    }

    private fun start() {
        logger.info("Scheduling UpdateTimer every ${Config.updateInterval} seconds")
        timer.schedule(object : TimerTask() {
            override fun run() {
                updateTimerStep()
            }
        }, 0, (Config.updateInterval * 1000).toLong())
    }

    private fun updateTimerStep() {
        if (isUpdating) {
            logger.info("UpdateTimer is still updating...")
            return
        }

        isUpdating = true
        Director.update()
        isUpdating = false
    }

    fun stop() {
        try {
            logger.info("Trying to cancel UpdateTimer")
            timer.cancel()
            timer = Timer()
            logger.info("UpdateTimer canceled")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}