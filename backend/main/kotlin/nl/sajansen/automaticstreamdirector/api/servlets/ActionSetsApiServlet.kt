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
        logger.info("Getting Actions list")

        val list = Project.availableActionSets

        respondWithJson(response, list.map(ActionSetJson::from))
    }

    private fun getById(response: HttpServletResponse, params: List<String>) {
        val id = params[0].toLong()
        logger.info("Getting ActionSet with id: $id")

        val actionSet = Project.availableActionSets.find { it.id == id }

        if (actionSet == null) {
            logger.info("Could not find ActionSet with id: $id")
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

        updateProjectState(actionSet)

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

    private fun updateProjectState(actionSet: ActionSet) {
        val index = Project.availableActionSets.indexOfFirst { it.id == actionSet.id }
        if (index < 0) {
            Project.availableActionSets.add(actionSet)
            return
        }

        // Update
        val oldActionSet = Project.availableActionSets[index]
        Project.availableActionSets[index] = actionSet

        // Preserve existing action objects, as they are not changed by this Save method and need to keep their state
        oldActionSet.actions.forEach { oldAction ->
            val actionIndex = actionSet.actions.indexOfFirst { it.id == oldAction.id }
            if (actionIndex >= 0) {
                actionSet.actions[actionIndex] = oldAction
            }
        }
    }
}