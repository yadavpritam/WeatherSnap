package com.example.weathersnap.domain.repository

import com.example.weathersnap.domain.model.CityResult
import com.example.weathersnap.domain.model.WeatherData
import com.example.weathersnap.domain.model.WeatherReport
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun searchCity(query: String): Result<List<CityResult>>
    suspend fun getWeatherData(latitude: Double, longitude: Double, cityName: String, country: String): Result<WeatherData>
    suspend fun saveReport(report: WeatherReport)
    fun getAllReports(): Flow<List<WeatherReport>>
}
