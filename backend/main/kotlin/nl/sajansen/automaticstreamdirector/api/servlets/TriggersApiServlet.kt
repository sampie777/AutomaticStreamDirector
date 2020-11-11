package nl.sajansen.automaticstreamdirector.api.servlets


import com.google.gson.Gson
import nl.sajansen.automaticstreamdirector.api.body
import nl.sajansen.automaticstreamdirector.api.getPathVariables
import nl.sajansen.automaticstreamdirector.api.json.TriggerJson
import nl.sajansen.automaticstreamdirector.api.respondWithJson
import nl.sajansen.automaticstreamdirector.api.respondWithNotFound
import nl.sajansen.automaticstreamdirector.project.Project
import java.util.logging.Logger
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class TriggersApiServlet : HttpServlet() {
    private val logger = Logger.getLogger(ConfigApiServlet::class.java.name)

    operator fun Regex.contains(text: CharSequence?): Boolean = this.matches(text ?: "")
    private val triggerNameMatcher = """^/(\w+)$""".toRegex()

    override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        logger.info("Processing ${request.method} request from : ${request.requestURI}")

        when (request.pathInfo) {
            "/list" -> getList(response)
            in Regex(triggerNameMatcher.pattern) -> getByName(response, request.pathInfo.getPathVariables(triggerNameMatcher))
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
        logger.info("Getting Triggers list")

        val list = Project.triggers

        respondWithJson(response, list.map(TriggerJson::from))
    }

    private fun getByName(response: HttpServletResponse, params: List<String>) {
        val name = params[0]
        logger.info("Getting Trigger with name: $name")

        val trigger = Project.triggers.find { it.name == name }

        if (trigger == null) {
            logger.info("Could not find Trigger with name: $name")
            return respondWithJson(response, null)
        }

        respondWithJson(response, trigger.run(TriggerJson::from))
    }

    private fun postSave(request: HttpServletRequest, response: HttpServletResponse) {
        logger.info("Saving Trigger")

        val json = request.body()
        val jsonObject = Gson().fromJson(json, TriggerJson::class.java)
        logger.info(jsonObject.toString())

        val trigger = TriggerJson.toTrigger(jsonObject)

        if (trigger == null) {
            return respondWithJson(response, null)
        }

        Project.triggers.removeIf { it.name == trigger.name }
        Project.triggers.add(trigger)

        respondWithJson(response, trigger.run(TriggerJson::from))
    }
}