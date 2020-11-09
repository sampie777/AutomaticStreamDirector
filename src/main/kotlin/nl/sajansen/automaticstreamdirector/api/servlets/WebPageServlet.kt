package nl.sajansen.automaticstreamdirector.api.servlets

import nl.sajansen.automaticstreamdirector.api.getPathVariables
import nl.sajansen.automaticstreamdirector.api.respondWithContent
import nl.sajansen.automaticstreamdirector.api.respondWithHtml
import nl.sajansen.automaticstreamdirector.api.respondWithNotFound
import java.io.File
import java.util.logging.Logger
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Suppress("UNUSED_PARAMETER")
class WebPageServlet : HttpServlet() {
    private val logger = Logger.getLogger(ConfigApiServlet::class.java.name)

    operator fun Regex.contains(text: CharSequence?): Boolean = this.matches(text ?: "")
    private val assetsMatcher = """^/assets/(.*?)$""".toRegex()

    override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        logger.info("Processing ${request.method} request from : ${request.requestURI}")

        when (request.pathInfo) {
            "/" -> getIndex(request, response)
            in Regex(assetsMatcher.pattern) -> getAsset(response, request.pathInfo.getPathVariables(assetsMatcher))
            else -> respondWithNotFound(response)
        }
    }

    override fun doPost(request: HttpServletRequest, response: HttpServletResponse) {
        logger.info("Processing ${request.method} request from : ${request.requestURI}")

        when (request.pathInfo) {
            else -> respondWithNotFound(response)
        }
    }

    private fun getAsset(response: HttpServletResponse, params: List<String>) {
        val path = params[0]
        logger.info("Getting asset: $path")

        val content = WebPageServlet::class.java.getResource("/nl/sajansen/automaticstreamdirector/web/assets/$path")
            .readText(Charsets.UTF_8)

        val contentType = when(File(path).extension.toLowerCase()) {
            "css" -> "text/css"
            "js" -> "application/javascript"
            "xml" -> "text/xml"
            "pdf" -> "application/pdf"
            "gif" -> "image/gif"
            "jpeg" -> "image/jpeg"
            "jpg" -> "image/jpeg"
            "ico" -> "image/x-icon"
            "png" -> "image/png"
            else -> "text/plain"
        }

        respondWithContent(response, content, contentType)
    }

    private fun getIndex(request: HttpServletRequest, response: HttpServletResponse) {
        val html = WebPageServlet::class.java.getResource("/nl/sajansen/automaticstreamdirector/web/index.html")
            .readText(Charsets.UTF_8)
        respondWithHtml(response, html)
    }

}