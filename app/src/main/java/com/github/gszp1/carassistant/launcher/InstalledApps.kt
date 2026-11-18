package com.github.gszp1.carassistant.launcher

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo

private val LauncherIntent: Intent =
    Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_LAUNCHER)

fun Context.installedApps(): List<GridApp> =
    packageManager.queryIntentActivities(LauncherIntent, 0)
        .map { it.toGridApp(packageManager) }
        .filterNot { it.packageName == packageName }
        .distinctBy { it.packageName }
        .sortedBy { it.label.lowercase() }

private fun ResolveInfo.toGridApp(packageManager: PackageManager): GridApp {
    val packageName = activityInfo.packageName
    return GridApp(
        packageName = packageName,
        label = loadLabel(packageManager).toString().ifBlank { packageName },
    )
}
