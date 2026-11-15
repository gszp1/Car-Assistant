package com.github.gszp1.carassistant.permissions

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat
import com.github.gszp1.carassistant.media.MediaNotificationListenerService

object MediaNotificationAccessPermission : AppPermission {

    override val id = "media_notification_access"
    override val title = "Media notification access"
    override val rationale =
        "Read and control playback – play/pause, skip and volume – " +
            "from whichever app is currently playing media."

    override fun isGranted(context: Context): Boolean =
        context.packageName in NotificationManagerCompat.getEnabledListenerPackages(context)

    override fun request(context: Context): PermissionRequest {
        val component = ComponentName(context, MediaNotificationListenerService::class.java)
        val intent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Intent(Settings.ACTION_NOTIFICATION_LISTENER_DETAIL_SETTINGS).putExtra(
                Settings.EXTRA_NOTIFICATION_LISTENER_COMPONENT_NAME,
                component.flattenToString(),
            )
        } else {
            Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
        }
        return PermissionRequest.OpenSettings(intent)
    }
}
