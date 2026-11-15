package com.github.gszp1.carassistant.permissions

import android.content.Context
import android.content.Intent

sealed interface PermissionRequest {
    data class OpenSettings(val intent: Intent) : PermissionRequest
    data class RequestRuntimePermissions(val permissions: List<String>) : PermissionRequest
}

interface AppPermission {
    val id: String
    val title: String
    val rationale: String
    fun isGranted(context: Context): Boolean
    fun request(context: Context): PermissionRequest
}

val RequiredPermissions: List<AppPermission> = listOf(
    LocationPermission,
    OverlayPermission,
    MediaNotificationAccessPermission,
)
