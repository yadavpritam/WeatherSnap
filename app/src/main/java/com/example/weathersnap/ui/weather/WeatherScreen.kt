package com.example.weathersnap.ui.weather

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weathersnap.domain.model.WeatherData
import com.example.weathersnap.ui.theme.DarkGreenText
import com.example.weathersnap.ui.theme.MutedGray
import com.example.weathersnap.ui.theme.OliveFill
import com.example.weathersnap.ui.theme.OliveHeader

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(
    onNavigateToCreateReport: (WeatherData) -> Unit,
    onNavigateToReports: () -> Unit,
    viewModel: WeatherViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        // Header Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(OliveHeader)
                .padding(top = 48.dp, bottom = 24.dp, start = 16.dp, end = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "WeatherSnap",
                        style = MaterialTheme.typography.titleLarge,
                        color = DarkGreenText
                    )
                    Text(
                        text = "Live weather reports with camera evidence",
                        style = MaterialTheme.typography.labelMedium,
                        color = DarkGreenText.copy(alpha = 0.7f)
                    )
                }
                OutlinedButton(
                    onClick = onNavigateToReports,
                    shape = RoundedCornerShape(50),
                    border = ButtonDefaults.outlinedButtonBorder(true).copy(width = 1.dp)
                ) {
                    Text("Reports", style = MaterialTheme.typography.bodyMedium, color = DarkGreenText)
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Search Input
            OutlinedTextField(
                value = uiState.searchQuery,
                onValueChange = { viewModel.onSearchQueryChanged(it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text("City") },
                placeholder = { Text("Enter city name...") },
                supportingText = {
                    Text("Enter more than 2 letters to start city suggestions.")
                },
                trailingIcon = {
                    IconButton(onClick = { /* Search logic handled by query change */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp)
            )

            // Suggestions Dropdown
            Column(modifier = Modifier.fillMaxWidth()) {
                AnimatedVisibility(
                    visible = uiState.suggestions.isNotEmpty(),
                    enter = fadeIn() + slideInVertically(),
                    exit = fadeOut() + slideOutVertically()
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        elevation = CardDefaults.cardElevation(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        LazyColumn(modifier = Modifier.heightIn(max = 200.dp)) {
                            items(uiState.suggestions) { city ->
                                ListItem(
                                    headlineContent = { Text("${city.name}, ${city.country}") },
                                    modifier = Modifier.clickable { viewModel.onCitySelected(city) }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Main Content Area
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Crossfade(targetState = uiState, label = "WeatherStateCrossfade") { state ->
                    when {
                        state.isLoadingWeather -> CircularProgressIndicator(color = OliveFill)
                        state.weatherData != null -> {
                            WeatherCard(
                                data = state.weatherData,
                                onCreateReport = { onNavigateToCreateReport(state.weatherData) }
                            )
                        }
                        state.error != null -> {
                            ErrorCard(message = state.error)
                        }
                        state.searchQuery.isEmpty() -> {
                            EmptyState(message = "Search for a city to see the weather")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherCard(data: WeatherData, onCreateReport: () -> Unit) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize(),
        colors = CardDefaults.elevatedCardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(
                        text = data.cityName,
                        style = MaterialTheme.typography.headlineMedium,
                        color = DarkGreenText
                    )
                    Text(
                        text = data.country,
                        style = MaterialTheme.typography.labelMedium,
                        color = MutedGray
                    )
                }
                Text(
                    text = "${data.temperature.toInt()}°C",
                    style = MaterialTheme.typography.headlineLarge,
                    color = DarkGreenText
                )
            }

            Text(
                text = data.condition,
                style = MaterialTheme.typography.bodyMedium,
                color = MutedGray,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                WeatherChip(label = "Humidity", value = "${data.humidity}%")
                WeatherChip(label = "Wind", value = "${data.windSpeed} m/s")
                WeatherChip(label = "Pressure", value = "${data.pressure.toInt()} hPa")
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp), thickness = 0.5.dp, color = MutedGray.copy(alpha = 0.3f))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Info, contentDescription = null, tint = OliveFill, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Camera and Room DB enabled",
                    style = MaterialTheme.typography.labelSmall,
                    color = MutedGray
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onCreateReport,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = OliveFill, contentColor = DarkGreenText),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Create Report", fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
fun WeatherChip(label: String, value: String) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFFEAF3DE)
    ) {
        Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)) {
            Text(text = label, style = MaterialTheme.typography.labelSmall, color = MutedGray)
            Text(text = value, style = MaterialTheme.typography.bodyMedium, color = Color(0xFF3B6D11), fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun EmptyState(message: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(64.dp), tint = MutedGray.copy(alpha = 0.5f))
        Text(text = message, color = MutedGray)
    }
}

@Composable
fun ErrorCard(message: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
    ) {
        Text(text = message, color = Color.Red, modifier = Modifier.padding(16.dp))
    }
}
