package com.github.gszp1.carassistant.media

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay

private const val StatusPollIntervalMillis = 1_000L

data class MediaStatus(
    val hasAccess: Boolean = false,
    val hasSession: Boolean = false,
    val isPlaying: Boolean = false,
    val title: String? = null,
    val artist: String? = null,
    val volumeLevel: Float = 0.0f,
    val isMuted: Boolean = false,
) {
    companion object {
        val Empty = MediaStatus()
    }
}

@Stable
interface MediaControls {

    val status: MediaStatus

    fun playPause()

    fun previous()

    fun next()

    fun setVolumeLevel(level: Float)

    fun raiseVolume()

    fun lowerVolume()

    fun toggleMute()

    companion object {
        val Inactive: MediaControls = object : MediaControls {
            override val status = MediaStatus.Empty
            override fun playPause() = Unit
            override fun previous() = Unit
            override fun next() = Unit
            override fun setVolumeLevel(level: Float) = Unit
            override fun raiseVolume() = Unit
            override fun lowerVolume() = Unit
            override fun toggleMute() = Unit
        }
    }
}

@Composable
fun rememberMediaControls(
    pollIntervalMillis: Long = StatusPollIntervalMillis,
): MediaControls {
    val context = LocalContext.current
    val controls = remember(context) {
        SessionMediaControls(MediaSessionManager(context.applicationContext))
    }
    LaunchedEffect(controls, pollIntervalMillis) {
        while (true) {
            controls.refresh()
            delay(pollIntervalMillis)
        }
    }
    return controls
}

@Stable
private class SessionMediaControls(
    private val manager: MediaSessionManager,
) : MediaControls {

    override var status by mutableStateOf(MediaStatus.Empty)
        private set

    fun refresh() {
        status = manager.status()
    }

    override fun playPause() = withSessionAccess {
        status = status.copy(isPlaying = !status.isPlaying)
        manager.playPause()
    }

    override fun previous() = withSessionAccess(manager::skipToPrevious)

    override fun next() = withSessionAccess(manager::skipToNext)

    override fun setVolumeLevel(level: Float) {
        status = status.copy(volumeLevel = level)
        manager.setVolumeLevel(level)
        refresh()
    }

    override fun raiseVolume() {
        manager.raiseVolume()
        refresh()
    }

    override fun lowerVolume() {
        manager.lowerVolume()
        refresh()
    }

    override fun toggleMute() {
        status = status.copy(isMuted = !status.isMuted)
        manager.toggleMute()
        refresh()
    }

    private inline fun withSessionAccess(action: () -> Unit) {
        if (!manager.hasSessionAccess) {
            manager.requestSessionAccess()
            refresh()
            return
        }
        action()
        refresh()
    }
}
