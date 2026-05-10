# WeatherSnap

A production-quality Android application for creating live weather reports
with camera evidence, built as part of an Android internship assignment.

## Screen Recording
[▶ Watch Full App Demo](https://drive.google.com/file/d/13lcPeSm0NnfRLrGmucNpvYE2Ufd3vPJy/view?usp=sharing)

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
3. No API keys required — the app uses [Open-Meteo](https://open-meteo.com), which is free and open
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
