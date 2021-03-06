package nl.sajansen.automaticstreamdirector.config

import java.lang.reflect.Field
import java.lang.reflect.Modifier
import java.util.logging.Logger

object Config {
    private val logger = Logger.getLogger(Config.toString())

    // GUI
    var directorStatusUpdateInterval: Long = 20 // seconds

    // Director
    var updateInterval: Double = 1.0  // seconds

    // API
    var httpApiServerPort: Int = 8080

    fun load() {
        try {
            PropertyLoader.load()
            PropertyLoader.loadConfig(this::class.java)
        } catch (e: Exception) {
            logger.severe("Failed to load Config")
            e.printStackTrace()
        }
    }

    fun save() {
        try {
            if (PropertyLoader.saveConfig(this::class.java)) {
                PropertyLoader.save()
            }
        } catch (e: Exception) {
            logger.severe("Failed to save Config")
            e.printStackTrace()
        }
    }

    fun get(key: String): Any? {
        try {
            return javaClass.getDeclaredField(key).get(this)
        } catch (e: Exception) {
            logger.severe("Could not get config key $key")
            e.printStackTrace()
        }
        return null
    }

    fun set(key: String, value: String?) {
        try {
            val field = javaClass.getDeclaredField(key)
            field.isAccessible = true

            if (value == null) {
                field.set(null, null)
            } else {
                field.set(null, PropertyLoader.stringToTypedValue(value, field.name, field.type))
            }
        } catch (e: Exception) {
            logger.severe("Could not set config key $key")
            e.printStackTrace()
        }
    }

    fun enableWriteToFile(value: Boolean) {
        PropertyLoader.writeToFile = value
    }

    fun fields(): List<Field> {
        val fields = javaClass.declaredFields.filter {
            it.name != "INSTANCE" && it.name != "logger"
                    && Modifier.isStatic(it.modifiers)
        }
        fields.forEach { it.isAccessible = true }
        return fields
    }
}