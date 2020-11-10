package nl.sajansen.automaticstreamdirector.api

import nl.sajansen.automaticstreamdirector.jsonBuilder
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.servlets.CrossOriginFilter
import java.io.InputStream
import java.net.HttpURLConnection
import java.util.*
import java.util.logging.Logger
import javax.servlet.DispatcherType
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

private val logger: Logger = Logger.getLogger("utils")

data class JsonErrorObject(val title: String, val detail: String, val status: Int)

data class JsonSuccessResponse(val data: Any?)
data class JsonErrorResponse(val errors: List<JsonErrorObject>)

fun respondWithJson(
    response: HttpServletResponse,
    data: Any?,
    status: Int = HttpServletResponse.SC_OK
) {
    val json = jsonBuilder().toJson(JsonSuccessResponse(data = data))
    respondWithContent(response, json, "application/json", status)
}

fun respondWithHtml(
    response: HttpServletResponse,
    data: Any?,
    status: Int = HttpServletResponse.SC_OK
) {
    respondWithContent(response, data, "text/html", status)
}

fun respondWithContent(
    response: HttpServletResponse,
    data: Any?,
    contentType: String = "text/plain",
    status: Int = HttpServletResponse.SC_OK
) {
    response.status = status
    response.contentType = contentType
    response.writer.println(data)
    logger.fine("Response: $data")
}

fun respondWithNotFound(response: HttpServletResponse) {
    response.status = HttpServletResponse.SC_NOT_FOUND
    response.writer.println("Not Found")
}

fun String.getPathVariables(regex: Regex): List<String> = regex.find(this)?.destructured?.toList() ?: emptyList()

fun HttpServletRequest.getQueryParameter(key: String, default: Any?): Any? {
    val param = this.parameterMap[key] ?: return default
    return param[0] ?: default
}

fun HttpURLConnection.body() = (this.content as InputStream).bufferedReader().readText()
fun HttpURLConnection.errorBody() = this.errorStream?.bufferedReader()?.readText()
fun HttpServletRequest.body() = this.inputStream.bufferedReader().readText()

fun ServletContextHandler.allowCrossOrigin() {
    val filterHolder = this.addFilter(
        CrossOriginFilter::class.java,
        "/*",
        EnumSet.of(DispatcherType.REQUEST)
    )

    filterHolder.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*")
    filterHolder.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_CREDENTIALS_HEADER, "true")
    filterHolder.setInitParameter(
        CrossOriginFilter.ACCESS_CONTROL_ALLOW_METHODS_HEADER,
        "OPTIONS, HEAD, GET, POST, PUT, DELETE"
    )
    filterHolder.setInitParameter(
        CrossOriginFilter.ACCESS_CONTROL_ALLOW_HEADERS_HEADER,
        "X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept"
    )
}