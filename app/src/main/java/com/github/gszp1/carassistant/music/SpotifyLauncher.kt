package com.github.gszp1.carassistant.music

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.github.gszp1.carassistant.launcher.openExternalApp

private const val SpotifyPackage = "com.spotify.music"
private const val SpotifyNotInstalledMessage = "Spotify is not installed"

private val SpotifyHomeUri: Uri = Uri.parse("spotify:")

fun Context.openSpotify(): Boolean = openExternalApp(
    packageName = SpotifyPackage,
    appIntents = listOfNotNull(
        packageManager.getLaunchIntentForPackage(SpotifyPackage),
        Intent(Intent.ACTION_VIEW, SpotifyHomeUri).setPackage(SpotifyPackage),
    ),
    notInstalledMessage = SpotifyNotInstalledMessage,
)
