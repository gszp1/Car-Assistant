package com.github.gszp1.carassistant.launcher

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast

fun Context.openExternalApp(
    packageName: String,
    appIntents: List<Intent>,
    notInstalledMessage: String,
): Boolean {
    if (appIntents.any(::startActivitySafely)) return true

    Toast.makeText(this, notInstalledMessage, Toast.LENGTH_SHORT).show()
    return listOf(
        "market://details?id=$packageName",
        "https://play.google.com/store/apps/details?id=$packageName",
    )
        .map { Intent(Intent.ACTION_VIEW, Uri.parse(it)) }
        .any(::startActivitySafely)
}

/**
 * Starts [intent] in a new task, returning false instead of throwing
 * when no activity can handle it or the caller is not allowed to start it.
 */
fun Context.startActivitySafely(intent: Intent): Boolean = try {
    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    true
} catch (_: ActivityNotFoundException) {
    false
} catch (_: SecurityException) {
    false
}
