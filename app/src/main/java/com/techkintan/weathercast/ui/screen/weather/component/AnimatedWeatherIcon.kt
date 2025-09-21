package com.techkintan.weathercast.ui.screen.weather.component

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.RepeatMode.*
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AnimatedWeatherIcon(
    modifier: Modifier = Modifier,
    @DrawableRes weatherIcon: Int
) {
    // infinite transition for looping animation
    val infiniteTransition = rememberInfiniteTransition()

    val offsetY by infiniteTransition.animateFloat(
        initialValue = -20f,
        targetValue = 20f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = Reverse
        )
    )

    Image(
        imageVector = ImageVector.vectorResource(weatherIcon),
        contentDescription = null,
        modifier = modifier
            .graphicsLayer { translationY = offsetY },
        alignment = Alignment.CenterEnd
    )
}

@Preview(showBackground = true)
@Composable
fun AnimatedWeatherIconPreview() {
    AnimatedWeatherIcon(weatherIcon = com.techkintan.weathercast.R.drawable.ic_scattered_clouds)
}
