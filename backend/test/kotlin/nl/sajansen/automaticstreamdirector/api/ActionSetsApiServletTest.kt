package nl.sajansen.automaticstreamdirector.api

import nl.sajansen.automaticstreamdirector.actions.ActionSet
import nl.sajansen.automaticstreamdirector.config.Config
import nl.sajansen.automaticstreamdirector.project.Project
import org.eclipse.jetty.http.HttpStatus
import org.junit.AfterClass
import org.junit.BeforeClass
import java.net.ServerSocket
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals


class ActionSetsApiServletTest {
    companion object {
        private var apiRootEndpoint: String = "/api/v1/actionsets"
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
        Project.availableActionSets.clear()
    }

    @Test
    fun testGetList() {
        Project.availableActionSets.add(ActionSet("ActionSet1"))
        Project.availableActionSets.add(ActionSet("ActionSet2"))
        Project.availableActionSets.add(ActionSet("ActionSet3"))

        val connection = get("$apiUrl/list")

        assertEquals(HttpStatus.OK_200, connection.responseCode)
        assertEquals(
            """{
  "data": [
    {
      "name": "ActionSet1",
      "actions": []
    },
    {
      "name": "ActionSet2",
      "actions": []
    },
    {
      "name": "ActionSet3",
      "actions": []
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
    fun testGetByName() {
        Project.availableActionSets.add(ActionSet("ActionSet1"))
        Project.availableActionSets.add(ActionSet("ActionSet2"))

        val connection = get("$apiUrl/ActionSet1")

        assertEquals(HttpStatus.OK_200, connection.responseCode)
        assertEquals(
            """{
  "data": {
    "name": "ActionSet1",
    "actions": []
  }
}""".trimIndent(), connection.body().trim()
        )
    }
}