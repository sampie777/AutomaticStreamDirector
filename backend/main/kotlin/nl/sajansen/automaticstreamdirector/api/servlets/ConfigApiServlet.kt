package nl.sajansen.automaticstreamdirector.api.servlets


import com.google.gson.Gson
import nl.sajansen.automaticstreamdirector.api.body
import nl.sajansen.automaticstreamdirector.api.getPathVariables
import nl.sajansen.automaticstreamdirector.api.json.ConfigJson
import nl.sajansen.automaticstreamdirector.api.json.ConfigJsonItem
import nl.sajansen.automaticstreamdirector.api.json.FormDataJson
import nl.sajansen.automaticstreamdirector.api.respondWithJson
import nl.sajansen.automaticstreamdirector.api.respondWithNotFound
import nl.sajansen.automaticstreamdirector.common.FormComponent
import nl.sajansen.automaticstreamdirector.config.Config
import java.util.logging.Logger
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class ConfigApiServlet : HttpServlet() {
    private val logger = Logger.getLogger(ConfigApiServlet::class.java.name)

    operator fun Regex.contains(text: CharSequence?): Boolean = this.matches(text ?: "")
    private val configKeyMatcher = """^/key/(\w+)$""".toRegex()

    override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        logger.info("Processing ${request.method} request from : ${request.requestURI}")

        when (request.pathInfo) {
            "/list" -> getList(response)
            in Regex(configKeyMatcher.pattern) -> getKeyValue(
                response,
                request.pathInfo.getPathVariables(configKeyMatcher)
            )
            else -> respondWithNotFound(response)
        }
    }

    override fun doPost(request: HttpServletRequest, response: HttpServletResponse) {
        logger.info("Processing ${request.method} request from : ${request.requestURI}")

        when (request.pathInfo) {
            "/save" -> postSave(request, response)
            else -> respondWithNotFound(response)
        }
    }

    private fun getList(response: HttpServletResponse) {
        logger.info("Getting Config values list")

        val configJson = ConfigJson(
            frontend = listOf(
                createConfigJsonItem("directorStatusUpdateInterval", "Director status update interval (sec.)"),
            ),
            backend = listOf(
                createConfigJsonItem("updateInterval", "Check triggers interval (sec.)"),
                createConfigJsonItem("httpApiServerPort", "API server port"),
            )
        )

        respondWithJson(response, configJson)
    }

    private fun getKeyValue(response: HttpServletResponse, params: List<String>) {
        val key = params[0]
        logger.info("Getting Config value for key: $key")

        val jsonObject = createConfigJsonItem(key)

        respondWithJson(response, jsonObject)
    }

    private fun postSave(request: HttpServletRequest, response: HttpServletResponse) {
        logger.info("Saving Config values")

        val json = request.body()
        val jsonObject = Gson().fromJson(json, FormDataJson::class.java)
        logger.info(jsonObject.toString())

        val validationErrors = validateConfigList(jsonObject)

        if (validationErrors.isNotEmpty()) {
            return respondWithJson(response, validationErrors)
        }

        jsonObject.forEach { (key, value) ->
            logger.info("Saving config value: $key = $value")
            Config.set(key, value)
        }

        respondWithJson(response, "ok")
    }

    private fun validateConfigList(data: FormDataJson): List<String> {
        logger.info("Validating config values")
        val validationErrors = arrayListOf<String>()

        if (data["directorStatusUpdateInterval"]?.toLongOrNull().let { it == null || it < 1 }) {
            validationErrors.add("Minimum interval value for director status update is 1 second")
        }

        if (data["updateInterval"]?.toDoubleOrNull().let { it == null || it < 1 }) {
            validationErrors.add("Minimum interval value for director trigger checks is 1 second. Value given: ${data["updateInterval"]?.toDoubleOrNull()}")
        }

        if (data["httpApiServerPort"]?.toLongOrNull().let { it == null || it <= 0 }) {
            validationErrors.add("API port number must be greater than 0")
        }

        return validationErrors
    }

//    private fun postKeyValue(request: HttpServletRequest, response: HttpServletResponse, params: List<String>) {
//        val key = params[0]
//        logger.info("Setting new Config value for key: $key")
//
//        val json = request.inputStream.bufferedReader().readText()
//        val pair = Gson().fromJson(json, JsonConfigPair::class.java)
//        logger.info(pair.toString())
//
//        if (key != pair.key) {
//            logger.info("Request parameter key doesn't match request body key")
//            respondWithJson(response, null)
//            return
//        }
//
//        logger.info("New value for key is: ${pair.value}")
//        Config.set(pair.key, pair.value)
//
//        getKeyValue(response, params)
//    }

    private fun createConfigJsonItem(key: String, labelText: String? = null): ConfigJsonItem {
        val value = Config.get(key)

        val formComponent = if (labelText == null) {
            null
        } else {
            val type = when (value) {
                is String -> FormComponent.Type.Text
                is Number -> FormComponent.Type.Number
                is Boolean -> FormComponent.Type.Checkbox
                else -> FormComponent.Type.Text
            }

            FormComponent(
                name = key,
                defaultValue = value,
                labelText = labelText,
                type = type,
            )
        }

        return ConfigJsonItem(
            key = key,
            value = value,
            formComponent = formComponent
        )
    }
}