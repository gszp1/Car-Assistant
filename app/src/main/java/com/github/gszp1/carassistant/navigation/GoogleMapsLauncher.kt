package com.github.gszp1.carassistant.navigation

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.github.gszp1.carassistant.launcher.openExternalApp

private const val GoogleMapsPackage = "com.google.android.apps.maps"
private const val MapsNotInstalledMessage = "Google Maps is not installed"

private val CurrentLocationUri: Uri = Uri.parse("geo:0,0")

fun Context.openGoogleMaps(): Boolean = openExternalApp(
    packageName = GoogleMapsPackage,
    appIntents = listOfNotNull(
        packageManager.getLaunchIntentForPackage(GoogleMapsPackage),
        Intent(Intent.ACTION_VIEW, CurrentLocationUri).setPackage(GoogleMapsPackage),
    ),
    notInstalledMessage = MapsNotInstalledMessage,
)
