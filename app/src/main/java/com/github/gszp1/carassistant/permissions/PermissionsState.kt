package com.github.gszp1.carassistant.permissions

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

@Stable
class PermissionsState internal constructor(
    private val context: Context,
    private val permissions: List<AppPermission>,
) {
    var missing: List<AppPermission> by mutableStateOf(permissions.filterUngranted())
        private set

    val allGranted: Boolean
        get() = missing.isEmpty()

    fun refresh() {
        missing = permissions.filterUngranted()
    }

    private fun List<AppPermission>.filterUngranted() = filterNot { it.isGranted(context) }
}

@Composable
fun rememberPermissionsState(
    permissions: List<AppPermission> = RequiredPermissions,
): PermissionsState {
    val context = LocalContext.current
    val state = remember(context, permissions) { PermissionsState(context, permissions) }

    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner, state) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) state.refresh()
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    return state
}
