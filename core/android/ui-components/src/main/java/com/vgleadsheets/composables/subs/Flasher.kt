package com.vgleadsheets.composables.subs

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vgleadsheets.themes.VglsMaterial

@Composable
fun Flasher(
    startDelay: Int = 0
) {
    val infiniteTransition = rememberInfiniteTransition()
    val animatedAlphaValue by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 500 - (startDelay / 3),
                delayMillis = startDelay,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Reverse
        )
    )

    Surface(
        modifier = Modifier
            .fillMaxSize()
            .alpha(animatedAlphaValue),
        color = MaterialTheme.colorScheme.inverseSurface,
    ) { }
}

@Preview
@Composable
private fun LightCircle() {
    VglsMaterial(useDarkTheme = false) {
        Box(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.background
            )
        ) {
            Sample()
        }
    }
}

@Preview
@Composable
private fun DarkCircle() {
    VglsMaterial(useDarkTheme = true) {
        Box(
            modifier = Modifier.background(
                color = MaterialTheme.colorScheme.background
            )
        ) {
            Sample()
        }
    }
}

@Composable
private fun Sample() {
    ElevatedCircle(
        Modifier.size(64.dp)
    ) {
        Flasher()
    }
}
