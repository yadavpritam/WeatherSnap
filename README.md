# WeatherSnap

## 1. Project Overview
WeatherSnap is a production-quality Android application that allows users to search for live weather data across global cities and create detailed weather reports. Users can capture real-time evidence using a custom integrated camera, apply automated image compression, and save annotated reports to a local database for future reference.

### Tech Stack
- **Language:** Kotlin
- **UI:** Jetpack Compose + Material 3
- **Architecture:** MVVM (ViewModel + StateFlow) + Clean Architecture
- **Dependency Injection:** Hilt
- **Navigation:** Navigation Compose with animated transitions
- **Networking:** Retrofit + Gson + OkHttp Logging Interceptor
- **Local Database:** Room Persistence Library
- **Camera:** CameraX (Custom implementation)
- **Async/Concurrency:** Coroutines + Flow
- **Image Loading:** Coil
- **Splash Screen:** AndroidX Core SplashScreen API

## 2. Prerequisites
- **Android Studio:** Hedgehog (2023.1.1) or newer
- **Minimum SDK:** 26 (Android 8.0)
- **Target SDK:** 34 (Android 14)
- **Kotlin:** 1.9.0+
- **JVM Target:** 17
- Internet connection required for weather data and city geocoding

## 3. Setup & Run
1. Clone the repository:
```bash
   git clone https://github.com/yadavpritam/WeatherSnap.git
```
2. Open in Android Studio — select the root project folder
3. No API keys required — the app uses Open-Meteo which is free
4. Sync Gradle via `File > Sync Project with Gradle Files`
5. Run on an emulator or physical device

## 4. Camera Permission
The app requires `CAMERA` permission at runtime.
- Permission is requested when the user taps "Capture Photo" for the first time
- Grant it when prompted to enable the custom camera

## 5. Project Structure
com.example.weathersnap
├── data/       → Retrofit API, Room DB, repositories, mappers
├── di/         → Hilt modules (Network, Database, Repository)
├── domain/     → Data models, repository interfaces
├── ui/         → Screens, ViewModels, Theme, Navigation
└── util/       → ImageCompressor

## 6. Screens
- **Weather Screen** — City search with autocomplete, live weather display
- **Create Report Screen** — Weather snapshot + camera photo + notes
- **Custom Camera Screen** — CameraX full-screen capture interface
- **Saved Reports Screen** — All saved reports from local Room DB

## 7. Key Features
- City autocomplete with in-memory caching (no repeated API calls)
- Live weather via Open-Meteo (no API key needed)
- Custom CameraX implementation — no device camera intent used
- Image compression at ~70% quality, max 1024px
- Room DB local storage, all ops on Dispatchers.IO
- Olive Green Material 3 theme with slide/fade animations
- Native Android 12+ Splash Screen API

## 8. Bonus Features
- Debug-only network logging via `HttpLoggingInterceptor`
- All DB and image ops strictly on `Dispatchers.IO`
- Animated state transitions using `Crossfade` and `AnimatedVisibility`

Bas yeh poora copy karke apni README.md file mein replace kar do aur push kar do.You said: isko profatoaonal waay mein likhisko profatoaonal waay mein likh2:34 AMClaude responded: ---
markdown# WeatherSnap

A production-quality Android application for creating live weather reports
with camera evidence, built as part of an Android internship assignment.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose + Material 3 |
| Architecture | MVVM + Clean Architecture |
| DI | Hilt |
| Navigation | Navigation Compose |
| Networking | Retrofit + Gson + OkHttp |
| Database | Room Persistence Library |
| Camera | CameraX |
| Async | Coroutines + Flow |
| Image Loading | Coil |
| Splash Screen | AndroidX Core SplashScreen API |

---

## Requirements

- Android Studio Hedgehog (2023.1.1) or newer
- Minimum SDK 26 (Android 8.0 Oreo)
- Target SDK 34 (Android 14)
- Kotlin 1.9.0+
- JVM Target 17
- Active internet connection for weather and geocoding APIs

---

## Getting Started

```bash
git clone https://github.com/yadavpritam/WeatherSnap.git
```

1. Open the project in Android Studio
2. Let Gradle sync automatically — no manual configuration needed
3. No API keys required — the app uses [Open-Meteo](https://open-meteo.com),
   which is free and open
4. Run on a physical device or emulator (API 26+)

---

## Permissions

The app requests `CAMERA` permission at runtime when the user first attempts
to capture a photo. No permissions are required at install time.

---

## Project Structure
com.example.weathersnap
├── data/          Retrofit API client, Room database, repositories, mappers
├── di/            Hilt modules — Network, Database, Repository bindings
├── domain/        Business models, repository interfaces
├── ui/            Composable screens, ViewModels, theme, navigation
└── util/          ImageCompressor utility

---

## Screens

**Weather Screen**
Search any city with real-time autocomplete suggestions. Displays current
temperature, condition, humidity, wind speed, and pressure.

**Create Report Screen**
Combines the weather snapshot with a captured photo and user notes into
a single report ready to save locally.

**Custom Camera Screen**
A full-screen CameraX-based camera interface built entirely within the app —
no system camera intent is used.

**Saved Reports Screen**
Displays all locally stored reports with captured image, weather details,
notes, image sizes, and timestamp.

---

## Features

- Real-time city autocomplete with in-memory caching
- Live weather data via Open-Meteo — no API key required
- Custom CameraX implementation — no ACTION_IMAGE_CAPTURE intent
- Automatic image compression (~70% quality, max 1024px)
- Full offline report viewing from Room database
- Material 3 design with smooth slide and fade transitions
- Native Android 12+ splash screen via AndroidX SplashScreen API

---

## Bonus

- Network requests logged to Logcat in DEBUG builds only
- All database and image operations run on `Dispatchers.IO`
- State transitions animated using `Crossfade` and `AnimatedVisibility`

---

## Author

[yadavpritam](https://github.com/yadavpritam)
