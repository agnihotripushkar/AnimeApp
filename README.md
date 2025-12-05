# AnimeApp

A modern Android application for browsing, searching, and tracking anime, built with Jetpack Compose and Clean Architecture.

## 📱 Screenshots

| Home | Details | Auth |
|:---:|:---:|:---:|
| ![Home](https://github.com/user-attachments/assets/76cd0684-3861-4b89-9ab4-4233f7e66fb7) | ![Details](https://github.com/user-attachments/assets/475e5eab-6c5c-4581-ae69-68a1b12363db) | ![Auth](https://github.com/user-attachments/assets/0922c7f0-94c9-4092-a2a7-783b9fa562be) |

## ✨ Features

- **Home Feed**: Browse anime with Compact and Expanded views.
- **Trending**: Discover what's currently popular in the anime world.
- **Detailed Insights**: View comprehensive information about anime series.
- **Favorites Management**: Save and manage your favorite anime.
- **Authentication**: secure login and signup functionality.
- **Biometric Security**: Protect your account with biometric authentication.
- **Onboarding**: Smooth onboarding experience for new users.

## 🛠 Tech Stack

- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI**: [Jetpack Compose](https://developer.android.com/jetpack/compose) (Material 3)
- **Architecture**: MVVM + Clean Architecture + Multi-module
- **Dependency Injection**: [Koin](https://insert-koin.io/)
- **Network**: [Ktor](https://ktor.io/)
- **Database**: [Room](https://developer.android.com/training/data-storage/room)
- **Image Loading**: [Coil](https://coil-kt.github.io/coil/)
- **Asynchronous**: Coroutines & Flow
- **Navigation**: Compose Navigation
- **Other**: DataStore, Paging 3, Biometric Auth

## 📂 Project Structure

 The project follows a feature-based modular structure:

```
com.devpush.animeapp
├── core            # Common utilities and extensions
├── data            # Data layer implementation
├── domain          # Domain layer (UseCases, Repository interfaces)
├── features        # Feature-specific modules
│   ├── auth        # Authentication flows
│   ├── home        # Home screen interactions
│   ├── details     # Anime details
│   ├── trending    # Trending anime
│   ├── favorited   # Favorites list
│   └── settings    # App settings
└── ui              # Common UI components and theme
```

## 🚀 Getting Started

1. Clone the repository.
2. Open in Android Studio.
3. Sync Gradle project.
4. Run on an emulator or physical device.

Note: The app uses `https://kitsu.io/api/edge/` as the base URL.
