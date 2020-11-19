package nl.sajansen.automaticstreamdirector.api.servlets


import com.google.gson.Gson
import nl.sajansen.automaticstreamdirector.api.body
import nl.sajansen.automaticstreamdirector.api.json.ConditionJson
import nl.sajansen.automaticstreamdirector.api.json.FormDataJson
import nl.sajansen.automaticstreamdirector.api.json.StaticConditionJson
import nl.sajansen.automaticstreamdirector.api.respondWithJson
import nl.sajansen.automaticstreamdirector.api.respondWithNotFound
import nl.sajansen.automaticstreamdirector.modules.Modules
import nl.sajansen.automaticstreamdirector.triggers.Condition
import java.util.logging.Logger
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class ConditionsApiServlet : HttpServlet() {
    private val logger = Logger.getLogger(ConfigApiServlet::class.java.name)

    operator fun Regex.contains(text: CharSequence?): Boolean = this.matches(text ?: "")

    override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        logger.info("Processing ${request.method} request from : ${request.requestURI}")

        when (request.pathInfo) {
            "/list" -> getStaticConditions(response)
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

    private fun getStaticConditions(response: HttpServletResponse) {
        logger.info("Getting Static Conditions for modules")

        val conditions = Modules.conditions()

        respondWithJson(response, conditions.map(StaticConditionJson::from))
    }

    private fun postSave(request: HttpServletRequest, response: HttpServletResponse) {
        logger.info("Saving Condition")

        val json = request.body()
        val jsonObject = Gson().fromJson(json, FormDataJson::class.java)
        logger.info(jsonObject.toString())

        // Get and load class
        val className = jsonObject["className"]
        if (className == null) {
            logger.warning("Missing 'className' key in FormData: $json")
            return respondWithJson(response, "Missing 'className' key in FormData")
        }

        val clazz = try {
            Class.forName(className).enclosingClass
        } catch (e: ClassNotFoundException) {
            logger.severe("Class not found for name: $className")
            throw e
        }

        // Get and execute method
        val method = try {
            clazz.getDeclaredMethod("save", FormDataJson::class.java)
        } catch (e: NoSuchMethodException) {
            logger.severe("Class does not have 'save(data: FormDataJson): Any' method. Did you define this and declared it with @JvmStatic?")
            throw e
        }
        val result = method.invoke(null, jsonObject)
        logger.info("Result from save(): $result")

        // Process save result
        if (result is List<*>) {
            return respondWithJson(response, result)
        }
        if (result !is Condition) {
            return respondWithJson(response, "Something went wrong")
        }

        respondWithJson(response, result.run(ConditionJson::from))
    }
}