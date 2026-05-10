package com.example.weathersnap.data.mapper

import com.example.weathersnap.data.local.WeatherReportEntity
import com.example.weathersnap.data.remote.dto.RemoteCity
import com.example.weathersnap.data.remote.dto.WeatherResponse
import com.example.weathersnap.domain.model.CityResult
import com.example.weathersnap.domain.model.WeatherData
import com.example.weathersnap.domain.model.WeatherReport

fun RemoteCity.toCityResult(): CityResult {
    return CityResult(
        name = name,
        country = country ?: "",
        latitude = latitude,
        longitude = longitude
    )
}

fun WeatherResponse.toWeatherData(cityName: String, country: String, lat: Double, lon: Double): WeatherData? {
    return current?.let {
        WeatherData(
            cityName = cityName,
            country = country,
            temperature = it.temperature,
            condition = mapWeatherCode(it.weatherCode),
            humidity = it.humidity,
            windSpeed = it.windSpeed,
            pressure = it.pressure,
            latitude = lat,
            longitude = lon
        )
    }
}

fun WeatherReportEntity.toWeatherReport(): WeatherReport {
    return WeatherReport(
        id = id,
        cityName = cityName,
        country = country,
        temperature = temperature,
        condition = condition,
        humidity = humidity,
        windSpeed = windSpeed,
        pressure = pressure,
        imagePath = imagePath,
        originalSize = originalSize,
        compressedSize = compressedSize,
        notes = notes,
        savedAt = savedAt
    )
}

fun WeatherReport.toEntity(): WeatherReportEntity {
    return WeatherReportEntity(
        id = id,
        cityName = cityName,
        country = country,
        temperature = temperature,
        condition = condition,
        humidity = humidity,
        windSpeed = windSpeed,
        pressure = pressure,
        imagePath = imagePath,
        originalSize = originalSize,
        compressedSize = compressedSize,
        notes = notes,
        savedAt = savedAt
    )
}

private fun mapWeatherCode(code: Int): String {
    return when (code) {
        0 -> "Clear sky"
        1, 2, 3 -> "Mainly clear, partly cloudy, and overcast"
        45, 48 -> "Fog and depositing rime fog"
        51, 53, 55 -> "Drizzle: Light, moderate, and dense intensity"
        56, 57 -> "Freezing Drizzle: Light and dense intensity"
        61, 63, 65 -> "Rain: Slight, moderate and heavy intensity"
        66, 67 -> "Freezing Rain: Light and heavy intensity"
        71, 73, 75 -> "Snow fall: Slight, moderate, and heavy intensity"
        77 -> "Snow grains"
        80, 81, 82 -> "Rain showers: Slight, moderate, and violent"
        85, 86 -> "Snow showers slight and heavy"
        95 -> "Thunderstorm: Slight or moderate"
        96, 99 -> "Thunderstorm with slight and heavy hail"
        else -> "Unknown"
    }
}
