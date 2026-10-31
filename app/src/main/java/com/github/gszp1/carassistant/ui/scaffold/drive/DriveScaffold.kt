package com.github.gszp1.carassistant.ui.scaffold.drive

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.github.gszp1.carassistant.ui.components.AppLauncherRow
import com.github.gszp1.carassistant.ui.theme.CarAssistantTheme
import com.github.gszp1.carassistant.ui.theme.DrivePalette
import com.github.gszp1.carassistant.ui.theme.LocalDrivePalette
import com.github.gszp1.carassistant.ui.theme.Styles

private const val NightMode = Configuration.UI_MODE_NIGHT_YES or Configuration.UI_MODE_TYPE_NORMAL
private const val DayMode = Configuration.UI_MODE_NIGHT_NO or Configuration.UI_MODE_TYPE_NORMAL

@Composable
fun DriveScaffold(
    modifier: Modifier = Modifier,
    palette: DrivePalette = LocalDrivePalette.current,
    onOpenAppStyle: () -> Unit = {},
    onToggleMode: () -> Unit = {},
    onOpenAppGrid: () -> Unit = {},
    content: @Composable () -> Unit = {},
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .driveContentBackground(palette)
                .padding(innerPadding),
        ) {
            DriveTopBar(
                modifier = Modifier.weight(0.2f),
                palette = palette,
                onOpenAppStyle = onOpenAppStyle,
                onToggleMode = onToggleMode,
                onOpenAppGrid = onOpenAppGrid,
            )
            Box(
                modifier = Modifier
                    .weight(0.65f)
                    .fillMaxWidth()
            ) {
                content()
            }
            DriveBottomBar(
                modifier = Modifier.weight(0.15f),
                palette = palette,
            )
        }
    }
}

@Preview(name = "Style – aquamarine – night", widthDp = 960, heightDp = 540, uiMode = NightMode)
@Preview(name = "Style – aquamarine – day", widthDp = 960, heightDp = 540, uiMode = DayMode)
@Composable
private fun DriveScaffoldAquamarinePreview() = DriveScaffoldPreview(Styles.AQUAMARINE)

@Preview(name = "Style – amber shift – night", widthDp = 960, heightDp = 540, uiMode = NightMode)
@Preview(name = "Style – amber shift – day", widthDp = 960, heightDp = 540, uiMode = DayMode)
@Composable
private fun DriveScaffoldAmberShiftPreview() = DriveScaffoldPreview(Styles.AMBER_SHIFT)

@Preview(name = "Style – emerald core – night", widthDp = 960, heightDp = 540, uiMode = NightMode)
@Preview(name = "Style – emerald core – day", widthDp = 960, heightDp = 540, uiMode = DayMode)
@Composable
private fun DriveScaffoldEmeraldCorePreview() = DriveScaffoldPreview(Styles.EMERALD_CORE)

@Preview(name = "Style – ruby dynamic – night", widthDp = 960, heightDp = 540, uiMode = NightMode)
@Preview(name = "Style – ruby dynamic – day", widthDp = 960, heightDp = 540, uiMode = DayMode)
@Composable
private fun DriveScaffoldRubyDynamicPreview() = DriveScaffoldPreview(Styles.RUBY_DYNAMIC)

@Composable
private fun DriveScaffoldPreview(style: Styles) {
    CarAssistantTheme(style = style) {
        DriveScaffold {
            AppLauncherRow()
        }
    }
}
