package com.turing.googlenano.animation

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.turing.googlenano.R

// @Credit: Anatolii Frolov

@Composable
fun PulseAnimationImage(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
) {
    val periodMs = 3600L
    val offsetsMs = longArrayOf(0L, 1200L, 2400L)

    val startNs = remember { System.nanoTime() }
    var frameTimeNs by remember { mutableLongStateOf(startNs) }

    LaunchedEffect(Unit) {
        while (true) {
            withFrameNanos { now -> frameTimeNs = now }
        }
    }

    fun phase(offsetMs: Long): Float {
        val elapsedMs = (frameTimeNs - startNs) / 1_000_000L + offsetMs
        return ((elapsedMs % periodMs).toFloat() / periodMs.toFloat())
    }

    Box(modifier
        .wrapContentSize()
        .padding(16.dp), contentAlignment = Alignment.Center) {
        @Composable
        fun Ring(p: Float) = Box(
            Modifier
                .matchParentSize()
                .graphicsLayer {
                    scaleX = 1f + 0.8f * p
                    scaleY = 1f + 0.8f * p
                    alpha = 1f - p
                }
                .border(1.5.dp, Color.Green.copy(alpha = 0.9f), CircleShape)
        )

        Ring(phase(offsetsMs[0]))
        Ring(phase(offsetsMs[1]))
        Ring(phase(offsetsMs[2]))

        Box(
            Modifier
                .size(80.dp)
                .background(Color.Green, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Preview
@Composable
fun PreviewPulseAnimationImage() {
    PulseAnimationImage(
        icon = R.drawable.logo_mitg,
    )
}
