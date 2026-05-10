package com.example.weathersnap.data.remote.dto

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("current") val current: CurrentWeatherData?
)

data class CurrentWeather(
    @SerializedName("temperature_2m") val temperature: Double,
    @SerializedName("weathercode") val weatherCode: Int,
    @SerializedName("windspeed_10m") val windSpeed: Double,
    @SerializedName("relativehumidity_2m") val humidity: Int,
    @SerializedName("surface_pressure") val pressure: Double
)

// Renamed slightly to avoid confusion with the domain model if needed, but let's follow the JSON structure.
data class CurrentWeatherData(
    @SerializedName("temperature_2m") val temperature: Double,
    @SerializedName("weathercode") val weatherCode: Int,
    @SerializedName("windspeed_10m") val windSpeed: Double,
    @SerializedName("relativehumidity_2m") val humidity: Int,
    @SerializedName("surface_pressure") val pressure: Double
)
