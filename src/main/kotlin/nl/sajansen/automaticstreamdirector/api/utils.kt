package nl.sajansen.automaticstreamdirector.api

import nl.sajansen.automaticstreamdirector.jsonBuilder
import java.io.InputStream
import java.net.HttpURLConnection
import java.util.logging.Logger
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

    response.status = status
    response.contentType = "application/json"
    response.writer.println(json)
    logger.info("Response: $json")
}

fun respondWithHtml(
    response: HttpServletResponse,
    data: Any?,
    status: Int = HttpServletResponse.SC_OK
) {
    response.status = status
    response.contentType = "text/html"
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
