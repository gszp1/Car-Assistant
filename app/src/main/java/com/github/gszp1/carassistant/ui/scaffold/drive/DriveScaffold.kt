package com.github.gszp1.carassistant.ui.scaffold.drive

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

@Composable
fun DriveScaffold(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit = {},
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .driveContentBackground()
                .padding(innerPadding),
        ) {
            DriveTopBar(Modifier.weight(0.2f))
            Box(
                modifier = Modifier
                    .weight(0.65f)
                    .fillMaxWidth()
            ) {
                content()
            }
            DriveBottomBar(Modifier.weight(0.15f))
        }
    }
}

@Preview(name = "Tryb jazdy", widthDp = 960, heightDp = 540)
@Composable
private fun DriveScaffoldPreview() {
    CarAssistantTheme {
        DriveScaffold {
            AppLauncherRow()
        }
    }
}
