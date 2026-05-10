package com.example.weathersnap.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [WeatherReportEntity::class], version = 1, exportSchema = false)
abstract class WeatherDatabase : RoomDatabase() {
    abstract val dao: WeatherReportDao
}
