package com.github.gszp1.carassistant

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.github.gszp1.carassistant.media.rememberMediaControls
import com.github.gszp1.carassistant.music.openSpotify
import com.github.gszp1.carassistant.navigation.openGoogleMaps
import com.github.gszp1.carassistant.overlay.MapsOverlayService
import com.github.gszp1.carassistant.permissions.PermissionRequest
import com.github.gszp1.carassistant.permissions.PermissionsScreen
import com.github.gszp1.carassistant.permissions.rememberPermissionsState
import com.github.gszp1.carassistant.ui.components.AppLauncherRow
import com.github.gszp1.carassistant.ui.components.MusicApp
import com.github.gszp1.carassistant.ui.components.NavigationApp
import com.github.gszp1.carassistant.ui.components.WeatherApp
import com.github.gszp1.carassistant.ui.scaffold.drive.DriveScaffold
import com.github.gszp1.carassistant.ui.theme.CarAssistantTheme
import com.github.gszp1.carassistant.ui.theme.DriveMode
import com.github.gszp1.carassistant.ui.theme.Styles
import com.github.gszp1.carassistant.ui.theme.systemDriveMode
import com.github.gszp1.carassistant.weather.WeatherScreen

private const val OverlayPermissionMessage = "Overlay permission denied – side bar is off"

class MainActivity : ComponentActivity() {

    private var overlayStyle = Styles.AQUAMARINE
    private var overlayMode = DriveMode.NIGHT
    private var overlayPermissionAsked = false

    private var pendingExternalApp: (() -> Boolean)? = null

    private val overlayPermissionRequest =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            pendingExternalApp?.let(::launchWithOverlay)
            pendingExternalApp = null
        }

    private val runtimePermissionRequest =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        configureFullScreenWindow()

        setContent {
            var style by rememberSaveable { mutableStateOf(Styles.AQUAMARINE) }
            val systemMode = systemDriveMode()
            var mode by rememberSaveable(systemMode) { mutableStateOf(systemMode) }

            SideEffect {
                overlayStyle = style
                overlayMode = mode
            }

            CarAssistantTheme(style = style, mode = mode) {
                val permissionsState = rememberPermissionsState()
                var permissionsSkipped by rememberSaveable { mutableStateOf(false) }
                if (!permissionsState.allGranted && !permissionsSkipped) {
                    PermissionsScreen(
                        permissions = permissionsState.missing,
                        onRequestPermission = { permission ->
                            when (val request = permission.request(this)) {
                                is PermissionRequest.OpenSettings ->
                                    startActivity(request.intent)
                                is PermissionRequest.RequestRuntimePermissions ->
                                    runtimePermissionRequest.launch(request.permissions.toTypedArray())
                            }
                        },
                        onSkip = { permissionsSkipped = true },
                    )
                } else {
                    var showWeather by rememberSaveable { mutableStateOf(false) }
                    if (showWeather) {
                        WeatherScreen(onBack = { showWeather = false })
                    } else {
                        DriveScaffold(
                            onSelectStyle = { style = it },
                            onSelectMode = { mode = it },
                            media = rememberMediaControls(),
                        ) {
                            AppLauncherRow(
                                onLaunch = { app ->
                                    when (app) {
                                        NavigationApp -> launchWithOverlay(::openGoogleMaps)
                                        MusicApp -> launchWithOverlay(::openSpotify)
                                        WeatherApp -> showWeather = true
                                    }
                                },
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        MapsOverlayService.hide(this)
    }


    private fun launchWithOverlay(openApp: () -> Boolean) {
        if (!Settings.canDrawOverlays(this) && !overlayPermissionAsked) {
            overlayPermissionAsked = true
            pendingExternalApp = openApp
            overlayPermissionRequest.launch(overlayPermissionIntent())
            return
        }
        if (!openApp()) return

        if (Settings.canDrawOverlays(this)) {
            MapsOverlayService.show(this, overlayStyle, overlayMode)
        } else {
            Toast.makeText(this, OverlayPermissionMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun overlayPermissionIntent(): Intent = Intent(
        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
        Uri.parse("package:$packageName"),
    )

    private fun configureFullScreenWindow() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes = window.attributes.apply {
                layoutInDisplayCutoutMode =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_ALWAYS
                    } else {
                        WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
                    }
            }
        }

        WindowCompat.getInsetsController(window, window.decorView).apply {
            systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            hide(WindowInsetsCompat.Type.systemBars())
        }
    }
}
