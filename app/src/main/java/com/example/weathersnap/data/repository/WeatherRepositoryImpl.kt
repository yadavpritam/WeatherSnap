package com.example.weathersnap.data.repository

import com.example.weathersnap.data.local.WeatherReportDao
import com.example.weathersnap.data.remote.WeatherApi
import com.example.weathersnap.data.mapper.*
import com.example.weathersnap.domain.model.CityResult
import com.example.weathersnap.domain.model.WeatherData
import com.example.weathersnap.domain.model.WeatherReport
import com.example.weathersnap.domain.repository.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi,
    private val dao: WeatherReportDao
) : WeatherRepository {

    override suspend fun searchCity(query: String): Result<List<CityResult>> {
        return try {
            val response = api.searchCity(query)
            Result.success(response.results?.map { it.toCityResult() } ?: emptyList())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getWeatherData(
        latitude: Double,
        longitude: Double,
        cityName: String,
        country: String
    ): Result<WeatherData> {
        return try {
            val response = api.getWeatherData(latitude, longitude)
            response.toWeatherData(cityName, country, latitude, longitude)?.let {
                Result.success(it)
            } ?: Result.failure(Exception("Empty weather data"))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun saveReport(report: WeatherReport) {
        dao.insertReport(report.toEntity())
    }

    override fun getAllReports(): Flow<List<WeatherReport>> {
        return dao.getAllReports().map { entities ->
            entities.map { it.toWeatherReport() }
        }
    }
}
