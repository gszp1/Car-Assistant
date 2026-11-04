package com.github.gszp1.carassistant.navigation

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

private const val GoogleMapsPackage = "com.google.android.apps.maps"
private const val MapsNotInstalledMessage = "Google Maps is not installed"

private val CurrentLocationUri: Uri = Uri.parse("geo:0,0")
private val PlayStoreUri: Uri = Uri.parse("market://details?id=$GoogleMapsPackage")
private val PlayStoreWebUri: Uri =
    Uri.parse("https://play.google.com/store/apps/details?id=$GoogleMapsPackage")

fun Context.openGoogleMaps(): Boolean {
    val mapsIntents = listOfNotNull(
        packageManager.getLaunchIntentForPackage(GoogleMapsPackage),
        Intent(Intent.ACTION_VIEW, CurrentLocationUri).setPackage(GoogleMapsPackage),
    )
    if (mapsIntents.any(::startActivitySafely)) return true

    Toast.makeText(this, MapsNotInstalledMessage, Toast.LENGTH_SHORT).show()
    return listOf(PlayStoreUri, PlayStoreWebUri)
        .map { Intent(Intent.ACTION_VIEW, it) }
        .any(::startActivitySafely)
}

private fun Context.startActivitySafely(intent: Intent): Boolean = try {
    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    true
} catch (_: ActivityNotFoundException) {
    false
} catch (_: SecurityException) {
    false
}
