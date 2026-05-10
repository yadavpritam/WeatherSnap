package com.example.weathersnap.domain.model

data class WeatherReport(
    val id: Int = 0,
    val cityName: String,
    val country: String,
    val temperature: Double,
    val condition: String,
    val humidity: Int,
    val windSpeed: Double,
    val pressure: Double,
    val imagePath: String,
    val originalSize: Long,
    val compressedSize: Long,
    val notes: String,
    val savedAt: Long = System.currentTimeMillis()
)
