package com.github.gszp1.carassistant.ui.components

import android.widget.TextClock
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.github.gszp1.carassistant.R

@Composable
fun Clock(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AndroidView(
            factory = { context ->
                TextClock(context).apply {
                    format12Hour = "hh:mm a"
                    textSize = 32f
                    setTextColor(ContextCompat.getColor(context, R.color.black))
                }
            },
            modifier = Modifier.padding(5.dp),
        )
    }
}
