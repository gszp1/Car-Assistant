package com.github.gszp1.carassistant.weather

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withLink
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.gszp1.carassistant.ui.components.RoundedTrapezoidShape
import com.github.gszp1.carassistant.ui.components.TimeText
import com.github.gszp1.carassistant.ui.components.WeatherIcon
import com.github.gszp1.carassistant.ui.components.WeatherSymbol
import com.github.gszp1.carassistant.ui.scaffold.drive.driveContentBackground
import com.github.gszp1.carassistant.ui.theme.CarAssistantTheme
import com.github.gszp1.carassistant.ui.theme.DrivePalette
import com.github.gszp1.carassistant.ui.theme.LocalDrivePalette
import com.github.gszp1.carassistant.ui.theme.Styles
import java.time.Instant

private val BackButtonPadding = 16.dp
private val BackButtonSize = 52.dp
private val BackButtonBorderWidth = 1.5.dp
private val BackIconSize = 32.dp

private val TopBarHeight = 88.dp
private const val TopBarWidthFraction = 0.6f
private val TopBarHorizontalPadding = 32.dp
private val TopBarBorderWidth = 1.5.dp
private val TopBarSlant = 28.dp
private val TopBarCornerRadius = 14.dp
private val LocationIconSize = 26.dp
private val LocationIconGap = 10.dp
private val WarningIconSize = 30.dp
private val WarningIconGap = 2.dp
private val WarningMaxWidth = 240.dp
private const val WarningMaxLines = 2
private const val ClockTextSizeSp = 20f
private const val ClockFormat12Hour = "h:mm a"
private const val ClockFormat24Hour = "HH:mm"

private val WeatherCircleSize = 220.dp
private val WeatherCircleBorderWidth = 1.5.dp
private val WeatherIconSize = 240.dp
private val ConditionGap = 14.dp
private val ConditionWidth = WeatherCircleSize
private val ConditionCornerRadius = 16.dp
private val ConditionShape = RoundedCornerShape(ConditionCornerRadius)
private val ConditionBorderWidth = 1.5.dp
private val ConditionHorizontalPadding = 24.dp
private val ConditionVerticalPadding = 10.dp
private val LabelIndent = 32.dp
private val MetricColumnWidth = 220.dp
private val LabelSpacing = 16.dp
private val LabelToValueGap = 2.dp

private val AttributionBarHeight = 16.dp
private val AttributionDividerHeight = 0.5.dp
private val AttributionHorizontalPadding = 12.dp
private const val AttributionTextSizeSp = 8f
private const val AttributionLineHeightSp = 10f

private const val AttributionPrefix = "Weather data by "
private const val AttributionLinkLabel = "Open-Meteo.com"
private const val AttributionLinkUrl = "https://open-meteo.com/"
private const val UpdatedPrefix = "Updated "

private const val LocatingLabel = "Locating…"
private const val LoadingLabel = "Loading weather…"

private data class WeatherMetric(val label: String, val value: String?)

