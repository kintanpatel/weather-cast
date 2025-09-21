package com.techkintan.weathercast.ui.screen.weather.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.techkintan.weathercast.ui.theme.allertaStencil

@Composable
fun TempText(
    modifier: Modifier = Modifier,
    temp: String,
    unit: String = "°C"
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top // aligns unit with number baseline
    ) {
        Text(
            text = temp,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontFamily = allertaStencil,
                color = MaterialTheme.colorScheme.onSurface
            )
        )
        Spacer(Modifier.width(4.dp))
        Text(
            text = unit,
            style = MaterialTheme.typography.bodySmall.copy(
                fontFamily = allertaStencil,
                fontSize = 20.sp, // smaller unit
                fontWeight = FontWeight.Normal,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            ),
            modifier = Modifier.padding(bottom = 8.dp) // lift it visually
        )
    }
}

