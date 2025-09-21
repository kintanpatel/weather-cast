package com.techkintan.weathercast.ui.screen.weather.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
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
fun MinMaxTempText(
    modifier: Modifier = Modifier,
    minTemp: String,
    maxTemp: String,
    unit: String = "°C"
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        verticalAlignment = Alignment.Top // aligns unit with number baseline
    ) {
        Text(
            text = "↓ $minTemp",
            modifier = modifier,
            style = MaterialTheme.typography.bodySmall.copy(
                fontFamily = allertaStencil
            )
        )
        Text(
            text = unit,
            style = MaterialTheme.typography.bodySmall.copy(
                fontFamily = allertaStencil,
                fontWeight = FontWeight.Normal,
                fontSize = 8.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            ),
            modifier = Modifier.padding(bottom = 8.dp) // lift it visually
        )
        Text(
            text = "↑ $maxTemp",
            modifier = modifier,
            style = MaterialTheme.typography.bodySmall.copy(
                fontFamily = allertaStencil

            )
        )
        Text(
            text = unit,
            style = MaterialTheme.typography.bodySmall.copy(
                fontFamily = allertaStencil,
                fontWeight = FontWeight.Normal,
                fontSize = 8.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            ),
            modifier = Modifier.padding(bottom = 8.dp) // lift it visually
        )

    }

}
