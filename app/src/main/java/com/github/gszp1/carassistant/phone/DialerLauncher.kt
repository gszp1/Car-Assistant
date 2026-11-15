package com.github.gszp1.carassistant.phone

import android.content.Context
import android.content.Intent
import android.telecom.TelecomManager
import com.github.gszp1.carassistant.launcher.openExternalApp

private const val GoogleDialerPackage = "com.google.android.dialer"
private const val DialerNotInstalledMessage = "No phone app is installed"

/**
 * Opens the system default dialer, falling back to a generic
 * [Intent.ACTION_DIAL] so any installed phone app can handle it.
 */
fun Context.openDialer(): Boolean {
    val defaultDialerPackage = getSystemService(TelecomManager::class.java)?.defaultDialerPackage
    return openExternalApp(
        packageName = defaultDialerPackage ?: GoogleDialerPackage,
        appIntents = listOfNotNull(
            defaultDialerPackage?.let(packageManager::getLaunchIntentForPackage),
            Intent(Intent.ACTION_DIAL),
        ),
        notInstalledMessage = DialerNotInstalledMessage,
    )
}
