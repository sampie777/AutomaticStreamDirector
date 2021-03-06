package nl.sajansen.automaticstreamdirector.api.servlets


import com.google.gson.Gson
import nl.sajansen.automaticstreamdirector.api.body
import nl.sajansen.automaticstreamdirector.api.getPathVariables
import nl.sajansen.automaticstreamdirector.api.json.TriggerJson
import nl.sajansen.automaticstreamdirector.api.respondWithJson
import nl.sajansen.automaticstreamdirector.api.respondWithNotFound
import nl.sajansen.automaticstreamdirector.project.Project
import nl.sajansen.automaticstreamdirector.triggers.Trigger
import java.util.logging.Logger
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class TriggersApiServlet : HttpServlet() {
    private val logger = Logger.getLogger(ConfigApiServlet::class.java.name)

    operator fun Regex.contains(text: CharSequence?): Boolean = this.matches(text ?: "")
    private val getIdMatcher = """^/(\d+)$""".toRegex()
    private val deleteIdMatcher = """^/delete/(\d+)$""".toRegex()

    override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        logger.info("Processing ${request.method} request from : ${request.requestURI}")

        when (request.pathInfo) {
            "/list" -> getList(response)
            in Regex(getIdMatcher.pattern) -> getById(
                response,
                request.pathInfo.getPathVariables(getIdMatcher)
            )
            else -> respondWithNotFound(response)
        }
    }

    override fun doPost(request: HttpServletRequest, response: HttpServletResponse) {
        logger.info("Processing ${request.method} request from : ${request.requestURI}")

        when (request.pathInfo) {
            "/save" -> postSave(request, response)
            "/reloadAll" -> postReloadAll(request, response)
            in Regex(deleteIdMatcher.pattern) -> deleteById(
                response,
                request.pathInfo.getPathVariables(deleteIdMatcher)
            )
            else -> respondWithNotFound(response)
        }
    }

    override fun doDelete(request: HttpServletRequest, response: HttpServletResponse) {
        logger.info("Processing ${request.method} request from : ${request.requestURI}")

        when (request.pathInfo) {
            in Regex(deleteIdMatcher.pattern) -> deleteById(
                response,
                request.pathInfo.getPathVariables(deleteIdMatcher)
            )
            else -> respondWithNotFound(response)
        }
    }

    private fun getList(response: HttpServletResponse) {
        logger.info("Getting Triggers list")

        val list = Project.triggers

        respondWithJson(response, list.map(TriggerJson::from))
    }

    private fun getById(response: HttpServletResponse, params: List<String>) {
        val id = params[0].toLong()
        logger.info("Getting Trigger with id: $id")

        val trigger = Project.triggers.find { it.id == id }

        if (trigger == null) {
            logger.info("Could not find Trigger with id: $id")
            return respondWithJson(response, null)
        }

        respondWithJson(response, trigger.run(TriggerJson::from))
    }

    private fun postSave(request: HttpServletRequest, response: HttpServletResponse) {
        logger.info("Saving Trigger")

        val json = request.body()
        val triggerJson = Gson().fromJson(json, TriggerJson::class.java)
        logger.info(triggerJson.toString())

        val validationResult = arrayListOf<String>()

        if (triggerJson.name.isEmpty()) {
            validationResult.add("Name must not be empty")
        } else if (Project.triggers.any { it.id != triggerJson.id && it.name == triggerJson.name }) {
            validationResult.add("Trigger name already exists")
        }

        if (triggerJson.conditions.isEmpty()) {
            validationResult.add("The trigger must contain at least one condition")
        }

        if (validationResult.isNotEmpty()) {
            logger.info("Validation result: $validationResult")
            return respondWithJson(response, validationResult)
        }

        val trigger = try {
            TriggerJson.toTrigger(triggerJson)
        } catch (e: Exception) {
            logger.severe("Could not create trigger from json: $triggerJson")
            e.printStackTrace()
            respondWithJson(response, "Something went wrong: ${e.localizedMessage}")
            null
        } ?: return

        Trigger.saveOrUpdate(trigger)

        updateProjectState(trigger)

        respondWithJson(response, trigger.run(TriggerJson::from))
    }

    private fun postReloadAll(request: HttpServletRequest, response: HttpServletResponse) {
        logger.info("Reloading all Triggers")

        Project.loadTriggers()

        respondWithJson(response, "ok")
    }

    private fun deleteById(response: HttpServletResponse, params: List<String>) {
        val id = params[0].toLong()
        logger.info("Deleting Trigger with id: $id")

        val trigger = Project.triggers.find { it.id == id }

        if (trigger == null) {
            logger.info("Could not find Trigger with id: $id")
            return respondWithJson(response, null)
        }

        Project.triggers.remove(trigger)
        respondWithJson(response, Trigger.delete(id))
    }

    private fun updateProjectState(trigger: Trigger) {
        trigger.syncActionSetsWithProjectState()

        val index = Project.triggers.indexOfFirst { it.id == trigger.id }
        if (index < 0) {
            Project.triggers.add(trigger)
            return
        }

        // Update
        val oldTrigger = Project.triggers[index]
        Project.triggers[index] = trigger

        // Preserve existing condition objects, as they are not changed by this Save method and need to keep their state
        oldTrigger.conditions.forEach { oldCondition ->
            val conditionIndex = trigger.conditions.indexOfFirst { it.id == oldCondition.id }
            if (conditionIndex >= 0) {
                trigger.conditions[conditionIndex] = oldCondition
            }
        }
    }
}