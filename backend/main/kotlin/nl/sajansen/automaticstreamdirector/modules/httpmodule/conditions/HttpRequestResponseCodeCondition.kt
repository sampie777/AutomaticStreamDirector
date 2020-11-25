package nl.sajansen.automaticstreamdirector.modules.httpmodule.conditions


import com.google.gson.Gson
import nl.sajansen.automaticstreamdirector.api.json.FormDataJson
import nl.sajansen.automaticstreamdirector.common.FormComponent
import nl.sajansen.automaticstreamdirector.db.entities.ConditionEntity
import nl.sajansen.automaticstreamdirector.triggers.Condition
import nl.sajansen.automaticstreamdirector.triggers.StaticCondition
import org.eclipse.jetty.http.HttpMethod
import requestConnection
import java.util.logging.Logger

class HttpRequestResponseCodeCondition(
    val url: String,
    val expectedCode: Int,
    val body: String? = null,
    val method: HttpMethod = HttpMethod.GET,
    override var id: Long? = null,
) : Condition() {
    private val logger = Logger.getLogger(HttpRequestResponseCodeCondition::class.java.name)

    override fun check(): Boolean {
        val connection = requestConnection(url, body, method)

        logger.info("HttpRequestResultCondition result code: ${connection.responseCode}")
        return expectedCode == connection.responseCode
    }

    override fun displayName(): String {
        return "If $url ($method) gives response code $expectedCode"
    }

    override fun getDataSet(): Any? = DbDataSet(
            url = url,
            expectedCode = expectedCode,
            body = body,
            method = method,
        )

    data class DbDataSet(
        val url: String,
        val expectedCode: Int,
        val body: String?,
        val method: HttpMethod,
    )

    companion object : StaticCondition {
        override val name: String = HttpRequestResponseCodeCondition::class.java.simpleName
        override val previewText: String = "If URL responds with code..."

        override fun formComponents() = listOf(
            FormComponent("url", "URL", FormComponent.Type.URL, required = true),
            FormComponent(
                "method", "HTTP method", FormComponent.Type.Select, required = true,
                selectValues = listOf(
                    FormComponent.SelectOption(HttpMethod.GET.name),
                    FormComponent.SelectOption(HttpMethod.POST.name),
                    FormComponent.SelectOption(HttpMethod.PUT.name),
                    FormComponent.SelectOption(HttpMethod.DELETE.name),
                )
            ),
            FormComponent("body", "Send with body", FormComponent.Type.TextArea),
            FormComponent("expectedCode", "Expected response code", FormComponent.Type.Number),
        )

        @JvmStatic
        override fun save(data: FormDataJson): Any {
            val id = data["id"]?.toLongOrNull()
            val url = data["url"] ?: ""
            val expectedCode = data["expectedCode"]?.toIntOrNull()
            val body = data["body"]
            val method = data["method"] ?: ""

            val validationErrors = arrayListOf<String>()

            if (url.isEmpty()) {
                validationErrors.add("Enter a valid URL")
            }

            if (expectedCode == null) {
                validationErrors.add("Response code to expect may not be null")
            } else if (expectedCode <= 0) {
                validationErrors.add("Response code to expect must be greater than 0")
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

            HttpRequestResponseCodeCondition(
                id = id,
                url = url,
                expectedCode = expectedCode!!,
                body = body,
                method = httpMethod!!,
            ).also {
                saveOrUpdate(it)
                return it
            }
        }

        override fun fromDbEntity(conditionEntity: ConditionEntity): Condition? {
            val data = Gson().fromJson(conditionEntity.dataString, DbDataSet::class.java)

            return HttpRequestResponseCodeCondition(
                url = data.url,
                expectedCode = data.expectedCode,
                body = data.body,
                method = data.method,
                id = conditionEntity.id,
            )
        }
    }
}