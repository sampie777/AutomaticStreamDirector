package nl.sajansen.automaticstreamdirector.modules.httpmodule.conditions


import com.google.gson.Gson
import nl.sajansen.automaticstreamdirector.api.body
import nl.sajansen.automaticstreamdirector.api.errorBody
import nl.sajansen.automaticstreamdirector.api.json.FormDataJson
import nl.sajansen.automaticstreamdirector.common.FormComponent
import nl.sajansen.automaticstreamdirector.db.entities.ConditionEntity
import nl.sajansen.automaticstreamdirector.jsonBuilder
import nl.sajansen.automaticstreamdirector.triggers.Condition
import nl.sajansen.automaticstreamdirector.triggers.StaticCondition
import org.eclipse.jetty.http.HttpMethod
import requestConnection
import java.util.logging.Logger

class HttpRequestBodyCondition(
    val url: String,
    val expectedBody: String,
    val body: String? = null,
    val method: HttpMethod = HttpMethod.GET,
    override var id: Long? = null,
) : Condition() {
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

    override fun getDbDataSet(): String? = jsonBuilder(prettyPrint = false).toJson(
        DbDataSet(
            url = url,
            expectedBody = expectedBody,
            body = body,
            method = method,
        )
    )

    data class DbDataSet(
        val url: String,
        val expectedBody: String,
        val body: String?,
        val method: HttpMethod,
    )

    companion object : StaticCondition {
        override val name: String = HttpRequestBodyCondition::class.java.simpleName
        override val previewText: String = "If URL responds with content..."

        override fun formComponents() = listOf(
            FormComponent("url", "URL", FormComponent.Type.URL),
            FormComponent("body", "Send with body", FormComponent.Type.TextArea),
            FormComponent(
                "method", "HTTP method", FormComponent.Type.Select,
                selectValues = listOf(
                    FormComponent.SelectOption(HttpMethod.GET.name),
                    FormComponent.SelectOption(HttpMethod.POST.name),
                    FormComponent.SelectOption(HttpMethod.PUT.name),
                    FormComponent.SelectOption(HttpMethod.DELETE.name),
                )
            ),
            FormComponent("expectedBody", "Expected response body", FormComponent.Type.TextArea),
        )

        @JvmStatic
        override fun save(data: FormDataJson): Any {
            val url = data["url"] ?: ""
            val expectedBody = data["expectedBody"]
            val body = data["body"]
            val method = data["method"] ?: ""

            val validationErrors = arrayListOf<String>()

            if (url.isEmpty()) {
                validationErrors.add("Enter a valid URL")
            }

            if (expectedBody == null) {
                validationErrors.add("Response body to expect may not be null")
            }

            val httpMethod = try {
                HttpMethod.valueOf(method.toUpperCase())
            } catch (e: Exception) {
                validationErrors.add("Invalid method")
                e.printStackTrace()
                null
            }

            if (validationErrors.isNotEmpty()) {
                return validationErrors
            }

            HttpRequestBodyCondition(
                url = url,
                expectedBody = expectedBody!!,
                body = body,
                method = httpMethod!!,
            ).also {
                saveOrUpdate(it)
                return it
            }
        }

        override fun fromDbEntity(conditionEntity: ConditionEntity): Condition? {
            val data = Gson().fromJson(conditionEntity.dataString, DbDataSet::class.java)

            return HttpRequestBodyCondition(
                url = data.url,
                expectedBody = data.expectedBody,
                body = data.body,
                method = data.method,
                id = conditionEntity.id,
            )
        }
    }
}