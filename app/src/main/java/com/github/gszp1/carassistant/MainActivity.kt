package com.github.gszp1.carassistant

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.github.gszp1.carassistant.ui.theme.CarAssistantTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

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

        setContent {
            CarAssistantTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    contentWindowInsets = WindowInsets(0, 0, 0, 0)
                ) { innerPadding ->
                    Box(Modifier
                        .fillMaxSize()
                        .background(Color(0xFFCBCBCB))
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize().padding(innerPadding),
                        ){
                            TopBar(Modifier.weight(0.15f))
                            MidBar(Modifier.weight(0.7f))
                            BottomBar(Modifier.weight(0.15f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    accentColor: Color = Color(0xFF00BCD4),
    accentWidth: Dp = 5.dp,
) {
    Row(
        modifier
            .fillMaxWidth()
            .fillMaxHeight(0.2f)
            .background(Color(0xFF6E6E6E), TopArcShape)
            .arcOutline(accentColor, accentWidth, ::topArcOutlinePath)
    ){}
}

@Composable
fun MidBar(modifier: Modifier = Modifier) {
    Row(
        modifier
            .fillMaxWidth()
            .fillMaxHeight(0.6f)
            .background(Color(0xFFCBCBCB))
    ){}
}

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    accentColor: Color = Color(0xFF00BCD4),
    accentWidth: Dp = 5.dp,
) {
    Row(
        modifier
            .fillMaxWidth()
            .fillMaxHeight(0.2f)
            .background(Color(0xFF737373), BottomArcShape)
            .arcOutline(accentColor, accentWidth, ::bottomArcOutlinePath)
    ){}
}

val TopArcShape = GenericShape { size, _ ->
    val edge = size.height * 0.6f
    moveTo(0f, 0f)
    lineTo(size.width, 0f)
    lineTo(size.width, edge)
    quadraticTo(size.width / 2, size.height * 1.4f, 0f, edge)
    close()
}

val BottomArcShape = GenericShape { size, _ ->
    val edge = size.height * 0.4f
    moveTo(0f, size.height)
    lineTo(size.width, size.height)
    lineTo(size.width, edge)
    quadraticTo(size.width / 2, size.height * -0.4f, 0f, edge)
    close()
}

private fun topArcOutlinePath(size: Size) = Path().apply {
    val edge = size.height * 0.6f
    moveTo(size.width, edge)
    quadraticTo(size.width / 2, size.height * 1.4f, 0f, edge)
}

private fun bottomArcOutlinePath(size: Size) = Path().apply {
    val edge = size.height * 0.4f
    moveTo(size.width, edge)
    quadraticTo(size.width / 2, size.height * -0.4f, 0f, edge)
}

private fun Modifier.arcOutline(
    color: Color,
    width: Dp,
    outline: (Size) -> Path,
) = drawWithContent {
    drawContent()
    drawPath(
        path = outline(size),
        color = color,
        style = Stroke(width = width.toPx(), cap = StrokeCap.Round)
    )
}