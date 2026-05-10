package com.example.weathersnap.ui.createreport

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weathersnap.domain.model.WeatherData
import com.example.weathersnap.domain.model.WeatherReport
import com.example.weathersnap.domain.repository.WeatherRepository
import com.example.weathersnap.util.CompressionResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateReportViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateReportUiState())
    val uiState: StateFlow<CreateReportUiState> = _uiState.asStateFlow()

    fun setWeatherData(data: WeatherData) {
        _uiState.update { it.copy(weatherData = data) }
    }

    fun resetState() {
        _uiState.update { CreateReportUiState() }
    }

    fun onImageCaptured(result: CompressionResult) {
        _uiState.update { it.copy(compressionResult = result) }
    }

    fun onNotesChanged(notes: String) {
        _uiState.update { it.copy(notes = notes) }
    }

    fun saveReport(onSuccess: () -> Unit) {
        val state = _uiState.value
        val weather = state.weatherData ?: return
        val result = state.compressionResult

        viewModelScope.launch {
            val report = WeatherReport(
                cityName = weather.cityName,
                country = weather.country,
                temperature = weather.temperature,
                condition = weather.condition,
                humidity = weather.humidity,
                windSpeed = weather.windSpeed,
                pressure = weather.pressure,
                imagePath = result?.compressedPath ?: "",
                originalSize = result?.originalSize ?: 0L,
                compressedSize = result?.compressedSize ?: 0L,
                notes = state.notes
            )
            repository.saveReport(report)
            onSuccess()
        }
    }
}

data class CreateReportUiState(
    val weatherData: WeatherData? = null,
    val compressionResult: CompressionResult? = null,
    val notes: String = "",
    val isLoading: Boolean = false
)
