package nl.sajansen.automaticstreamdirector.db

import org.hibernate.SessionFactory
import org.hibernate.cfg.Configuration
import java.sql.SQLException
import java.util.logging.Logger

object DB {
    private val logger = Logger.getLogger(DB::class.java.name)
    private var connection: SessionFactory? = null
    fun connection() = connection!!

    fun connect() {
        logger.info("Connecting to database...")
//        try {
//            connection = DriverManager.getConnection("jdbc:sqlite:./automaticstreamdirector.db")
//        } catch (e: SQLException) {
//            e.printStackTrace()
//            return
//        }
//
//        if (connection == null) {
//            logger.severe("Failed to connect to database")
//            return
//        }
//        logger.info("Connected to database")


        try {
            connection = Configuration().configure().buildSessionFactory()
        } catch (e: Exception) {
            logger.severe("Failed to connect to database")
            e.printStackTrace()
            return
        }
    }

    fun disconnect() {
        logger.info("Disconnecting database")
        try {
            connection?.close()
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }
}