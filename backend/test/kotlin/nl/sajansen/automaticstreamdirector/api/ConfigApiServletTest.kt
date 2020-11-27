package nl.sajansen.automaticstreamdirector.api

import com.google.gson.Gson
import com.google.gson.internal.LinkedTreeMap
import nl.sajansen.automaticstreamdirector.api.json.ConfigJson
import nl.sajansen.automaticstreamdirector.config.Config
import nl.sajansen.automaticstreamdirector.jsonBuilder
import org.eclipse.jetty.http.HttpStatus
import org.junit.AfterClass
import org.junit.BeforeClass
import java.net.ServerSocket
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class ConfigApiServletTest {
    companion object {
        private var apiRootEndpoint: String = "/api/v1/config"
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
        Config.updateInterval = 1.0
    }

    @Test
    fun testGetConfigKeyValue() {
        val connection = get("$apiUrl/key/updateInterval")

        assertEquals(HttpStatus.OK_200, connection.responseCode)
        assertEquals(
            """{
  "data": {
    "key": "updateInterval",
    "value": 1.0,
    "formComponent": null
  }
}""".trimIndent(), connection.body().trim()
        )
    }

    @Test
    fun testGetInvalidConfigKeyValue() {
        val connection = get("$apiUrl/key/xx")

        assertEquals(HttpStatus.OK_200, connection.responseCode)
        assertEquals(
            """{
  "data": {
    "key": "xx",
    "value": null,
    "formComponent": null
  }
}""".trimIndent(), connection.body().trim()
        )
    }

    @Test
    fun testGetList() {
        val connection = get("$apiUrl/list")

        assertEquals(HttpStatus.OK_200, connection.responseCode)
        val body = connection.body()
        println(body)
        val obj = Gson().fromJson(body, Map::class.java)

        assertEquals("data", obj.keys.first())
        assertEquals("frontend", (obj["data"] as LinkedTreeMap<String, Any>).keys.toList()[0])
        assertEquals("backend", (obj["data"] as LinkedTreeMap<String, Any>).keys.toList()[1])

        val config = Gson().fromJson(jsonBuilder().toJson(obj.entries.first().value), ConfigJson::class.java)
        assertTrue(config.backend.any {
            it.key == "updateInterval" && it.value == 1.0
        })
    }

    @Test
    fun testGetInvalidPostEndpoint() {
        val connection = post("$apiUrl/savex")

        assertEquals(HttpStatus.NOT_FOUND_404, connection.responseCode)
        assertEquals("Not Found", connection.errorBody()?.trim())
    }
}