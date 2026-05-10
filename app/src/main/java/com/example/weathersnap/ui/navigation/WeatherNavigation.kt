package com.example.weathersnap.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.weathersnap.domain.model.WeatherData
import com.example.weathersnap.ui.camera.CameraScreen
import com.example.weathersnap.ui.createreport.CreateReportScreen
import com.example.weathersnap.ui.createreport.CreateReportViewModel
import com.example.weathersnap.ui.reports.ReportsScreen
import com.example.weathersnap.ui.weather.WeatherScreen
import com.google.gson.Gson
import java.net.URLDecoder
import java.net.URLEncoder

@Composable
fun WeatherNavigation() {
    val navController = rememberNavController()
    val weatherViewModel: com.example.weathersnap.ui.weather.WeatherViewModel = hiltViewModel()
    val createReportViewModel: CreateReportViewModel = hiltViewModel()

    NavHost(
        navController = navController,
        startDestination = "weather",
        enterTransition = { slideInHorizontally(animationSpec = tween(400), initialOffsetX = { it }) + fadeIn(animationSpec = tween(400)) },
        exitTransition = { slideOutHorizontally(animationSpec = tween(400), targetOffsetX = { -it }) + fadeOut(animationSpec = tween(400)) },
        popEnterTransition = { slideInHorizontally(animationSpec = tween(400), initialOffsetX = { -it }) + fadeIn(animationSpec = tween(400)) },
        popExitTransition = { slideOutHorizontally(animationSpec = tween(400), targetOffsetX = { it }) + fadeOut(animationSpec = tween(400)) }
    ) {
        composable("weather") {
            WeatherScreen(
                viewModel = weatherViewModel,
                onNavigateToCreateReport = { weatherData ->
                    createReportViewModel.resetState()
                    weatherViewModel.clearSearch()
                    val json = URLEncoder.encode(Gson().toJson(weatherData), "UTF-8")
                    navController.navigate("create_report/$json")
                },
                onNavigateToReports = {
                    navController.navigate("reports")
                }
            )
        }

        composable("create_report/{weatherDataJson}") { backStackEntry ->
            val json = backStackEntry.arguments?.getString("weatherDataJson") ?: ""
            val weatherData = Gson().fromJson(URLDecoder.decode(json, "UTF-8"), WeatherData::class.java)

            CreateReportScreen(
                weatherData = weatherData,
                viewModel = createReportViewModel,
                onNavigateToCamera = { navController.navigate("camera") },
                onNavigateBack = { navController.popBackStack() },
                onSaveSuccess = {
                    navController.navigate("reports") {
                        popUpTo("weather")
                    }
                }
            )
        }

        composable("camera") {
            CameraScreen(
                onBack = { navController.popBackStack() },
                onResult = { result ->
                    createReportViewModel.onImageCaptured(result)
                    navController.popBackStack()
                }
            )
        }

        composable("reports") {
            ReportsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
