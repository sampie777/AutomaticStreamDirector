package nl.sajansen.automaticstreamdirector.api.servlets


import com.google.gson.Gson
import nl.sajansen.automaticstreamdirector.api.body
import nl.sajansen.automaticstreamdirector.api.getPathVariables
import nl.sajansen.automaticstreamdirector.api.json.ConditionJson
import nl.sajansen.automaticstreamdirector.api.json.FormDataJson
import nl.sajansen.automaticstreamdirector.api.json.StaticConditionJson
import nl.sajansen.automaticstreamdirector.api.respondWithJson
import nl.sajansen.automaticstreamdirector.api.respondWithNotFound
import nl.sajansen.automaticstreamdirector.modules.Modules
import nl.sajansen.automaticstreamdirector.project.Project
import nl.sajansen.automaticstreamdirector.triggers.Condition
import java.util.logging.Logger
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class ConditionsApiServlet : HttpServlet() {
    private val logger = Logger.getLogger(ConfigApiServlet::class.java.name)

    operator fun Regex.contains(text: CharSequence?): Boolean = this.matches(text ?: "")
    private val getIdMatcher = """^/(\d+)$""".toRegex()
    private val editIdMatcher = """^/edit/(\d+)$""".toRegex()

    override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        logger.info("Processing ${request.method} request from : ${request.requestURI}")

        when (request.pathInfo) {
            "/list" -> getStaticConditions(response)
            in Regex(getIdMatcher.pattern) -> getById(
                response,
                request.pathInfo.getPathVariables(getIdMatcher)
            )
            in Regex(editIdMatcher.pattern) -> getStaticConditionForId(
                response,
                request.pathInfo.getPathVariables(editIdMatcher)
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

    private fun getStaticConditions(response: HttpServletResponse) {
        logger.info("Getting Static Conditions for modules")

        val conditions = Modules.conditions()

        respondWithJson(response, conditions.map(StaticConditionJson::from))
    }

    private fun getStaticConditionForId(response: HttpServletResponse, params: List<String>) {
        val id = params[0].toLongOrNull()
        logger.info("Get StaticCondition for condition with id: $id")

        if (id == null || id < 0) {
            logger.info("Invalid condition id")
            return respondWithNotFound(response)
        }

        val condition = Condition.get(id) ?: return respondWithNotFound(response)

        val staticCondition = Modules.conditions().find { it::class.java.enclosingClass == condition::class.java }
        if (staticCondition == null) {
            logger.info("StaticCondition not found for condition class: ${condition::class.java}")
            return respondWithNotFound(response)
        }

        respondWithJson(response, staticCondition.run(StaticConditionJson::from))
    }

    private fun getById(response: HttpServletResponse, params: List<String>) {
        val id = params[0].toLong()
        logger.info("Getting Condition with id: $id")

        val condition = Project.triggers
            .flatMap { it.conditions }
            .find { it.id == id }

        if (condition == null) {
            logger.info("Could not find Condition with id: $id")
            return respondWithJson(response, null)
        }

        respondWithJson(response, condition.run(ConditionJson::from))
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

        updateProjectState(result)

        respondWithJson(response, result.run(ConditionJson::from))
    }

    fun updateProjectState(condition: Condition) {
        if (condition.id == null) {
            return
        }

        val trigger = Project.triggers
            .find { trigger -> trigger.conditions.any { it.id == condition.id } } ?: return

        val index = trigger.conditions.indexOfFirst { it.id == condition.id }
        trigger.conditions[index] = condition
    }
}