package com.github.gszp1.carassistant.media

import android.content.ActivityNotFoundException
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.media.MediaMetadata
import android.media.session.MediaController
import android.media.session.PlaybackState
import android.os.Build
import android.provider.Settings
import androidx.core.app.NotificationManagerCompat
import kotlin.math.roundToInt
import android.media.session.MediaSessionManager as PlatformSessionManager

private const val VolumeStream = AudioManager.STREAM_MUSIC
private const val NoVolumeFlags = 0
private const val MinVolumeLevel = 0.0f
private const val MaxVolumeLevel = 1.0f

class MediaSessionManager(private val context: Context) {

    private val audioManager: AudioManager? = context.getSystemService(AudioManager::class.java)

    private val sessionManager: PlatformSessionManager? =
        context.getSystemService(PlatformSessionManager::class.java)

    private val listenerComponent =
        ComponentName(context, MediaNotificationListenerService::class.java)

    private var volumeBeforeMute: Int? = null

    val hasSessionAccess: Boolean
        get() = context.packageName in
            NotificationManagerCompat.getEnabledListenerPackages(context)

    fun requestSessionAccess(): Boolean =
        listOfNotNull(listenerDetailSettingsIntent(), Intent(ListenerSettingsAction))
            .any(::startActivitySafely)

    fun status(): MediaStatus {
        val controller = activeController()
        val metadata = controller?.metadata
        return MediaStatus(
            hasAccess = hasSessionAccess,
            hasSession = controller != null,
            isPlaying = controller?.hasActivePlayback() == true,
            title = metadata?.getString(MediaMetadata.METADATA_KEY_TITLE),
            artist = metadata?.getString(MediaMetadata.METADATA_KEY_ARTIST),
            volumeLevel = volumeLevelOf(controller),
            isMuted = isMutedOn(controller),
        )
    }

    fun pause(): Boolean = withTransportControls { it.pause() }

    fun play(): Boolean = withTransportControls { it.play() }

    fun playPause(): Boolean {
        val controller = activeController() ?: return false
        return runSafely {
            with(controller.transportControls) {
                if (controller.hasActivePlayback()) pause() else play()
            }
            true
        }
    }

    fun skipToNext(): Boolean = withTransportControls { it.skipToNext() }

    fun skipToPrevious(): Boolean = withTransportControls { it.skipToPrevious() }

    fun volumeLevel(): Float = volumeLevelOf(activeController())

    fun setVolumeLevel(level: Float): Boolean = runSafely {
        val target = level.coerceIn(MinVolumeLevel, MaxVolumeLevel)
        val remote = remoteVolumeController()
        if (remote != null) {
            val info = remote.playbackInfo
            remote.setVolumeTo((target * info.maxVolume).roundToInt(), NoVolumeFlags)
            return@runSafely true
        }
        val audio = audioManager ?: return@runSafely false
        val max = audio.getStreamMaxVolume(VolumeStream)
        audio.setStreamVolume(VolumeStream, (target * max).roundToInt(), NoVolumeFlags)
        true
    }

    fun raiseVolume(): Boolean = adjustVolume(AudioManager.ADJUST_RAISE)

    fun lowerVolume(): Boolean = adjustVolume(AudioManager.ADJUST_LOWER)

    fun isMuted(): Boolean = isMutedOn(activeController())

    fun toggleMute(): Boolean = runSafely {
        val remote = remoteVolumeController()
        if (remote != null) {
            val info = remote.playbackInfo
            val muted = info.currentVolume <= 0
            val target = if (muted) volumeBeforeMute ?: (info.maxVolume / 2) else 0
            volumeBeforeMute = info.currentVolume.takeUnless { muted }
            remote.setVolumeTo(target, NoVolumeFlags)
            return@runSafely true
        }
        val audio = audioManager ?: return@runSafely false
        audio.adjustStreamVolume(VolumeStream, AudioManager.ADJUST_TOGGLE_MUTE, NoVolumeFlags)
        true
    }

    private fun adjustVolume(direction: Int): Boolean = runSafely {
        val remote = remoteVolumeController()
        if (remote != null) {
            remote.adjustVolume(direction, NoVolumeFlags)
            return@runSafely true
        }
        val audio = audioManager ?: return@runSafely false
        audio.adjustStreamVolume(VolumeStream, direction, NoVolumeFlags)
        true
    }

    private fun volumeLevelOf(controller: MediaController?): Float {
        controller?.remoteVolumeInfo()?.let { info ->
            if (info.maxVolume <= 0) return MinVolumeLevel
            return info.currentVolume.toFloat() / info.maxVolume
        }
        val audio = audioManager ?: return MinVolumeLevel
        val max = audio.getStreamMaxVolume(VolumeStream)
        if (max <= 0) return MinVolumeLevel
        return audio.getStreamVolume(VolumeStream).toFloat() / max
    }

    private fun isMutedOn(controller: MediaController?): Boolean {
        controller?.remoteVolumeInfo()?.let { return it.currentVolume <= 0 }
        return audioManager?.isStreamMute(VolumeStream) == true
    }

    private fun withTransportControls(action: (MediaController.TransportControls) -> Unit): Boolean {
        val controls = activeController()?.transportControls ?: return false
        return runSafely {
            action(controls)
            true
        }
    }

    private fun activeController(): MediaController? {
        val sessions = activeSessions()
        return sessions.firstOrNull { it.hasActivePlayback() } ?: sessions.firstOrNull()
    }

    private fun activeSessions(): List<MediaController> = try {
        sessionManager?.getActiveSessions(listenerComponent).orEmpty()
    } catch (_: SecurityException) {
        emptyList()
    }

    private fun remoteVolumeController(): MediaController? =
        activeController()?.takeIf { it.remoteVolumeInfo() != null }

    private fun listenerDetailSettingsIntent(): Intent? =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Intent(Settings.ACTION_NOTIFICATION_LISTENER_DETAIL_SETTINGS).putExtra(
                Settings.EXTRA_NOTIFICATION_LISTENER_COMPONENT_NAME,
                listenerComponent.flattenToString(),
            )
        } else {
            null
        }

    private fun startActivitySafely(intent: Intent): Boolean = try {
        context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        true
    } catch (_: ActivityNotFoundException) {
        false
    } catch (_: SecurityException) {
        false
    }

    private companion object {
        const val ListenerSettingsAction = Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS

        fun MediaController.hasActivePlayback(): Boolean = when (playbackState?.state) {
            PlaybackState.STATE_PLAYING, PlaybackState.STATE_BUFFERING -> true
            else -> false
        }

        fun MediaController.remoteVolumeInfo(): MediaController.PlaybackInfo? =
            playbackInfo.takeIf {
                it.playbackType == MediaController.PlaybackInfo.PLAYBACK_TYPE_REMOTE
            }

        inline fun runSafely(action: () -> Boolean): Boolean = try {
            action()
        } catch (_: SecurityException) {
            false
        } catch (_: IllegalArgumentException) {
            false
        }
    }
}
