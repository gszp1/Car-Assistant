package com.github.gszp1.carassistant.ui.components

import android.widget.TextClock
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.github.gszp1.carassistant.ui.theme.LocalDrivePalette

@Composable
fun Clock(
    modifier: Modifier = Modifier,
    color: Color = LocalDrivePalette.current.iconAccent,
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AndroidView(
            factory = { context ->
                TextClock(context).apply {
                    format12Hour = "hh:mm a"
                    textSize = 48f
                }
            },
            update = { clock -> clock.setTextColor(color.toArgb()) },
            modifier = Modifier.padding(5.dp),
        )
    }
}
