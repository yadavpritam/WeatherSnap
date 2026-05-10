package com.example.weathersnap.ui.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathersnap.domain.model.CityResult
import com.example.weathersnap.domain.model.WeatherData
import com.example.weathersnap.domain.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeatherUiState())
    val uiState: StateFlow<WeatherUiState> = _uiState.asStateFlow()

    private val suggestionsCache = mutableMapOf<String, List<CityResult>>()
    private var searchJob: Job? = null

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        
        if (query.length > 2) {
            if (suggestionsCache.containsKey(query)) {
                _uiState.update { it.copy(suggestions = suggestionsCache[query]!!) }
            } else {
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500) // Debounce
                    _uiState.update { it.copy(isLoadingSuggestions = true) }
                    repository.searchCity(query).onSuccess { suggestions ->
                        suggestionsCache[query] = suggestions
                        _uiState.update { it.copy(suggestions = suggestions, isLoadingSuggestions = false) }
                    }.onFailure { 
                        _uiState.update { it.copy(isLoadingSuggestions = false) }
                    }
                }
            }
        } else {
            _uiState.update { it.copy(suggestions = emptyList()) }
        }
    }

    fun onCitySelected(city: CityResult) {
        _uiState.update { it.copy(
            selectedCityName = city.name, 
            selectedCountry = city.country,
            suggestions = emptyList(),
            isLoadingWeather = true,
            weatherData = null,
            error = null
        ) }
        
        viewModelScope.launch {
            repository.getWeatherData(city.latitude, city.longitude, city.name, city.country)
                .onSuccess { data ->
                    _uiState.update { it.copy(weatherData = data, isLoadingWeather = false) }
                }
                .onFailure { error ->
                    _uiState.update { it.copy(error = error.message ?: "Failed to fetch weather", isLoadingWeather = false) }
                }
        }
    }

    fun clearSearch() {
        _uiState.update { it.copy(searchQuery = "", suggestions = emptyList()) }
    }
}

data class WeatherUiState(
    val searchQuery: String = "",
    val suggestions: List<CityResult> = emptyList(),
    val isLoadingSuggestions: Boolean = false,
    val selectedCityName: String? = null,
    val selectedCountry: String? = null,
    val weatherData: WeatherData? = null,
    val isLoadingWeather: Boolean = false,
    val error: String? = null
)
