package nl.sajansen.automaticstreamdirector.api

import nl.sajansen.automaticstreamdirector.config.Config
import nl.sajansen.automaticstreamdirector.mocks.ModuleMock
import nl.sajansen.automaticstreamdirector.modules.Modules
import org.eclipse.jetty.http.HttpStatus
import org.junit.AfterClass
import org.junit.BeforeClass
import java.net.ServerSocket
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals


class ModulesApiServletTest {
    companion object {
        private var apiRootEndpoint: String = "/api/v1/modules"
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
    }

    @Test
    fun testGetList() {
        Modules.modules.add(ModuleMock("ModuleMock1"))
        Modules.modules.add(ModuleMock("ModuleMock2"))

        val connection = get("$apiUrl/list")

        assertEquals(HttpStatus.OK_200, connection.responseCode)
        assertEquals(
            """{
  "data": [
    {
      "name": "ModuleMock1"
    },
    {
      "name": "ModuleMock2"
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
        Modules.modules.add(ModuleMock("ModuleMock1"))
        Modules.modules.add(ModuleMock("ModuleMock2"))

        val connection = get("$apiUrl/ModuleMock1")

        assertEquals(HttpStatus.OK_200, connection.responseCode)
        assertEquals(
            """{
  "data": {
    "name": "ModuleMock1"
  }
}""".trimIndent(), connection.body().trim()
        )
    }
}