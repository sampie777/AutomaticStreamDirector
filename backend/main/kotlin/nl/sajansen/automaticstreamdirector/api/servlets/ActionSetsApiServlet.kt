package nl.sajansen.automaticstreamdirector.api.servlets


import com.google.gson.Gson
import nl.sajansen.automaticstreamdirector.actions.ActionSet
import nl.sajansen.automaticstreamdirector.api.body
import nl.sajansen.automaticstreamdirector.api.getPathVariables
import nl.sajansen.automaticstreamdirector.api.json.ActionSetJson
import nl.sajansen.automaticstreamdirector.api.respondWithJson
import nl.sajansen.automaticstreamdirector.api.respondWithNotFound
import nl.sajansen.automaticstreamdirector.project.Project
import java.util.logging.Logger
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class ActionSetsApiServlet : HttpServlet() {
    private val logger = Logger.getLogger(ConfigApiServlet::class.java.name)

    operator fun Regex.contains(text: CharSequence?): Boolean = this.matches(text ?: "")
    private val actionNameMatcher = """^/(\w+)$""".toRegex()
    private val idMatcher = """^/delete/(\d+)$""".toRegex()

    override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        logger.info("Processing ${request.method} request from : ${request.requestURI}")

        when (request.pathInfo) {
            "/list" -> getList(response)
            in Regex(actionNameMatcher.pattern) -> getByName(
                response,
                request.pathInfo.getPathVariables(actionNameMatcher)
            )
            else -> respondWithNotFound(response)
        }
    }

    override fun doPost(request: HttpServletRequest, response: HttpServletResponse) {
        logger.info("Processing ${request.method} request from : ${request.requestURI}")

        when (request.pathInfo) {
            "/save" -> postSave(request, response)
            in Regex(idMatcher.pattern) -> deleteById(
                response,
                request.pathInfo.getPathVariables(idMatcher)
            )
            else -> respondWithNotFound(response)
        }
    }

    override fun doDelete(request: HttpServletRequest, response: HttpServletResponse) {
        logger.info("Processing ${request.method} request from : ${request.requestURI}")

        when (request.pathInfo) {
            in Regex(idMatcher.pattern) -> deleteById(
                response,
                request.pathInfo.getPathVariables(idMatcher)
            )
            else -> respondWithNotFound(response)
        }
    }

    private fun getList(response: HttpServletResponse) {
        logger.info("Getting Actions list")

        val list = Project.availableActionSets

        respondWithJson(response, list.map(ActionSetJson::from))
    }

    private fun getByName(response: HttpServletResponse, params: List<String>) {
        val name = params[0]
        logger.info("Getting ActionSet with name: $name")

        val actionSet = Project.availableActionSets.find { it.name == name }

        if (actionSet == null) {
            logger.info("Could not find ActionSet with name: $name")
            return respondWithJson(response, null)
        }

        respondWithJson(response, actionSet.run(ActionSetJson::from))
    }

    private fun postSave(request: HttpServletRequest, response: HttpServletResponse) {
        logger.info("Saving ActionSet")

        val json = request.body()
        val actionSetJson = Gson().fromJson(json, ActionSetJson::class.java)
        logger.info(actionSetJson.toString())

        val validationResult = arrayListOf<String>()

        if (actionSetJson.name.isEmpty()) {
            validationResult.add("Name must not be empty")
        } else if (Project.availableActionSets.any { it.id != actionSetJson.id && it.name == actionSetJson.name }) {
            validationResult.add("Action set name already exists")
        }

        if (actionSetJson.actions.isEmpty()) {
            validationResult.add("The action set must contain at least one action")
        }

        if (validationResult.isNotEmpty()) {
            logger.info("Validation result: $validationResult")
            return respondWithJson(response, validationResult)
        }

        val actionSet = try {
            ActionSetJson.toActionSet(actionSetJson)
        } catch (e: Exception) {
            logger.severe("Could not create actionSet from json: $actionSetJson")
            e.printStackTrace()
            respondWithJson(response, "Something went wrong: ${e.localizedMessage}")
            null
        } ?: return

        ActionSet.saveOrUpdate(actionSet)

        Project.availableActionSets.removeIf { it.id == actionSet.id }
        Project.availableActionSets.add(actionSet)

        respondWithJson(response, actionSet.run(ActionSetJson::from))
    }

    private fun deleteById(response: HttpServletResponse, params: List<String>) {
        val id = params[0].toLong()
        logger.info("Deleting ActionSet with id: $id")

        val actionSet = Project.availableActionSets.find { it.id == id }

        if (actionSet == null) {
            logger.info("Could not find ActionSet with id: $id")
            return respondWithJson(response, null)
        }

        Project.availableActionSets.remove(actionSet)
        respondWithJson(response, ActionSet.delete(id))
    }
}