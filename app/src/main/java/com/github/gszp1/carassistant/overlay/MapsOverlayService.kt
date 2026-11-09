package com.github.gszp1.carassistant.overlay

import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.provider.Settings
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.WindowManager
import com.github.gszp1.carassistant.R
import com.github.gszp1.carassistant.media.rememberMediaControls
import com.github.gszp1.carassistant.ui.components.MapsOverlayBar
import com.github.gszp1.carassistant.ui.theme.CarAssistantTheme
import com.github.gszp1.carassistant.ui.theme.DriveMode
import com.github.gszp1.carassistant.ui.theme.Styles

class MapsOverlayService : Service() {

    private val windowManager: WindowManager
        get() = getSystemService(WINDOW_SERVICE) as WindowManager

    private var host: OverlayComposeHost? = null

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (!Settings.canDrawOverlays(this)) {
            stopSelf()
            return START_NOT_STICKY
        }
        showBar(
            style = intent.styleExtra(),
            mode = intent.modeExtra(),
        )
        return START_NOT_STICKY
    }

    override fun onDestroy() {
        hideBar()
        super.onDestroy()
    }

    private fun showBar(style: Styles, mode: DriveMode) {
        hideBar()
        val host = OverlayComposeHost(ContextThemeWrapper(this, R.style.Theme_CarAssistant)) {
            CarAssistantTheme(style = style, mode = mode) {
                MapsOverlayBar(
                    onReturnToApp = ::returnToApp,
                    media = rememberMediaControls(),
                )
            }
        }
        host.start()
        windowManager.addView(host.view, barWindowParams())
        this.host = host
    }

    private fun hideBar() {
        host?.let { shown ->
            windowManager.removeViewImmediate(shown.view)
            shown.stop()
        }
        host = null
    }

    private fun returnToApp() {
        packageManager.getLaunchIntentForPackage(packageName)
            ?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            ?.let(::startActivity)
        stopSelf()
    }

    private fun barWindowParams() = WindowManager.LayoutParams(
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.WRAP_CONTENT,
        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
        WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
        PixelFormat.TRANSLUCENT,
    ).apply {
        gravity = Gravity.END or Gravity.CENTER_VERTICAL
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
    }

    companion object {
        private const val ExtraStyle = "com.github.gszp1.carassistant.overlay.STYLE"
        private const val ExtraMode = "com.github.gszp1.carassistant.overlay.MODE"

        fun show(context: Context, style: Styles, mode: DriveMode) {
            val intent = Intent(context, MapsOverlayService::class.java)
                .putExtra(ExtraStyle, style.name)
                .putExtra(ExtraMode, mode.name)
            context.startService(intent)
        }

        fun hide(context: Context) {
            context.stopService(Intent(context, MapsOverlayService::class.java))
        }

        private fun Intent?.styleExtra(): Styles =
            this?.getStringExtra(ExtraStyle)?.let(Styles::valueOf) ?: Styles.AQUAMARINE

        private fun Intent?.modeExtra(): DriveMode =
            this?.getStringExtra(ExtraMode)?.let(DriveMode::valueOf) ?: DriveMode.NIGHT
    }
}
