package com.techkintan.weathercast.ui.screen.weather.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.techkintan.weathercast.R
import com.techkintan.weathercast.helper.sampleData
import com.techkintan.weathercast.helper.toDisplayTime
import com.techkintan.weathercast.ui.model.DailyForecast

@Composable
fun ForecastGridCard(day: DailyForecast, modifier: Modifier = Modifier) {
    ElevatedCard(
        modifier = modifier
            .aspectRatio(1f)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {

//            Image(
//                imageVector = ImageVector.vectorResource(getWeatherIcon(day.condition)),
//                contentDescription = null,
//                modifier = Modifier
//                    .fillMaxSize()
//                    .align(Alignment.BottomEnd)
//                    .alpha(0.06f)
//                    .padding(10.dp),
//                alignment = Alignment.BottomEnd
//            )
            AnimatedWeatherIcon(modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.BottomEnd)
                    .alpha(0.06f)
                    .padding(10.dp),getWeatherIcon(day.condition))

            // Foreground weather details
            Column(
                modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(getWeatherIcon(day.condition)),
                        contentDescription = day.condition,
                        modifier = Modifier.size(30.dp)
                    )
                    Column {
                        Text(
                            day.date.toDisplayTime(),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            day.condition,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
                TempText(temp = day.temp)

                MinMaxTempText(minTemp = day.tempMin, maxTemp = day.tempMax)


                IconWithText(
                    iconRes = R.drawable.ic_humidity, label = day.humidity
                )
            }
        }
    }
}

fun getWeatherIcon(condition: String): Int {
    return when (condition.lowercase()) {
        "clear" -> R.drawable.ic_clear_day
        "clouds" -> R.drawable.ic_scattered_clouds
        "rain" -> R.drawable.ic_rain
        "drizzle" -> R.drawable.ic_shower_rain
        "thunderstorm" -> R.drawable.ic_thunderstorm
        "snow" -> R.drawable.ic_snow
        "mist", "fog", "haze", "smoke", "dust", "sand", "ash", "squall", "tornado" -> R.drawable.ic_mist
        else -> R.drawable.ic_unknown
    }
}

@Composable
@Preview(showBackground = true)
fun ForecastGridCardPreview() {
    ForecastGridCard(
        day = sampleData.last()
    )
}