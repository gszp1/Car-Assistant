package com.github.gszp1.carassistant.weather

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class OpenMeteoException(message: String, cause: Throwable? = null) : IOException(message, cause)

interface OpenMeteoClient {

    suspend fun currentWeather(point: GeoPoint): Result<CurrentWeather>

    companion object {
        fun create(): OpenMeteoClient = HttpOpenMeteoClient()
    }
}

private const val ConnectTimeoutMillis = 10_000
private const val ReadTimeoutMillis = 10_000
private const val ErrorDetailLimit = 500

private const val ForbiddenMessage =
    "Open-Meteo blocked this request (HTTP 403) - the free API limits requests per IP"

private class HttpOpenMeteoClient(
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO,
) : OpenMeteoClient {

    override suspend fun currentWeather(point: GeoPoint): Result<CurrentWeather> =
        withContext(dispatcher) {
            try {
                Result.success(parseCurrentWeather(get(OpenMeteoRequest.forecastUrl(point))))
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

    private fun get(url: String): String {
        val connection = (URL(url).openConnection() as HttpURLConnection).apply {
            requestMethod = "GET"
            connectTimeout = ConnectTimeoutMillis
            readTimeout = ReadTimeoutMillis
            setRequestProperty("Accept", "application/json")
            setRequestProperty("User-Agent", OpenMeteoRequest.UserAgent)
        }
        try {
            val status = connection.responseCode
            if (status !in HTTP_OK_RANGE) {
                throw OpenMeteoException(errorMessage(status, connection.readErrorBody()))
            }
            return connection.inputStream.bufferedReader().use { it.readText() }
        } finally {
            connection.disconnect()
        }
    }

    private fun errorMessage(status: Int, body: String?): String {
        val reason = body?.let(::reasonOrNull)?.ifBlank { null } ?: detailOrNull(body)
        return when {
            reason != null -> "Open-Meteo responded with HTTP $status: $reason"
            status == HTTP_FORBIDDEN -> ForbiddenMessage
            else -> "Open-Meteo responded with HTTP $status"
        }
    }

    private fun detailOrNull(body: String?): String? {
        val trimmed = body?.trim()?.takeIf { it.isNotEmpty() } ?: return null
        if (trimmed.startsWith("<")) return null
        return trimmed.take(ErrorDetailLimit)
    }

    private fun reasonOrNull(body: String): String? = try {
        JSONObject(body).optString("reason").ifEmpty { null }
    } catch (_: JSONException) {
        null
    }

    private fun HttpURLConnection.readErrorBody(): String? =
        errorStream?.bufferedReader()?.use { it.readText() }

    private companion object {
        val HTTP_OK_RANGE = 200..299
        const val HTTP_FORBIDDEN = 403
    }
}
