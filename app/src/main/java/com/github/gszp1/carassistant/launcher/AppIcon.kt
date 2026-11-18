package com.github.gszp1.carassistant.launcher

import android.content.pm.PackageManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.drawable.toBitmap

private const val FallbackIconPx = 128

@Composable
fun rememberAppIcon(packageName: String): ImageBitmap? {
    val packageManager = LocalContext.current.packageManager
    return remember(packageManager, packageName) {
        val icon = try {
            packageManager.getApplicationIcon(packageName)
        } catch (_: PackageManager.NameNotFoundException) {
            return@remember null
        }
        val width = icon.intrinsicWidth.takeIf { it > 0 } ?: FallbackIconPx
        val height = icon.intrinsicHeight.takeIf { it > 0 } ?: FallbackIconPx
        icon.toBitmap(width, height).asImageBitmap()
    }
}
