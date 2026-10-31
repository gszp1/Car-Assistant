package com.github.gszp1.carassistant.ui.scaffold.drive

import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.github.gszp1.carassistant.ui.theme.LocalDrivePalette

internal const val BarSafeContentHeight = 0.6f

@Composable
internal fun DriveBarButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.textButtonColors(
            contentColor = LocalDrivePalette.current.iconAccent,
        ),
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            maxLines = 2,
            style = MaterialTheme.typography.labelLarge,
        )
    }
}
