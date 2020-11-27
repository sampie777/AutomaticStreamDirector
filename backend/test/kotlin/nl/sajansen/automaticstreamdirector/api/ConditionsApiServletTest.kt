package nl.sajansen.automaticstreamdirector.api

import nl.sajansen.automaticstreamdirector.api.servlets.ConditionsApiServlet
import nl.sajansen.automaticstreamdirector.config.Config
import nl.sajansen.automaticstreamdirector.mocks.ConditionMock
import nl.sajansen.automaticstreamdirector.mocks.ModuleMock
import nl.sajansen.automaticstreamdirector.modules.Modules
import nl.sajansen.automaticstreamdirector.project.Project
import nl.sajansen.automaticstreamdirector.triggers.StaticCondition
import nl.sajansen.automaticstreamdirector.triggers.Trigger
import org.eclipse.jetty.http.HttpStatus
import org.junit.AfterClass
import org.junit.BeforeClass
import java.net.ServerSocket
import kotlin.test.*


@Suppress("USELESS_CAST")
class ConditionsApiServletTest {
    companion object {
        private var apiRootEndpoint: String = "/api/v1/conditions"
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
        Project.triggers.clear()
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
      "className": "${(ConditionMock as StaticCondition)::class.java.name}",
      "name": "ConditionMock",
      "previewText": "If ...",
      "formComponents": [
        {
          "name": "checkReturnValue",
          "labelText": "CheckReturnValue",
          "type": "Checkbox",
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
        Project.triggers.add(Trigger("Trigger1", id = 1).also {
            it.conditions.add(ConditionMock(id = 1))
        })
        Project.triggers.add(Trigger("Trigger1", id = 1).also {
            it.conditions.add(ConditionMock(id = 2))
        })

        val connection = get("$apiUrl/1")

        assertEquals(HttpStatus.OK_200, connection.responseCode)
        assertEquals(
            """{
  "data": {
    "id": 1,
    "name": "ConditionMock",
    "data": {
      "checkReturnValue": false
    }
  }
}""".trimIndent(), connection.body().trim()
        )
    }

    @Test
    fun testUpdateProjectStateWithExistingCondition() {
        Project.triggers.add(Trigger("Trigger1", id = 1).also {
            it.conditions.add(ConditionMock(id = 1, checkReturnValue = false))
            it.conditions.add(ConditionMock(id = 2))
        })
        Project.triggers.add(Trigger("Trigger1", id = 1).also {
            it.conditions.add(ConditionMock(id = 3))
        })

        val newCondition = ConditionMock(id = 1, checkReturnValue = true)
        assertNotEquals(newCondition, Project.triggers[0].conditions[0])

        ConditionsApiServlet().updateProjectState(newCondition)

        assertEquals(newCondition, Project.triggers[0].conditions[0])
        assertTrue(newCondition.checkReturnValue)
    }

    @Test
    fun testUpdateProjectStateWithNewCondition() {
        Project.triggers.add(Trigger("Trigger1", id = 1).also {
            it.conditions.add(ConditionMock(id = 1, checkReturnValue = false))
            it.conditions.add(ConditionMock(id = 2))
        })
        Project.triggers.add(Trigger("Trigger1", id = 1).also {
            it.conditions.add(ConditionMock(id = 3))
        })

        val newCondition = ConditionMock(id = 4, checkReturnValue = true)
        assertNotEquals(newCondition, Project.triggers[0].conditions[0])

        ConditionsApiServlet().updateProjectState(newCondition)

        assertNotEquals(newCondition, Project.triggers[0].conditions[0])
    }
}