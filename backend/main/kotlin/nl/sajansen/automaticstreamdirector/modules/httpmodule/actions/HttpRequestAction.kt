package nl.sajansen.automaticstreamdirector.modules.httpmodule.actions


import com.google.gson.Gson
import nl.sajansen.automaticstreamdirector.actions.Action
import nl.sajansen.automaticstreamdirector.actions.StaticAction
import nl.sajansen.automaticstreamdirector.api.json.FormDataJson
import nl.sajansen.automaticstreamdirector.common.FormComponent
import nl.sajansen.automaticstreamdirector.db.entities.ActionEntity
import nl.sajansen.automaticstreamdirector.jsonBuilder
import org.eclipse.jetty.http.HttpMethod
import requestConnection
import java.util.logging.Logger

class HttpRequestAction(
    val url: String,
    val body: String? = null,
    val method: HttpMethod = HttpMethod.GET,
    override var id: Long? = null,
) : Action() {
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

    override fun getDbDataSet(): String? = jsonBuilder(prettyPrint = false).toJson(
        DbDataSet(url, body, method)
    )

    data class DbDataSet(
        val url: String,
        val body: String?,
        val method: HttpMethod,
    )

    companion object : StaticAction {
        override val name: String = HttpRequestAction::class.java.simpleName
        override val previewText: String = "Do request / send content to URL..."

        override fun formComponents(): List<FormComponent> = listOf(
            FormComponent("url", "URL", FormComponent.Type.URL, required = true),
            FormComponent("sendBody", "Send with body?", FormComponent.Type.Checkbox),
            FormComponent("body", "Body", FormComponent.Type.Text),
            FormComponent("method", "Method", FormComponent.Type.Text, required = true),
        )

        @JvmStatic
        override fun save(data: FormDataJson): Any {
            val url = data["url"] ?: ""
            val sendBody = data["sendBody"] ?: "" == "on"
            val body = data["body"] ?: ""
            val method = data["method"] ?: ""

            val errors = arrayListOf<String>()
            if (url.isEmpty()) {
                errors.add("URL cannot be empty")
            }

            val httpMethod = try {
                HttpMethod.valueOf(method.toUpperCase())
            } catch (e: Exception) {
                errors.add("Invalid method")
                e.printStackTrace()
                null
            }

            if (errors.isNotEmpty()) {
                return errors
            }

            HttpRequestAction(url, if (sendBody) body else null, httpMethod!!)
                .also {
                    saveOrUpdate(it)
                    return it
                }
        }

        override fun fromDbEntity(actionEntity: ActionEntity): Action? {
            val data = Gson().fromJson(actionEntity.dataString, DbDataSet::class.java)

            return HttpRequestAction(
                data.url,
                data.body,
                data.method,
                id = actionEntity.id,
            )
        }
    }
}