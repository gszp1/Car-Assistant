package com.github.gszp1.carassistant.permissions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

object LocationPermission : AppPermission {

    private val permissions = listOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION,
    )

    override val id = "location"
    override val title = "Device location"
    override val rationale = "Device location is required for navigational functionalities."

    override fun isGranted(context: Context): Boolean = permissions.any {
        ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun request(context: Context): PermissionRequest =
        PermissionRequest.RequestRuntimePermissions(permissions)
}
