import org.eclipse.jetty.http.HttpMethod
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

fun requestConnection(
    url: String,
    body: String? = null,
    method: HttpMethod = HttpMethod.GET
): HttpURLConnection {

    val connection = URL(url).openConnection() as HttpURLConnection
    connection.requestMethod = method.toString()

    if (body != null) {
        connection.setRequestProperty("Content-Type", "application/json; utf-8")
        connection.setRequestProperty("Accept", "application/json")
        connection.doOutput = true

        OutputStreamWriter(connection.outputStream).run {
            write(body)
            flush()
        }
    }

    connection.connect()
    return connection
}