package com.github.gszp1.carassistant.ui.scaffold.drive

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.github.gszp1.carassistant.ui.theme.LocalDrivePalette

internal const val BarSafeContentHeight = 0.6f

private val BarIconSize = 22.dp
private val BarIconSpacing = 2.dp
private val BarContentPadding = PaddingValues(horizontal = 12.dp, vertical = 0.dp)

@Composable
internal fun DriveBarButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
        contentPadding = BarContentPadding,
        colors = ButtonDefaults.textButtonColors(
            contentColor = LocalDrivePalette.current.iconAccent,
        ),
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(BarIconSpacing),
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(BarIconSize),
                )
            }
            Text(
                text = text,
                textAlign = TextAlign.Center,
                maxLines = 2,
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}
