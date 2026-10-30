package com.github.gszp1.carassistant.ui.scaffold.drive

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.gszp1.carassistant.ui.components.Clock
import com.github.gszp1.carassistant.ui.theme.DriveBarBrush
import com.github.gszp1.carassistant.ui.theme.DriveBarOutline
import com.github.gszp1.carassistant.ui.theme.DriveIconAccent

private const val SafeContentHeight = 0.6f

@Composable
fun DriveTopBar(
    modifier: Modifier = Modifier,
    accentColor: Color = DriveBarOutline,
    accentWidth: Dp = 3.dp,
    onOpenAppStyle: () -> Unit = {},
    onOpenAppGrid: () -> Unit = {},
) {
    Box(
        modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(DriveBarBrush, TopArcShape)
            .arcOutline(accentColor, accentWidth, ::topArcOutlinePath)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalAlignment = Alignment.Top,
        ) {
            DriveBarButton(
                text = "Styl aplikacji",
                onClick = onOpenAppStyle,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(SafeContentHeight),
            )
            Clock(Modifier.weight(1f))
            DriveBarButton(
                text = "Siatka aplikacji",
                onClick = onOpenAppGrid,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(SafeContentHeight),
            )
        }
    }
}

@Composable
private fun DriveBarButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.textButtonColors(contentColor = DriveIconAccent),
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            maxLines = 2,
            style = MaterialTheme.typography.labelLarge,
        )
    }
}
