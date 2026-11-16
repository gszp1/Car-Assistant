package com.github.gszp1.carassistant.weather

import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.CancellationSignal
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat
import com.github.gszp1.carassistant.permissions.LocationPermission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import kotlinx.coroutines.withTimeoutOrNull
import java.io.IOException
import kotlin.coroutines.resume

class LocationUnavailableException(message: String) : IOException(message)

interface LocationSource {

    suspend fun currentPoint(): Result<GeoPoint>

    companion object {
        fun create(context: Context): LocationSource = DeviceLocationSource(context)
    }
}

private const val PermissionDeniedMessage = "Grant the location permission to see local weather"
private const val LocationDisabledMessage = "Turn on location services to see local weather"
private const val NoFixMessage = "Waiting for a location fix"

private const val FreshFixMillis = 5 * 60 * 1000L
private const val FixTimeoutMillis = 15_000L

private val LastKnownProviders = listOf(
    LocationManager.GPS_PROVIDER,
    LocationManager.NETWORK_PROVIDER,
    LocationManager.PASSIVE_PROVIDER,
)

private val FixProviders = listOf(
    LocationManager.NETWORK_PROVIDER,
    LocationManager.GPS_PROVIDER,
)

private class DeviceLocationSource(context: Context) : LocationSource {

    private val context = context.applicationContext

    override suspend fun currentPoint(): Result<GeoPoint> = withContext(Dispatchers.IO) {
        if (!LocationPermission.isGranted(context)) return@withContext unavailable(PermissionDeniedMessage)
        val manager = context.getSystemService(LocationManager::class.java)
            ?: return@withContext unavailable(LocationDisabledMessage)
        if (!LocationManagerCompat.isLocationEnabled(manager)) {
            return@withContext unavailable(LocationDisabledMessage)
        }

        val lastKnown = manager.lastKnownLocation()
        if (lastKnown != null && lastKnown.isFresh()) {
            return@withContext Result.success(lastKnown.asGeoPoint())
        }

        val fix = withTimeoutOrNull(FixTimeoutMillis) { manager.awaitFix() }
        val location = fix ?: lastKnown ?: return@withContext unavailable(NoFixMessage)
        Result.success(location.asGeoPoint())
    }

    private fun LocationManager.lastKnownLocation(): Location? = LastKnownProviders
        .filter { supportsProvider(it) }
        .mapNotNull { provider -> lastKnownLocationFrom(provider) }
        .maxByOrNull { it.time }

    private fun LocationManager.lastKnownLocationFrom(provider: String): Location? = try {
        getLastKnownLocation(provider)
    } catch (_: SecurityException) {
        null
    } catch (_: IllegalArgumentException) {
        null
    }

    private suspend fun LocationManager.awaitFix(): Location? = FixProviders
        .filter { supportsProvider(it) && isProviderEnabled(it) }
        .firstNotNullOfOrNull { provider -> currentLocation(provider) }

    private suspend fun LocationManager.currentLocation(provider: String): Location? =
        suspendCancellableCoroutine { continuation ->
            val signal = CancellationSignal()
            continuation.invokeOnCancellation { signal.cancel() }
            try {
                LocationManagerCompat.getCurrentLocation(
                    this,
                    provider,
                    signal,
                    ContextCompat.getMainExecutor(context),
                ) { location -> continuation.resume(location) }
            } catch (_: SecurityException) {
                continuation.resume(null)
            }
        }

    private fun LocationManager.supportsProvider(provider: String): Boolean =
        LocationManagerCompat.hasProvider(this, provider)

    private fun Location.isFresh(): Boolean = System.currentTimeMillis() - time <= FreshFixMillis

    private fun Location.asGeoPoint(): GeoPoint = GeoPoint(latitude, longitude)

    private fun unavailable(message: String): Result<GeoPoint> =
        Result.failure(LocationUnavailableException(message))
}
