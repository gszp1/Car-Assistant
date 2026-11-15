package com.github.gszp1.carassistant.permissions

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings

object OverlayPermission : AppPermission {

    override val id = "overlay"
    override val title = "Display over other apps"
    override val rationale =
        "Integration with Google Maps navigation."

    override fun isGranted(context: Context): Boolean = Settings.canDrawOverlays(context)

    override fun request(context: Context): PermissionRequest = PermissionRequest.OpenSettings(
        Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:${context.packageName}"),
        ),
    )
}
