package com.github.gszp1.carassistant.weather

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val RefreshIntervalMillis = 10 * 60 * 1000L

private const val GenericFailureMessage = "Weather is unavailable"

@Immutable
data class WeatherUiState(
    val placeName: String? = null,
    val weather: CurrentWeather? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

@Stable
class WeatherState internal constructor(
    private val scope: CoroutineScope,
    private val client: OpenMeteoClient,
    private val locations: LocationSource,
    private val places: PlaceNames,
) {
    var uiState: WeatherUiState by mutableStateOf(WeatherUiState(isLoading = true))
        private set

    private var inFlight: Job? = null

    fun refresh() {
        if (inFlight?.isActive == true) return
        inFlight = scope.launch { load() }
    }

    private suspend fun load() {
        uiState = uiState.copy(isLoading = true, errorMessage = null)

        val point = locations.currentPoint().getOrElse { return fail(it) }
        val weather = client.currentWeather(point).getOrElse { return fail(it) }

        uiState = WeatherUiState(
            placeName = places.nameFor(point) ?: formatCoordinates(point),
            weather = weather,
        )
    }

    private fun fail(cause: Throwable) {
        uiState = uiState.copy(
            isLoading = false,
            errorMessage = cause.message?.ifBlank { null } ?: GenericFailureMessage,
        )
    }
}

@Composable
fun rememberWeatherState(refreshIntervalMillis: Long = RefreshIntervalMillis): WeatherState {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val state = remember(context, scope) {
        WeatherState(
            scope = scope,
            client = OpenMeteoClient.create(),
            locations = LocationSource.create(context),
            places = PlaceNames.create(context),
        )
    }
    LaunchedEffect(state, refreshIntervalMillis) {
        while (true) {
            state.refresh()
            delay(refreshIntervalMillis)
        }
    }
    return state
}
