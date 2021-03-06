package nl.sajansen.automaticstreamdirector.api.servlets


import nl.sajansen.automaticstreamdirector.api.getPathVariables
import nl.sajansen.automaticstreamdirector.api.json.ModuleJson
import nl.sajansen.automaticstreamdirector.api.json.StaticConditionJson
import nl.sajansen.automaticstreamdirector.api.respondWithJson
import nl.sajansen.automaticstreamdirector.api.respondWithNotFound
import nl.sajansen.automaticstreamdirector.modules.Modules
import java.util.logging.Logger
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class ModulesApiServlet : HttpServlet() {
    private val logger = Logger.getLogger(ConfigApiServlet::class.java.name)

    operator fun Regex.contains(text: CharSequence?): Boolean = this.matches(text ?: "")
    private val triggerNameMatcher = """^/(\w+)$""".toRegex()

    override fun doGet(request: HttpServletRequest, response: HttpServletResponse) {
        logger.info("Processing ${request.method} request from : ${request.requestURI}")

        when (request.pathInfo) {
            "/list" -> getList(response)
            "/conditions" -> getStaticConditions(response)
            in Regex(triggerNameMatcher.pattern) -> getByName(
                response,
                request.pathInfo.getPathVariables(triggerNameMatcher)
            )
            else -> respondWithNotFound(response)
        }
    }

    override fun doPost(request: HttpServletRequest, response: HttpServletResponse) {
        logger.info("Processing ${request.method} request from : ${request.requestURI}")

        when (request.pathInfo) {
            else -> respondWithNotFound(response)
        }
    }

    private fun getList(response: HttpServletResponse) {
        logger.info("Getting Modules list")

        val list = Modules.modules

        respondWithJson(response, list.map(ModuleJson::from))
    }

    private fun getStaticConditions(response: HttpServletResponse) {
        logger.info("Getting Static Conditions for modules")

        val conditions = Modules.conditions()

        respondWithJson(response, conditions.map(StaticConditionJson::from))
    }

    private fun getByName(response: HttpServletResponse, params: List<String>) {
        val name = params[0]
        logger.info("Getting Modules with name: $name")

        val module = Modules.modules.find { it.name == name }

        if (module == null) {
            logger.info("Could not find Module with name: $name")
            return respondWithJson(response, null)
        }

        respondWithJson(response, module.run(ModuleJson::from))
    }
}