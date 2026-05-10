# WeatherSnap

## 1. Project Overview
WeatherSnap is a production-quality Android application that allows users to search for live weather data across global cities and create detailed weather reports. Users can capture real-time evidence using a custom integrated camera, apply automated image compression, and save annotated reports to a local database for future reference.

### Tech Stack
*   **Language:** Kotlin
*   **UI:** Jetpack Compose + Material 3
*   **Architecture:** MVVM (ViewModel + StateFlow) + Clean Architecture principles
*   **Dependency Injection:** Hilt
*   **Navigation:** Navigation Compose with animated transitions
*   **Networking:** Retrofit + Gson + OkHttp Logging Interceptor
*   **Local Database:** Room Persistence Library
*   **Camera:** CameraX (Custom implementation)
*   **Async/Concurrency:** Coroutines + Flow
*   **Image Loading:** Coil
*   **Splash Screen:** AndroidX Core SplashScreen API

## 2. Prerequisites
*   **Android Studio:** Hedgehog (2023.1.1) or newer
*   **Minimum SDK:** 26 (Android 8.0)
*   **Target SDK:** 34 (Android 14)
*   **Kotlin:** 1.9.0+
*   **JVM Target:** 17
*   **Internet:** Required for weather data and city geocoding APIs.

## 3. Setup & Run
1.  **Clone the repository:**
    ```bash
    git clone https://github.com/yourusername/weathersnap.git
    ```
2.  **Open in Android Studio:** Select the root project folder.
3.  **API Keys:** No API keys are required. The project uses Open-Meteo, which is free for non-commercial use.
4.  **Gradle Sync:** Allow Android Studio to sync dependencies automatically or trigger it manually via `File > Sync Project with Gradle Files`.
5.  **Run:** Select an emulator or a physical device and click the `Run` button.

## 4. Camera Permission
The application requires the `CAMERA` permission to capture evidence for weather reports.
*   The permission is requested at runtime when the user navigates to the Camera screen for the first time.
*   Please grant the permission when prompted to enable the full functionality of the custom camera.

## 5. Project Structure
The project follows a modular structure based on Clean Architecture:
*   `data/`: Implementation of data sources (Remote API via Retrofit and Local DB via Room), repositories, and mappers.
*   `di/`: Hilt modules for providing Network, Database, and Repository instances.
*   `domain/`: Core business logic including clean data models and Repository interfaces.
*   `ui/`: Composable screens, ViewModels, Theme, and Navigation.
*   `util/`: Image compression utility.

## 6. Screens
*   **Weather Screen:** Search for cities with real-time suggestions and view current weather conditions.
*   **Create Report Screen:** Assemble weather data, captured photos, and personal field notes into a report.
*   **Custom Camera Screen:** A bespoke CameraX-based interface for capturing weather evidence photos.
*   **Saved Reports Screen:** A list-based view of all historically saved reports retrieved from the local Room database.

## 7. Key Features
*   **City Autocomplete:** Debounced city search with in-memory caching to minimize API calls.
*   **Open-Meteo Integration:** Fetches accurate weather forecasts without requiring an API key.
*   **Custom CameraX:** Full-screen camera preview and capture flow integrated directly into the app UI.
*   **Automated Compression:** Captures are compressed to ~70% quality and max 1024px to balance clarity and storage.
*   **Room DB Persistence:** Reports are stored locally and sorted by capture time.
*   **Modern UI/UX:** Features a custom Olive Green theme with smooth slide/fade animations and responsive cards.
*   **Native Splash Screen:** Implements the official Android 12+ SplashScreen API for a seamless cold-start experience.

## 8. Bonus Features
*   **Debug-only Logging:** Network requests are logged to Logcat only in DEBUG builds via `HttpLoggingInterceptor`.
*   **Optimized Performance:** All database and image processing operations are strictly offloaded to `Dispatchers.IO`.
*   **Animated State Transitions:** Uses `Crossfade` and `AnimatedVisibility` for loading and result states.
