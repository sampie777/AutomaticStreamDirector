package nl.sajansen.automaticstreamdirector.api


import nl.sajansen.automaticstreamdirector.api.servlets.ActionSetsApiServlet
import nl.sajansen.automaticstreamdirector.api.servlets.ConfigApiServlet
import nl.sajansen.automaticstreamdirector.api.servlets.TriggersApiServlet
import nl.sajansen.automaticstreamdirector.api.servlets.WebPageServlet
import nl.sajansen.automaticstreamdirector.config.Config
import org.eclipse.jetty.server.Server
import org.eclipse.jetty.server.handler.HandlerList
import org.eclipse.jetty.servlet.ServletContextHandler
import java.util.logging.Logger

object ApiServer {
    private val logger = Logger.getLogger(ApiServer::class.java.name)

    private val server: Server = Server(Config.httpApiServerPort)

    init {
        val handlers = HandlerList()
        server.handler = handlers

        val apiServletContextHandler = ServletContextHandler()
        apiServletContextHandler.contextPath = "/api/v1"

        logger.fine("Registering API endpoints")
        apiServletContextHandler.addServlet(ConfigApiServlet::class.java, "/config/*")
        apiServletContextHandler.addServlet(TriggersApiServlet::class.java, "/triggers/*")
        apiServletContextHandler.addServlet(ActionSetsApiServlet::class.java, "/actionsets/*")
        handlers.addHandler(apiServletContextHandler)

        val webPageServletContextHandler = ServletContextHandler()
        webPageServletContextHandler.contextPath = ""
        webPageServletContextHandler.addServlet(WebPageServlet::class.java, "/*")
        handlers.addHandler(webPageServletContextHandler)
    }

    fun start() {
        logger.info("Starting API server...")
        server.start()
        logger.info("API server started on: ${url()}")
    }

    fun stop() {
        logger.info("Stopping API server...")
        server.stop()
        logger.info("API server stopped")
    }

    fun url(): String = server.uri.scheme + "://" + server.uri.authority
}