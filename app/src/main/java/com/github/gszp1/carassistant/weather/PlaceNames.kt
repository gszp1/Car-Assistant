package com.github.gszp1.carassistant.weather

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.util.Locale
import kotlin.coroutines.resume
import kotlin.math.roundToLong

interface PlaceNames {

    suspend fun nameFor(point: GeoPoint): String?

    companion object {
        fun create(context: Context): PlaceNames = CachingPlaceNames(GeocoderPlaceNames(context))
    }
}

private const val CachePrecision = 100.0

private class CachingPlaceNames(private val delegate: PlaceNames) : PlaceNames {

    private var cachedKey: Pair<Long, Long>? = null
    private var cachedName: String? = null

    override suspend fun nameFor(point: GeoPoint): String? {
        val key = point.cacheKey()
        if (key == cachedKey) return cachedName
        return delegate.nameFor(point)?.also {
            cachedKey = key
            cachedName = it
        }
    }

    private fun GeoPoint.cacheKey(): Pair<Long, Long> =
        (latitude * CachePrecision).roundToLong() to (longitude * CachePrecision).roundToLong()
}

private class GeocoderPlaceNames(context: Context) : PlaceNames {

    private val context = context.applicationContext

    override suspend fun nameFor(point: GeoPoint): String? {
        if (!Geocoder.isPresent()) return null
        val geocoder = Geocoder(context, Locale.getDefault())
        return try {
            geocoder.addressesAt(point).firstNotNullOfOrNull { it.placeName() }
        } catch (e: CancellationException) {
            throw e
        } catch (_: Exception) {
            null
        }
    }

    private suspend fun Geocoder.addressesAt(point: GeoPoint): List<Address> =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            awaitAddresses(point)
        } else {
            withContext(Dispatchers.IO) { legacyAddresses(point) }
        }

    @Suppress("DEPRECATION")
    private fun Geocoder.legacyAddresses(point: GeoPoint): List<Address> =
        getFromLocation(point.latitude, point.longitude, MaxResults).orEmpty()

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private suspend fun Geocoder.awaitAddresses(point: GeoPoint): List<Address> =
        suspendCancellableCoroutine { continuation ->
            getFromLocation(
                point.latitude,
                point.longitude,
                MaxResults,
                object : Geocoder.GeocodeListener {
                    override fun onGeocode(addresses: List<Address>) {
                        continuation.resume(addresses)
                    }

                    override fun onError(errorMessage: String?) {
                        continuation.resume(emptyList())
                    }
                },
            )
        }

    private fun Address.placeName(): String? = listOf(
        locality,
        subAdminArea,
        adminArea,
        countryName,
    ).firstOrNull { !it.isNullOrBlank() }

    private companion object {
        const val MaxResults = 1
    }
}
