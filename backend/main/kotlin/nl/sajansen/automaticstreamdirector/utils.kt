package nl.sajansen.automaticstreamdirector

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.awt.Color
import java.io.File
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.logging.Logger

private val logger: Logger = Logger.getLogger("utils")

fun getTimeAsClock(seconds: Long, looseFormat: Boolean = false): String {
    var positiveValue = seconds

    var signString = ""
    if (seconds < 0) {
        positiveValue *= -1
        signString = "-"
    }

    val timerHours = positiveValue / 3600
    val timerMinutes = (positiveValue - timerHours * 3600) / 60
    val timerSeconds = positiveValue - timerHours * 3600 - timerMinutes * 60

    if (!looseFormat || timerHours != 0L) {
        return String.format("%s%d:%02d:%02d", signString, timerHours, timerMinutes, timerSeconds)
    }

    return String.format("%s%d:%02d", signString, timerMinutes, timerSeconds)
}

@Throws(UnsupportedEncodingException::class)
fun getCurrentJarDirectory(caller: Any): File {
    val url = caller::class.java.protectionDomain.codeSource.location
    val jarPath = URLDecoder.decode(url.file, "UTF-8")
    return File(jarPath).parentFile
}

fun isAddressLocalhost(address: String): Boolean {
    return address.contains("localhost") || address.contains("127.0.0.1")
}

fun brightness(color: Color): Double {
    return 0.2126 * color.red + 0.7152 * color.green + 0.0722 * color.blue
}

fun decodeURI(uri: String): String {
    return URLDecoder.decode(uri, StandardCharsets.UTF_8.name())
}

fun getReadableFileSize(file: File): String {
    return when {
        file.length() > 1024 * 1024 -> {
            val fileSize = file.length().toDouble() / (1024 * 1024)
            String.format("%.2f MB", fileSize)
        }
        file.length() > 1024 -> {
            val fileSize = file.length().toDouble() / 1024
            String.format("%.2f kB", fileSize)
        }
        else -> {
            String.format("%d bytes", file.length())
        }
    }
}

fun getFileNameWithoutExtension(file: File): String {
    return file.name.substring(0, file.name.lastIndexOf('.'))
}

fun getFileExtension(file: File): String {
    return file.name.substring(file.name.lastIndexOf('.') + 1)
}

fun Date.format(format: String): String? = SimpleDateFormat(format).format(this)
fun LocalTime.format(format: String): String = this.format(DateTimeFormatter.ofPattern(format))

internal fun jsonBuilder(prettyPrint: Boolean = true): Gson {
    val builder = GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
        .serializeNulls()

    if (prettyPrint) {
        builder.setPrettyPrinting()
    }

    return builder.create()
}