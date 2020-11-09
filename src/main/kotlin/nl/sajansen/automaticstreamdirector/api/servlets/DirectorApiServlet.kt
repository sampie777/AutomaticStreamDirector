package nl.sajansen.automaticstreamdirector.api.servlets


import nl.sajansen.automaticstreamdirector.Director
import nl.sajansen.automaticstreamdirector.api.respondWithJson
import nl.sajansen.automaticstreamdirector.api.respondWithNotFound
import java.util.logging.Logger
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class DirectorApiServlet : HttpServlet() {
    private val logger = Logger.getLogger(ConfigApiServlet::class.java.name)

    operator fun Regex.contains(text: CharSequence?): Boolean = this.matches(text ?: "")

    override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        logger.info("Processing ${request.method} request from : ${request.requestURI}")

        when (request.pathInfo) {
            "/start" -> getStart(response)
            "/stop" -> getStop(response)
            "/status" -> getStatus(response)
            else -> respondWithNotFound(response)
        }
    }

    override fun doPost(request: HttpServletRequest, response: HttpServletResponse) {
        logger.info("Processing ${request.method} request from : ${request.requestURI}")

        when (request.pathInfo) {
            else -> respondWithNotFound(response)
        }
    }

    private fun getStart(response: HttpServletResponse) {
        logger.info("Starting Director")

        Director.start()

        respondWithJson(response, "Director started")
    }

    private fun getStop(response: HttpServletResponse) {
        logger.info("Stopping Director")

        Director.stop()

        respondWithJson(response, "Director stopped")
    }

    private fun getStatus(response: HttpServletResponse) {
        respondWithJson(response, Director.isRunning())
    }
}