package nl.sajansen.automaticstreamdirector.api

import nl.sajansen.automaticstreamdirector.config.Config
import nl.sajansen.automaticstreamdirector.project.Project
import nl.sajansen.automaticstreamdirector.triggers.Trigger
import org.eclipse.jetty.http.HttpStatus
import org.junit.AfterClass
import org.junit.BeforeClass
import java.net.ServerSocket
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals


class TriggersApiServletTest {
    companion object {
        private var apiRootEndpoint: String = "/api/v1/triggers"
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
        Project.triggers.clear()
    }

    @Test
    fun testGetList() {
        Project.triggers.add(Trigger("Trigger1"))
        Project.triggers.add(Trigger("Trigger2"))
        Project.triggers.add(Trigger("Trigger3"))

        val connection = get("$apiUrl/list")

        assertEquals(HttpStatus.OK_200, connection.responseCode)
        assertEquals(
            """{
  "data": [
    {
      "name": "Trigger1",
      "importance": 0,
      "conditions": [],
      "actionSets": []
    },
    {
      "name": "Trigger2",
      "importance": 0,
      "conditions": [],
      "actionSets": []
    },
    {
      "name": "Trigger3",
      "importance": 0,
      "conditions": [],
      "actionSets": []
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
        Project.triggers.add(Trigger("Trigger1"))
        Project.triggers.add(Trigger("Trigger2"))

        val connection = get("$apiUrl/Trigger1")

        assertEquals(HttpStatus.OK_200, connection.responseCode)
        assertEquals(
            """{
  "data": {
    "name": "Trigger1",
    "importance": 0,
    "conditions": [],
    "actionSets": []
  }
}""".trimIndent(), connection.body().trim()
        )
    }
}