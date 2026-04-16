# MovieX

A modern Android application for browsing, searching, and exploring movies and TV shows, powered by the [TMDB API](https://www.themoviedb.org/documentation/api).

---

## Overview

MovieX is a native Android app built with modern Android development practices. It provides users with an intuitive interface to discover trending movies, search for titles, view detailed information including cast and reviews, and save favorites for later.

---

## Tech Stack

| Category | Technology |
|----------|------------|
| Language | Kotlin |
| UI Framework | Jetpack Compose |
| Architecture | MVVM + Clean Architecture |
| Networking | Retrofit 2 + OkHttp |
| Dependency Injection | Hilt |
| Async Operations | Coroutines + Flow |
| Image Loading | Coil |
| Navigation | Jetpack Navigation Compose |
| Local Storage | Room (Planned) |
| Serialization | Kotlinx Serialization |

---

## Features

| Feature | Description | Status |
|---------|-------------|--------|
| Splash Screen | App branding and initial load | Done |
| Onboarding | First-time user introduction flow | Done |
| Home Screen | Browse trending, popular, and categorized movies | Done |
| Movie Detail | Full movie info with cast, reviews, and ratings | Done |
| Search | Search movies and TV shows by title | Planned |
| Favorites | Save and manage favorite movies locally | Planned |
| Authentication | User login and signup | Done |
| Settings | App settings and sign out | Done |

---

## Screens

- **Splash** - App launch with logo and branding
- **Onboarding** - 4-page introduction with pager and indicators
- **Login/Signup** - Authentication screens
- **Home** - Category-based movie browsing (Trending, Most Watched, TV Shows, Action, Drama, Comedy)
- **Movie Detail** - Backdrop image, title, rating, genres, overview, cast, reviews
- **Settings** - Sign out functionality

---

## Prerequisites

| Requirement | Version | Notes |
|-------------|---------|-------|
| Android Studio | Meerkat (2024.3.1+) | Recommended IDE |
| JDK | 11+ | Bundled with Android Studio |
| Android SDK | API 24+ | Minimum API 24 |
| Android Gradle Plugin | 8.2+ | Compatible with AGP 8.2+ |
| Gradle | 8.2+ | Wrapper included |

---

## Quick Start

```bash
# Clone the repository
git clone https://github.com/jAmikA78/MovieX.git
cd MovieX

# Add TMDB API key
echo "TMDB_API_KEY=your_key_here" >> local.properties

# Build debug APK
./gradlew assembleDebug

# Run on emulator or device
./gradlew installDebug
```

Or open the project in **Android Studio** and click Run.

---

## Project Structure

```
app/src/main/java/com/depi/moviex/
├── MainActivity.kt           # App entry and navigation
├── MovieApplication.kt       # Hilt application
├── auth/                     # Authentication module
│   ├── data/                 # Repository impl, API, models
│   ├── di/                   # Hilt module
│   └── domain/               # Use cases, repo interface
├── movie/                    # Movie listing module
│   ├── data/                 # Repository impl, API, mappers
│   └── domain/               # Models, repo interface
├── movie_detail/             # Movie detail module
│   ├── data/                 # Repository impl, API, models
│   └── domain/               # Models, repo interface
├── di/                       # Hilt modules
├── ui/theme/                 # Compose theme, colors, screens
│   └── screens/              # All app screens
└── utils/                    # Constants, extensions, helpers
```

---

## Documentation

| Document | Description |
|----------|-------------|
| [Setup & Development](Docs/Setup%26Development.md) | Environment setup, build, ADB, Docker |
| [Architecture](Docs/Architecture.md) | Clean Architecture layers and patterns |
| [Features](Docs/Features.md) | Feature breakdown and implementation details |
| [API Integration](Docs/API.md) | TMDB API endpoints and integration |
| [Checklist](Docs/Checklist.md) | Development checklist |
| [Milestones](Docs/Milestone.md) | Project milestones |

---

## Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature/your-feature`
3. Make changes and commit: `git commit -m 'feat: add your feature'`
4. Push to remote: `git push origin feature/your-feature`
5. Open a Pull Request

Follow [Conventional Commits](https://www.conventionalcommits.org/) for commit messages.

---

## License

[MIT License](LICENSE)