package nl.sajansen.automaticstreamdirector.api.servlets


import com.google.gson.Gson
import nl.sajansen.automaticstreamdirector.actions.Action
import nl.sajansen.automaticstreamdirector.api.body
import nl.sajansen.automaticstreamdirector.api.getPathVariables
import nl.sajansen.automaticstreamdirector.api.json.ActionJson
import nl.sajansen.automaticstreamdirector.api.json.FormDataJson
import nl.sajansen.automaticstreamdirector.api.json.StaticActionJson
import nl.sajansen.automaticstreamdirector.api.respondWithJson
import nl.sajansen.automaticstreamdirector.api.respondWithNotFound
import nl.sajansen.automaticstreamdirector.modules.Modules
import nl.sajansen.automaticstreamdirector.project.Project
import java.util.logging.Logger
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class ActionsApiServlet : HttpServlet() {
    private val logger = Logger.getLogger(ConfigApiServlet::class.java.name)

    operator fun Regex.contains(text: CharSequence?): Boolean = this.matches(text ?: "")
    private val getIdMatcher = """^/(\d+)$""".toRegex()
    private val editIdMatcher = """^/edit/(\d+)$""".toRegex()

    override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        logger.info("Processing ${request.method} request from : ${request.requestURI}")

        when (request.pathInfo) {
            "/list" -> getStaticActions(response)
            in Regex(getIdMatcher.pattern) -> getById(
                response,
                request.pathInfo.getPathVariables(getIdMatcher)
            )
            in Regex(editIdMatcher.pattern) -> getStaticActionForId(
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

    private fun getStaticActions(response: HttpServletResponse) {
        logger.info("Getting Static Actions for modules")

        val actions = Modules.actions()

        respondWithJson(response, actions.map(StaticActionJson::from))
    }

    private fun getStaticActionForId(response: HttpServletResponse, params: List<String>) {
        val id = params[0].toLongOrNull()
        logger.info("Get StaticAction for action with id: $id")

        if (id == null || id < 0) {
            logger.info("Invalid action id")
            return respondWithNotFound(response)
        }

        val action = Action.get(id) ?: return respondWithNotFound(response)

        val staticAction = Modules.actions().find { it::class.java.enclosingClass == action::class.java }
        if (staticAction == null) {
            logger.info("StaticAction not found for action class: ${action::class.java}")
            return respondWithNotFound(response)
        }

        respondWithJson(response, staticAction.run(StaticActionJson::from))
    }

    private fun getById(response: HttpServletResponse, params: List<String>) {
        val id = params[0].toLong()
        logger.info("Getting Action with id: $id")

        val action = Project.availableActionSets
            .flatMap { it.actions }
            .find { it.id == id }

        if (action == null) {
            logger.info("Could not find Action with id: $id")
            return respondWithJson(response, null)
        }

        respondWithJson(response, action.run(ActionJson::from))
    }

    private fun postSave(request: HttpServletRequest, response: HttpServletResponse) {
        logger.info("Saving Action")

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
        if (result !is Action) {
            return respondWithJson(response, "Something went wrong")
        }

        updateProjectState(result)

        respondWithJson(response, result.run(ActionJson::from))
    }

    fun updateProjectState(action: Action) {
        if (action.id == null) {
            return
        }

        val actionSet = Project.availableActionSets
            .find { actionSet -> actionSet.actions.any { it.id == action.id } } ?: return

        val index = actionSet.actions.indexOfFirst { it.id == action.id }
        actionSet.actions[index] = action
    }
}