package com.github.gszp1.carassistant.weather

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.roundToInt

internal const val UnknownValue = "—"

private const val NoPrecipitationValue = "None"
private const val MetersPerKilometer = 1000.0
private const val VisibilityWholeKilometers = 10.0
private const val OneDecimalFormat = "%.1f"
private const val CoordinateFormat = "%.2f"
private const val ObservedAtPattern = "HH:mm"

internal fun formatTemperature(celsius: Double): String = "${celsius.roundToInt()}°C"

internal fun formatSpeed(kilometersPerHour: Double): String = "${kilometersPerHour.roundToInt()} km/h"

internal fun formatBearing(direction: WindDirection, degrees: Int): String =
    "${direction.label} ($degrees°)"

internal fun formatPressure(hectopascals: Double): String = "${hectopascals.roundToInt()} hPa"

internal fun formatPercent(percent: Int): String = "$percent%"

internal fun formatVisibility(meters: Double): String {
    val kilometers = meters / MetersPerKilometer
    return when {
        kilometers < 1.0 -> "${meters.roundToInt()} m"
        kilometers >= VisibilityWholeKilometers -> "${kilometers.roundToInt()} km"
        else -> "${oneDecimal(kilometers)} km"
    }
}

internal fun formatPrecipitation(millimeters: Double): String =
    if (millimeters <= 0.0) NoPrecipitationValue else "${oneDecimal(millimeters)} mm/h"

internal fun formatCoordinates(point: GeoPoint): String = String.format(
    Locale.ROOT,
    "$CoordinateFormat, $CoordinateFormat",
    point.latitude,
    point.longitude,
)

internal fun formatObservedAt(instant: Instant, zone: ZoneId = ZoneId.systemDefault()): String =
    DateTimeFormatter.ofPattern(ObservedAtPattern).withZone(zone).format(instant)

private fun oneDecimal(value: Double): String =
    String.format(Locale.ROOT, OneDecimalFormat, value).removeSuffix(".0")
