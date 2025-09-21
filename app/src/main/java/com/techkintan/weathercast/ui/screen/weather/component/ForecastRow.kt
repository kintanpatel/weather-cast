package com.techkintan.weathercast.ui.screen.weather.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.techkintan.weathercast.R
import com.techkintan.weathercast.helper.sampleData
import com.techkintan.weathercast.helper.toDisplayHour
import com.techkintan.weathercast.ui.model.DailyForecast

@Composable
fun ForecastRow(day: DailyForecast) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Box {
            // Faded Background Icon
            AnimatedWeatherIcon(
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.08f) // light fade
                    .padding(end = 16.dp),
                getWeatherIcon(day.condition)
            )
            /*Image(
                imageVector = ImageVector.vectorResource(getWeatherIcon(day.condition)),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.08f) // light fade
                    .padding(end = 16.dp),
                alignment = Alignment.CenterEnd
            )*/

            // Foreground Content
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(getWeatherIcon(day.condition)),
                        contentDescription = day.condition,
                        modifier = Modifier.size(36.dp)
                    )

                    Text(day.condition, style = MaterialTheme.typography.titleMedium)
                }

                Spacer(Modifier.height(10.dp))


                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_temp),
                        contentDescription = null,
                        modifier = Modifier.size(28.dp)
                    )
                    MinMaxTempText(minTemp = day.tempMin, maxTemp = day.tempMax)
                }
                Spacer(Modifier.height(10.dp))
                Row {
                    Spacer(Modifier.width(6.dp))
                    IconWithText(iconRes = R.drawable.ic_humidity, label = day.humidity)
                }
            }
            TempText(
                temp = day.temp,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(horizontal = 12.dp),
            )
        }
    }
}


@Composable
fun ForecastTimelineRow(
    forecast: DailyForecast
) {
    Row(
        Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            modifier = Modifier.width(60.dp),
            text = forecast.date.toDisplayHour(), // e.g. "09:00"
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )
        // Right side → your existing row card
        ForecastRow(forecast)
    }
}

@Composable
@Preview(showBackground = true)
fun ForecastRowPreview() {
    ForecastTimelineRow(forecast = sampleData.first())
}