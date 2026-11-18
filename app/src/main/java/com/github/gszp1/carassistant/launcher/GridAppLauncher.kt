package com.github.gszp1.carassistant.launcher

import android.content.Context

fun Context.openGridApp(app: GridApp): Boolean = openExternalApp(
    packageName = app.packageName,
    appIntents = listOfNotNull(packageManager.getLaunchIntentForPackage(app.packageName)),
    notInstalledMessage = "${app.label} is not installed",
)
