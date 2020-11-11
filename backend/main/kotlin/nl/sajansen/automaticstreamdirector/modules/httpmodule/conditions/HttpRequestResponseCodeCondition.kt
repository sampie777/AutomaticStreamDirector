package nl.sajansen.automaticstreamdirector.modules.httpmodule.conditions


import nl.sajansen.automaticstreamdirector.triggers.Condition
import org.eclipse.jetty.http.HttpMethod
import requestConnection
import java.util.logging.Logger

class HttpRequestResponseCodeCondition(
    val url: String,
    val expectedCode: Int,
    val body: String? = null,
    val method: HttpMethod = HttpMethod.GET
) : Condition {
    private val logger = Logger.getLogger(HttpRequestResponseCodeCondition::class.java.name)

    override fun check(): Boolean {
        val connection = requestConnection(url, body, method)

        logger.info("HttpRequestResultCondition result code: ${connection.responseCode}")
        return expectedCode == connection.responseCode
    }

    override fun displayName(): String {
        return "If $url ($method) gives response code $expectedCode"
    }

    override fun toString() = displayName()
}