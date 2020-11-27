package nl.sajansen.automaticstreamdirector.api

import nl.sajansen.automaticstreamdirector.actions.ActionSet
import nl.sajansen.automaticstreamdirector.actions.StaticAction
import nl.sajansen.automaticstreamdirector.api.servlets.ActionsApiServlet
import nl.sajansen.automaticstreamdirector.config.Config
import nl.sajansen.automaticstreamdirector.mocks.ActionMock
import nl.sajansen.automaticstreamdirector.mocks.ModuleMock
import nl.sajansen.automaticstreamdirector.modules.Modules
import nl.sajansen.automaticstreamdirector.project.Project
import org.eclipse.jetty.http.HttpStatus
import org.junit.AfterClass
import org.junit.BeforeClass
import java.net.ServerSocket
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals


@Suppress("USELESS_CAST")
class ActionsApiServletTest {
    companion object {
        private var apiRootEndpoint: String = "/api/v1/actions"
        private var apiUrl: String = "" + apiRootEndpoint

        @BeforeClass
        @JvmStatic
        fun setup() {
            // Get some random free port
            Config.httpApiServerPort = ServerSocket(0).use { it.localPort }

            ApiServer.start()
            apiUrl = ApiServer.url() + apiRootEndpoint
        }

        @AfterClass
        @JvmStatic
        fun tearDown() {
            ApiServer.stop()
        }
    }

    @BeforeTest
    fun before() {
        Modules.modules.clear()
        Project.availableActionSets.clear()
    }

    @Test
    fun testGetList() {
        Modules.modules.add(ModuleMock("ModuleMock1"))

        val connection = get("$apiUrl/list")

        assertEquals(HttpStatus.OK_200, connection.responseCode)
        assertEquals(
            """{
  "data": [
    {
      "className": "${(ActionMock as StaticAction)::class.java.name}",
      "name": "ActionMock",
      "previewText": "ActionMock ...",
      "formComponents": [
        {
          "name": "age",
          "labelText": "Age",
          "type": "Number",
          "required": true,
          "defaultValue": null,
          "selectValues": []
        }
      ]
    }
  ]
}""".trimIndent(), connection.body().trim()
        )
    }

    @Test
    fun testGetEmptyList() {
        val connection = get("$apiUrl/list")

        assertEquals(HttpStatus.OK_200, connection.responseCode)
        assertEquals(
            """{
  "data": []
}""", connection.body().trim()
        )
    }

    @Test
    fun testGetById() {
        Project.availableActionSets.add(ActionSet("ActionSet1", id = 1).also {
            it.actions.add(ActionMock(id = 1))
        })
        Project.availableActionSets.add(ActionSet("ActionSet1", id = 1).also {
            it.actions.add(ActionMock(id = 2))
        })

        val connection = get("$apiUrl/1")

        assertEquals(HttpStatus.OK_200, connection.responseCode)
        assertEquals(
            """{
  "data": {
    "id": 1,
    "name": "ActionMock",
    "data": {
      "age": null
    }
  }
}""".trimIndent(), connection.body().trim()
        )
    }

    @Test
    fun testUpdateProjectStateWithExistingAction() {
        Project.availableActionSets.add(ActionSet("ActionSet1", id = 1).also {
            it.actions.add(ActionMock(id = 1, age = 1))
            it.actions.add(ActionMock(id = 2))
        })
        Project.availableActionSets.add(ActionSet("ActionSet1", id = 1).also {
            it.actions.add(ActionMock(id = 3))
        })

        val newAction = ActionMock(id = 1, age = 2)
        assertNotEquals(newAction, Project.availableActionSets[0].actions[0])

        ActionsApiServlet().updateProjectState(newAction)

        assertEquals(newAction, Project.availableActionSets[0].actions[0])
        assertEquals(2, newAction.age)
    }

    @Test
    fun testUpdateProjectStateWithNewAction() {
        Project.availableActionSets.add(ActionSet("ActionSet1", id = 1).also {
            it.actions.add(ActionMock(id = 1, age = 1))
            it.actions.add(ActionMock(id = 2))
        })
        Project.availableActionSets.add(ActionSet("ActionSet1", id = 1).also {
            it.actions.add(ActionMock(id = 3))
        })

        val newAction = ActionMock(id = 4, age = 2)
        assertNotEquals(newAction, Project.availableActionSets[0].actions[0])

        ActionsApiServlet().updateProjectState(newAction)

        assertNotEquals(newAction, Project.availableActionSets[0].actions[0])
    }
}