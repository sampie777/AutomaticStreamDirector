package nl.sajansen.automaticstreamdirector.api.servlets

import nl.sajansen.automaticstreamdirector.api.respondWithHtml
import nl.sajansen.automaticstreamdirector.api.respondWithNotFound
import java.util.logging.Logger
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Suppress("UNUSED_PARAMETER")
class WebPageServlet : HttpServlet() {
    private val logger = Logger.getLogger(ConfigApiServlet::class.java.name)

    operator fun Regex.contains(text: CharSequence?): Boolean = this.matches(text ?: "")

    override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        logger.info("Processing ${request.method} request from : ${request.requestURI}")

        when (request.pathInfo) {
            "/" -> getIndex(request, response)
            else -> respondWithNotFound(response)
        }
    }

    override fun doPost(request: HttpServletRequest, response: HttpServletResponse) {
        logger.info("Processing ${request.method} request from : ${request.requestURI}")

        when (request.pathInfo) {
            else -> respondWithNotFound(response)
        }
    }

    private fun getIndex(request: HttpServletRequest, response: HttpServletResponse) {
        val html = WebPageServlet::class.java.getResource("/nl/sajansen/automaticstreamdirector/api/index.html").readText(Charsets.UTF_8)
        respondWithHtml(response, html)
    }

}