@Composable
fun WeatherScreen(
    uiState: WeatherUiState,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {},
    onRefresh: () -> Unit = {},
    palette: DrivePalette = LocalDrivePalette.current,
) {
    val weather = uiState.weather
    Surface(
        modifier = modifier.fillMaxSize(),
        color = Color.Transparent,
        contentColor = palette.barLabel,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .driveContentBackground(palette),
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                WeatherTopBar(
                    location = uiState.placeName ?: LocatingLabel,
                    warning = uiState.errorMessage ?: weather?.let(::warningFor),
                    palette = palette,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth(TopBarWidthFraction),
                )
                Box(modifier = Modifier.fillMaxWidth().weight(1f)) {
                    Row(
                        modifier = Modifier.align(Alignment.Center),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        WeatherMetricColumn(
                            metrics = windMetrics(weather),
                            horizontalAlignment = Alignment.End,
                        )
                        Spacer(Modifier.width(LabelIndent))
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(ConditionGap),
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(WeatherCircleSize)
                                    .border(WeatherCircleBorderWidth, palette.barOutline, CircleShape)
                                    .clickable(enabled = !uiState.isLoading, onClick = onRefresh),
                                contentAlignment = Alignment.Center,
                            ) {
                                WeatherIcon(
                                    symbol = weather?.symbol ?: WeatherSymbol.NOT_AVAILABLE,
                                    modifier = Modifier.requiredSize(WeatherIconSize),
                                )
                            }
                            Text(
                                text = uiState.conditionLabel(),
                                style = MaterialTheme.typography.titleMedium,
                                textAlign = TextAlign.Center,
                                modifier = Modifier
                                    .width(ConditionWidth)
                                    .background(palette.barMid, ConditionShape)
                                    .border(ConditionBorderWidth, palette.barOutline, ConditionShape)
                                    .padding(
                                        horizontal = ConditionHorizontalPadding,
                                        vertical = ConditionVerticalPadding,
                                    ),
                            )
                        }
                        Spacer(Modifier.width(LabelIndent))
                        WeatherMetricColumn(
                            metrics = airMetrics(weather),
                            horizontalAlignment = Alignment.Start,
                        )
                    }
                }
                WeatherAttributionBar(observedAt = weather?.observedAt, palette = palette)
            }
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(BackButtonPadding)
                    .size(BackButtonSize)
                    .border(BackButtonBorderWidth, palette.barOutline, CircleShape),
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.size(BackIconSize),
                    tint = palette.iconAccent,
                )
            }
        }
    }
}

private fun WeatherUiState.conditionLabel(): String = when {
    weather != null -> weather.condition.description
    isLoading -> LoadingLabel
    else -> WeatherCondition.UNKNOWN.description
}

private fun windMetrics(weather: CurrentWeather?): List<WeatherMetric> = listOf(
    WeatherMetric("Wind", weather?.let { formatSpeed(it.windSpeedKmh) }),
    WeatherMetric("Wind gusts", weather?.let { formatSpeed(it.windGustsKmh) }),
    WeatherMetric("Wind direction", weather?.let { formatBearing(it.windDirection, it.windBearingDegrees) }),
    WeatherMetric("Visibility", weather?.visibilityMeters?.let(::formatVisibility)),
    WeatherMetric("Precipitation", weather?.let { formatPrecipitation(it.precipitationMillimeters) }),
)

private fun airMetrics(weather: CurrentWeather?): List<WeatherMetric> = listOf(
    WeatherMetric("Temperature", weather?.let { formatTemperature(it.temperatureCelsius) }),
    WeatherMetric("Feels like", weather?.let { formatTemperature(it.apparentTemperatureCelsius) }),
    WeatherMetric("Pressure", weather?.let { formatPressure(it.pressureHectopascals) }),
    WeatherMetric("Humidity", weather?.let { formatPercent(it.relativeHumidityPercent) }),
    WeatherMetric("Precipitation chance", weather?.precipitationProbabilityPercent?.let(::formatPercent)),
)

@Composable
private fun WeatherAttributionBar(
    observedAt: Instant?,
    palette: DrivePalette,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(AttributionDividerHeight)
                .background(palette.barOutline),
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(AttributionBarHeight)
                .background(palette.barMid),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = buildAnnotatedString {
                    append(AttributionPrefix)
                    withLink(
                        LinkAnnotation.Url(
                            url = AttributionLinkUrl,
                            styles = TextLinkStyles(
                                style = SpanStyle(
                                    color = palette.iconAccent,
                                    textDecoration = TextDecoration.Underline,
                                ),
                            ),
                        ),
                    ) {
                        append(AttributionLinkLabel)
                    }
                },
                style = MaterialTheme.typography.labelSmall,
                fontSize = AttributionTextSizeSp.sp,
                lineHeight = AttributionLineHeightSp.sp,
                maxLines = 1,
                textAlign = TextAlign.Center,
            )
            if (observedAt != null) {
                Text(
                    text = UpdatedPrefix + formatObservedAt(observedAt),
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = AttributionTextSizeSp.sp,
                    lineHeight = AttributionLineHeightSp.sp,
                    maxLines = 1,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(horizontal = AttributionHorizontalPadding),
                )
            }
        }
    }
}

