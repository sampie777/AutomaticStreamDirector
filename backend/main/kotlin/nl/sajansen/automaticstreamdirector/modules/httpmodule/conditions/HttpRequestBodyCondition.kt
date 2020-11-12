package nl.sajansen.automaticstreamdirector.modules.httpmodule.conditions


import nl.sajansen.automaticstreamdirector.api.body
import nl.sajansen.automaticstreamdirector.api.errorBody
import nl.sajansen.automaticstreamdirector.triggers.Condition
import nl.sajansen.automaticstreamdirector.triggers.StaticCondition
import org.eclipse.jetty.http.HttpMethod
import requestConnection
import java.util.logging.Logger

class HttpRequestBodyCondition(
    val url: String,
    val expectedBody: String,
    val body: String? = null,
    val method: HttpMethod = HttpMethod.GET
) : Condition {
    private val logger = Logger.getLogger(HttpRequestBodyCondition::class.java.name)

    override fun check(): Boolean {
        val connection = requestConnection(url, body, method)

        if (connection.responseCode != 200) {
            logger.warning("HttpRequestBodyCondition failed to connect to $url ($method)")
        }

        val responseBody = try {
            connection.body()
        } catch (e: Exception) {
            connection.errorBody() ?: ""
        }

        logger.info("HttpRequestBodyCondition response body: $responseBody")

        return expectedBody.trim() == responseBody.trim()
    }

    override fun displayName(): String {
        return "If $url ($method) response matches $expectedBody"
    }

    override fun toString() = displayName()

    companion object : StaticCondition {
        override fun name(): String = HttpRequestBodyCondition::class.java.simpleName
        override fun previewText(): String = "If URL responds with content..."
    }
}