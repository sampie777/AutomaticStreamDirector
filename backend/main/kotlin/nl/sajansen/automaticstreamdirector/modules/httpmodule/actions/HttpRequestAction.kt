package nl.sajansen.automaticstreamdirector.modules.httpmodule.actions


import nl.sajansen.automaticstreamdirector.actions.Action
import org.eclipse.jetty.http.HttpMethod
import requestConnection
import java.util.logging.Logger

class HttpRequestAction(
    val url: String,
    val body: String? = null,
    val method: HttpMethod = HttpMethod.GET
) : Action {
    private val logger = Logger.getLogger(HttpRequestAction::class.java.name)

    override fun execute() {
        val connection = requestConnection(url, body, method)

        if (connection.responseCode != 200) {
            logger.warning("HttpRequestAction failed to connect to $url ($method)")
        }
    }

    override fun displayName(): String {
        return if (body != null) {
            "Send content to $url"
        } else {
            "Do $method request to $url"
        }
    }

    override fun toString() = displayName()
}