@Composable
private fun WeatherMetricColumn(
    metrics: List<WeatherMetric>,
    horizontalAlignment: Alignment.Horizontal,
    modifier: Modifier = Modifier,
) {
    val textAlign = if (horizontalAlignment == Alignment.End) TextAlign.End else TextAlign.Start
    Column(
        modifier = modifier.width(MetricColumnWidth),
        verticalArrangement = Arrangement.spacedBy(LabelSpacing),
        horizontalAlignment = horizontalAlignment,
    ) {
        metrics.forEach { metric ->
            Column(
                verticalArrangement = Arrangement.spacedBy(LabelToValueGap),
                horizontalAlignment = horizontalAlignment,
            ) {
                Text(
                    text = metric.label,
                    style = MaterialTheme.typography.labelMedium,
                    textAlign = textAlign,
                )
                Text(
                    text = metric.value ?: UnknownValue,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = textAlign,
                )
            }
        }
    }
}

@Composable
private fun WeatherTopBar(
    location: String,
    warning: String?,
    palette: DrivePalette,
    modifier: Modifier = Modifier,
) {
    val shape = remember { RoundedTrapezoidShape(TopBarSlant, TopBarCornerRadius) }
    Box(
        modifier = modifier
            .height(TopBarHeight)
            .background(palette.barMid, shape)
            .border(TopBarBorderWidth, palette.barOutline, shape)
            .padding(horizontal = TopBarHorizontalPadding),
    ) {
        Row(
            modifier = Modifier.align(Alignment.CenterStart),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = null,
                modifier = Modifier.size(LocationIconSize),
                tint = palette.iconAccent,
            )
            Spacer(Modifier.width(LocationIconGap))
            Text(text = location, style = MaterialTheme.typography.titleLarge)
        }
        if (warning != null) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .widthIn(max = WarningMaxWidth),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(WarningIconGap),
            ) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = "Weather warning",
                    modifier = Modifier.size(WarningIconSize),
                    tint = MaterialTheme.colorScheme.error,
                )
                Text(
                    text = warning,
                    style = MaterialTheme.typography.labelMedium,
                    textAlign = TextAlign.Center,
                    maxLines = WarningMaxLines,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
        TimeText(
            modifier = Modifier.align(Alignment.CenterEnd),
            color = palette.barLabel,
            format12Hour = ClockFormat12Hour,
            format24Hour = ClockFormat24Hour,
            textSizeSp = ClockTextSizeSp,
        )
    }
}

private val PreviewWeather = CurrentWeather(
    observedAt = Instant.ofEpochSecond(1_763_294_400),
    isDay = true,
    condition = WeatherCondition.PARTLY_CLOUDY,
    temperatureCelsius = 21.3,
    apparentTemperatureCelsius = 20.1,
    relativeHumidityPercent = 58,
    pressureHectopascals = 1013.4,
    windSpeedKmh = 18.2,
    windGustsKmh = 62.4,
    windBearingDegrees = 310,
    precipitationMillimeters = 0.8,
    visibilityMeters = 8_000.0,
    precipitationProbabilityPercent = 20,
)

@Preview(name = "weather-screen", widthDp = 960, heightDp = 540)
@Composable
private fun WeatherScreenPreview() {
    CarAssistantTheme(style = Styles.AQUAMARINE) {
        WeatherScreen(
            uiState = WeatherUiState(placeName = "Krakow", weather = PreviewWeather),
        )
    }
}

@Preview(name = "weather-screen-unavailable", widthDp = 960, heightDp = 540)
@Composable
private fun WeatherScreenUnavailablePreview() {
    CarAssistantTheme(style = Styles.AQUAMARINE) {
        WeatherScreen(
            uiState = WeatherUiState(errorMessage = "Grant the location permission to see local weather"),
        )
    }
}
